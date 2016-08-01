package rikka.akashitoolkit.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.TwitterAdapter;
import rikka.akashitoolkit.model.Avatars;
import rikka.akashitoolkit.model.LatestAvatar;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.ui.GalleryActivity;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterFragment extends Fragment implements TwitterAdapter.Listener {
    private static final String TAG = "TwitterFragment";

    private static final String JSON_NAME = "/json/twitter.json";
    private static final String JSON_NAME_AVATARS = "/json/twitter_avatars.json";
    private String CACHE_FILE;
    private String CACHE_FILE_AVATARS;

    private RecyclerView mRecyclerView;
    private TwitterAdapter mTwitterAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Call<Twitter> mCall;
    private Call<LatestAvatar> mCall2;

    private Avatars mAvatars;

    @Override
    public void onStop() {
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }

        if (mCall2 != null && mCall2.isExecuted()) {
            mCall2.cancel();
        }

        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        BusProvider.instance().register(this);

        CACHE_FILE = getContext().getCacheDir().getAbsolutePath() + JSON_NAME;
        CACHE_FILE_AVATARS = getContext().getCacheDir().getAbsolutePath() + JSON_NAME_AVATARS;
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

        mTwitterAdapter = new TwitterAdapter();
        mTwitterAdapter.setListener(this);
        mRecyclerView.setAdapter(mTwitterAdapter);
        mTwitterAdapter.setMaxItem(Settings
                .instance(getContext())
                .getIntFromString(Settings.TWITTER_COUNT, 30));

        mTwitterAdapter.setLanguage(Settings
                .instance(getContext())
                .getIntFromString(Settings.TWITTER_LANGUAGE, 0));

        mTwitterAdapter.setAvatarUrl(Settings
                .instance(getContext())
                .getString(Settings.TWITTER_AVATAR_URL, ""));

        setUpRecyclerView();

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

        /*if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }*/

        return view;
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

    private void loadFromCache() {
        Twitter twitter;
        try {
            Gson gson = new Gson();
            twitter = gson.fromJson(
                    new FileReader(CACHE_FILE),
                    Twitter.class);

            mAvatars = gson.fromJson(
                    new FileReader(CACHE_FILE_AVATARS),
                    Avatars.class);

            updateData(twitter, false);
        } catch (FileNotFoundException ignored) {
        }
    }

    private void updateData(Twitter source, boolean animate) {
        if (source == null || source.getPosts() == null) {
            return;
        }

        if (getContext() == null) {
            return;
        }

        List<TwitterAdapter.DataModel> data;

        int id = -1;
        int added = 0;
        if (mTwitterAdapter.getData().size() > 0) {
            id = mTwitterAdapter.getData().get(0).getId();
        }

        data = new ArrayList<>();

        for (Twitter.PostsEntity entity:
                source.getPosts()) {
            TwitterAdapter.DataModel item = new TwitterAdapter.DataModel();
            String content = entity.getContent();

            Pattern r = Pattern.compile("<p>[\\w\\W]+?</p>");
            Matcher m = r.matcher(content);

            int i = 0;
            while (m.find()) {
                if (i == 0) {
                    item.setText(
                            cutString(m.group(), "<p>", "</p>"));
                } else {
                    item.setTranslated(
                            // bad way
                            (i > 1 ? item.getTranslated() + "\n" : "") +
                            cutString(m.group(), "<p>", "</p>"));
                }
                i ++;
            }

            item.setDate(entity.getDate());
            item.setId(entity.getId());
            item.setModified(entity.getModified());
            data.add(item);

            r = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
            m = r.matcher(entity.getDate());
            if (m.find()) {
                int yy = Integer.parseInt(m.group(1));
                int MM = Integer.parseInt(m.group(2));
                int DD = Integer.parseInt(m.group(3));
                int HH = Integer.parseInt(m.group(4));
                int mm = Integer.parseInt(m.group(5));
                int ss = Integer.parseInt(m.group(6));
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.getDefault());
                calendar.set(yy, MM - 1, DD, HH, mm, ss);
                item.setTime(calendar.getTimeInMillis());
            }

            if (id != -1 && entity.getId() > id) {
                added++;
            }
        }

        mTwitterAdapter.setData(data);
        mTwitterAdapter.notifyDataSetChanged();
        if (animate) {
            mRecyclerView.scrollToPosition(0);

            if (added == 0) {
                Snackbar.make(mSwipeRefreshLayout, R.string.no_new_tweet, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mSwipeRefreshLayout, String.format(getString(R.string.new_twitter), added), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        refresh(1,
                Settings.instance(getContext())
                        .getIntFromString(Settings.TWITTER_COUNT, 30));

        Log.d(TAG, "start refresh");
    }

    private void refresh(final int json, final int count) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.kcwiki.moe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.TwitterService service = retrofit.create(RetrofitAPI.TwitterService.class);
        mCall2 = service.getLatestAvatarUrl();
        mCall2.enqueue(new Callback<LatestAvatar>() {
            @Override
            public void onResponse(Call<LatestAvatar> call, Response<LatestAvatar> response) {
                String url = response.body().getLatest();

                if (url != null) {
                    mTwitterAdapter.setAvatarUrl(url);
                    Settings.instance(getContext()).putString(Settings.TWITTER_AVATAR_URL, url);
                }
            }

            @Override
            public void onFailure(Call<LatestAvatar> call, Throwable t) {
            }
        });

        retrofit.create(RetrofitAPI.TwitterService.class)
                .getAvatars()
                .enqueue(new Callback<Avatars>() {
                    @Override
                    public void onResponse(Call<Avatars> call, Response<Avatars> response) {
                        mAvatars = response.body();

                        Gson gson = new Gson();
                        Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                                CACHE_FILE_AVATARS);
                    }

                    @Override
                    public void onFailure(Call<Avatars> call, Throwable t) {

                    }
                });


        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://t.kcwiki.moe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.TwitterService service2 = retrofit2.create(RetrofitAPI.TwitterService.class);
        mCall = service2.get(json, count);

        mCall.enqueue(new Callback<Twitter>() {
            @Override
            public void onResponse(Call<Twitter> call, Response<Twitter> response) {
                mSwipeRefreshLayout.setRefreshing(false);

                updateData(response.body(), true);
                // save result to local
                Gson gson = new Gson();
                Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                        CACHE_FILE);
            }

            @Override
            public void onFailure(Call<Twitter> call, Throwable t) {
                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);

                Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private String cutString(String string, String a, String b) {
        int len = string.length();
        return string.substring(a.length(), len - b.length());
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        switch (action.getKey()) {
            case Settings.TWITTER_COUNT:
                mTwitterAdapter.setMaxItem(
                        Settings
                                .instance(getContext())
                                .getIntFromString(Settings.TWITTER_COUNT, 30));

                mTwitterAdapter.getData().clear();
                mTwitterAdapter.notifyDataSetChanged();

                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        loadFromCache();
                    }
                });
                break;
            case Settings.TWITTER_LANGUAGE:
                mTwitterAdapter.setLanguage(
                        Settings
                                .instance(getContext())
                                .getIntFromString(Settings.TWITTER_LANGUAGE, 0));

                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTwitterAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case Settings.TWITTER_GRID_LAYOUT:
                setUpRecyclerView();
                break;
        }
    }

    @Override
    public void onMoreButtonClick(TwitterAdapter.ShareDataModel data) {
        TwitterMoreDialogFragment f = new TwitterMoreDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", data.text);
        bundle.putString("TRANSLATE", data.translated);
        f.setArguments(bundle);

        f.setTargetFragment(this, 0);
        f.show(getChildFragmentManager(), TAG);
    }

    @Override
    public void onAvatarLongClick() {
        if (mAvatars == null || mAvatars.getArchives() == null) {
            Toast.makeText(getContext(), "Download not finished or failed.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> list = new ArrayList<>();
        for (int i = mAvatars.getArchives().size() - 1; i >= 0; i--) {
            list.add(String.format("%s%s", mAvatars.getBase(), mAvatars.getArchives().get(i)));
        }
        GalleryActivity.start(getContext(), list, getString(R.string.twitter_history_avatars));
    }
}