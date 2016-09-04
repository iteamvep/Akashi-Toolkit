package rikka.akashitoolkit.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.Listener;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.model.MessageReadStatus;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ReadStatusResetAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.utils.NetworkUtils;

import static rikka.akashitoolkit.support.ApiConstParam.Message.COUNT_DOWN;

/**
 * Created by Rikka on 2016/6/11.
 */
public class MessageFragment extends BaseRefreshFragment<CheckUpdate> {

    private MessageAdapter mAdapter;

    private MessageReadStatus mMessageReadStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.instance().register(this);
        setHasOptionsMenu(true);

        loadReadStatus();
    }

    @Override
    public void onStop() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_twitter_container, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));
        mAdapter = new MessageAdapter();
        mRecyclerView.setAdapter(mAdapter);

        GridRecyclerViewHelper.init(mRecyclerView);

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

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    @Override
    public void onSuccess(@NonNull CheckUpdate data) {
        mAdapter.clearItemList();

        addLocalCard();
        addUpdateCard(data.getUpdate());
        addMessageCard(data.getMessages());

        /*checkDataUpdate(data.getData());*/

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<CheckUpdate> call, Throwable t) {

    }

    private void addLocalCard() {
        mAdapter.addItem(MessageAdapter.TYPE_DAILY_EQUIP, null, 0);
        mAdapter.addItem(MessageAdapter.TYPE_EXPEDITION_NOTIFY, null, 0);
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
            mAdapter.addItem(MessageAdapter.TYPE_MESSAGE_UPDATE, entity, 0);
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
                mAdapter.addItem(MessageAdapter.TYPE_MESSAGE, entity, 0);
            } else {
                mAdapter.addItem(MessageAdapter.TYPE_MESSAGE, entity);
            }
        }
    }

    @Override
    public void onRefresh(Call<CheckUpdate> call, boolean force_cache) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(NetworkUtils.getClient(force_cache))
                .baseUrl("http://app.kcwiki.moe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int channel = Settings
                .instance(getActivity())
                .getIntFromString(Settings.UPDATE_CHECK_CHANNEL, 0);

        RetrofitAPI.UpdateAPI service = retrofit.create(RetrofitAPI.UpdateAPI.class);
        call = service.get(5, channel);

        super.onRefresh(call, force_cache);
    }

    @Subscribe
    public void readStatusReset(ReadStatusResetAction action) {
        if (mMessageReadStatus != null) {
            mMessageReadStatus.setMessageId(new ArrayList<Long>());
            mMessageReadStatus.setVersionCode(0);
        }

        onRefresh(true);

        Toast.makeText(getActivity(), R.string.read_status_reset, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        switch (action.getKey()) {
            case Settings.UPDATE_CHECK_CHANNEL:
                if (mMessageReadStatus != null) {
                    mMessageReadStatus.setMessageId(new ArrayList<Long>());
                    mMessageReadStatus.setVersionCode(0);
                }

                onRefresh(true);
                break;
            case Settings.TWITTER_GRID_LAYOUT:
                GridRecyclerViewHelper.init(mRecyclerView);
                break;
        }
    }
}
