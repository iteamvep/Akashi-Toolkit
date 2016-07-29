package rikka.akashitoolkit.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.Listener;
import rikka.akashitoolkit.adapter.MessageAdapter;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.model.MessageReadStatus;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ReadStatusResetAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.utils.UpdateCheck;
import rikka.akashitoolkit.utils.Utils;

import static rikka.akashitoolkit.support.ApiConstParam.Message.COUNT_DOWN;

/**
 * Created by Rikka on 2016/6/11.
 */
public class MessageFragment extends Fragment {
    private static final String JSON_NAME = "/json/home.json";
    private String CACHE_FILE;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;

    private MessageReadStatus mMessageReadStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.instance().register(this);
        setHasOptionsMenu(true);
        CACHE_FILE = getContext().getCacheDir().getAbsolutePath() + JSON_NAME;

        loadReadStatus();
    }

    @Override
    public void onStop() {
        UpdateCheck.instance().recycle();
        saveReadStatus();
        super.onStop();
    }

    private void loadReadStatus() {
        mMessageReadStatus = Settings
                .instance(getContext())
                .getGSON(Settings.MESSAGE_READ_STATUS, MessageReadStatus.class);

        if (mMessageReadStatus == null) {
            mMessageReadStatus = new MessageReadStatus();
        }

        if (mMessageReadStatus.getMessageId() == null) {
            mMessageReadStatus.setMessageId(new ArrayList<Long>());
        }
    }

    private void saveReadStatus() {
        Settings.instance(getContext())
                .putGSON(Settings.MESSAGE_READ_STATUS, mMessageReadStatus);
    }

    @Override
    public void onDestroy() {
        BusProvider.instance().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    refresh();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_twitter_container, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));
        mAdapter = new MessageAdapter();
        mRecyclerView.setAdapter(mAdapter);
        setUpRecyclerView();

        mAdapter.setListener(new Listener() {
            @Override
            public void OnRemove(int position, Object data) {
                switch (mAdapter.getItemViewType(position)) {
                    case 0:
                        mMessageReadStatus.getMessageId().add(mAdapter.getItemId(position));
                        break;
                    case 1:
                        mMessageReadStatus.setVersionCode(((CheckUpdate.UpdateEntity) data).getVersionCode());
                        break;
                }

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        loadFromCache();

        if (savedInstanceState == null) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 500);
        }

        return view;
    }

    private void loadFromCache() {
        CheckUpdate data;
        try {
            Gson gson = new Gson();
            data = gson.fromJson(
                    new FileReader(CACHE_FILE),
                    CheckUpdate.class);

            // clear old messages
            if (data != null && data.getMessages() != null)
                data.getMessages().clear();

            updateData(data);
        } catch (FileNotFoundException ignored) {
        }
    }

    private void updateData(CheckUpdate data) {
        if (data == null) {
            return;
        }

        mAdapter.clear();

        addLocalCard();
        addUpdateCard(data.getUpdate());
        addMessageCard(data.getMessages());

        /*checkDataUpdate(data.getData());*/

        mAdapter.notifyDataSetChanged();
    }

    private void addLocalCard() {
        mAdapter.add(MessageAdapter.TYPE_DAILY_EQUIP, null, 0);
        mAdapter.add(MessageAdapter.TYPE_EXPEDITION_NOTIFY, null, 0);
    }

    private void addUpdateCard(final CheckUpdate.UpdateEntity entity) {
        if (BuildConfig.isGooglePlay) {
            return;
        }

        if (entity == null) {
            return;
        }

        int versionCode = StaticData.instance(getContext()).versionCode;

        if (versionCode <= mMessageReadStatus.getVersionCode()) {
            return;
        }

        if (entity.getVersionCode() > versionCode || BuildConfig.DEBUG) {
            mAdapter.add(MessageAdapter.TYPE_MESSAGE_UPDATE, entity, 0);
        }
    }

    private void addMessageCard(List<CheckUpdate.MessagesEntity> list) {
        if (list == null) {
            return;
        }

        for (final CheckUpdate.MessagesEntity entity :
                list) {
            // do not add card that time is early than now
            if ((entity.getType() & COUNT_DOWN) > 0) {
                if (entity.getTime() * DateUtils.SECOND_IN_MILLIS < System.currentTimeMillis()) {
                    continue;
                }
            }

            // skip message that read
            if (mMessageReadStatus.getMessageId().indexOf(entity.getId()) != -1) {
                continue;
            }

            if (entity.isShowFirst()) {
                mAdapter.add(MessageAdapter.TYPE_MESSAGE, entity, 0);
            } else {
                mAdapter.add(MessageAdapter.TYPE_MESSAGE, entity);
            }
        }
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        UpdateCheck.instance().check(getContext(), new Callback<CheckUpdate>() {
            @Override
            public void onResponse(Call<CheckUpdate> call, final Response<CheckUpdate> response) {
                UpdateCheck.instance().recycle();

                if (getContext() == null) {
                    Log.d("MessageFragment", "onResponse context == null");
                    return;
                }

                if (response.body() == null) {
                    onFailure(call, new NullPointerException());
                }

                updateData(response.body());

                UpdateCheck.instance().recycle();
                mSwipeRefreshLayout.setRefreshing(false);

                Gson gson = new Gson();
                Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                        CACHE_FILE);
            }

            @Override
            public void onFailure(Call<CheckUpdate> call, Throwable t) {
                UpdateCheck.instance().recycle();

                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);
                //Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private RecyclerView.ItemDecoration mItemDecoration;

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager;
        if (StaticData.instance(getActivity()).isTablet) {
            if (Settings.instance(getActivity()).getBoolean(Settings.TWITTER_GRID_LAYOUT, false)) {
                layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);

                if (mItemDecoration != null) {
                    mRecyclerView.removeItemDecoration(mItemDecoration);
                }
            } else {
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

                if (mItemDecoration == null) {
                    mItemDecoration = new RecyclerView.ItemDecoration() {
                        int width = 0;

                        @Override
                        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                            if (width == 0) {
                                width = Utils.dpToPx(560 + 8 + 8 + 8);
                            }

                            outRect.left = (mRecyclerView.getWidth() - width) / 2;
                            outRect.right = (mRecyclerView.getWidth() - width) / 2;
                        }
                    };
                }

                if (getResources().getDimension(R.dimen.card_width) != -1) {
                    mRecyclerView.addItemDecoration(mItemDecoration);
                }
            }

        } else {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Subscribe
    public void readStatusReset(ReadStatusResetAction action) {
        if (mMessageReadStatus != null) {
            mMessageReadStatus.setMessageId(new ArrayList<Long>());
            mMessageReadStatus.setVersionCode(0);
        }

        refresh();

        Toast.makeText(getActivity(), R.string.read_status_reset, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        if (action.getKey().equals(Settings.UPDATE_CHECK_CHANNEL)) {
            if (mMessageReadStatus != null) {
                mMessageReadStatus.setMessageId(new ArrayList<Long>());
                mMessageReadStatus.setVersionCode(0);
            }

            refresh();
        }
    }
}
