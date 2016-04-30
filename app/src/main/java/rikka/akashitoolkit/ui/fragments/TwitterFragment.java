package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.TwitterAdapter;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterFragment extends BaseFragment {
    private static final String TAG = "TwitterFragment";

    private static final int TAB_LAYOUT_VISIBILITY = View.GONE;
    private static final String CACHE_TWITTER_JSON_NAME = "/json/twitter.json";
    private String CACHE_DIR;

    private RecyclerView mRecyclerView;
    private TwitterAdapter mTwitterAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Call<Twitter> mCall;
    private Call<ResponseBody> mCall2;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getSupportActionBar().setTitle(getString(R.string.official_twitter));
        activity.setRightDrawerLocked(true);

        mSwipeRefreshLayout.setRefreshing(false);

        Statistics.onFragmentStart("TwitterFragment");
    }

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
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("TwitterFragment");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        BusProvider.instance().register(this);

        CACHE_DIR = getContext().getCacheDir().getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        BusProvider.instance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.twitter, menu);
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
        View view = inflater.inflate(R.layout.content_swipe_refresh, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mTwitterAdapter = new TwitterAdapter();
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

        FragmentManager fragmentManager = getFragmentManager();
        mTwitterAdapter.openImageShow(fragmentManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //layoutManager.setAutoMeasureEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);

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

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private void loadFromCache() {
        Twitter twitter;
        try {
            Gson gson = new Gson();
            twitter = gson.fromJson(
                    new FileReader(CACHE_DIR + CACHE_TWITTER_JSON_NAME),
                    Twitter.class);

            updateData(twitter, false);
        } catch (FileNotFoundException ignored) {
        }

        /*if (twitter != null) {
            updateData(twitter, false);
        }*/
    }

    private void updateData(Twitter source, boolean animate) {
        List<TwitterAdapter.DataModel> data;
        int id = 0;

        if (!animate) {
            data = new ArrayList<>();
        } else {
            data = mTwitterAdapter.getData();
            id = data.size() > 0 ? data.get(0).getId() : 0;
        }

        int added = 0;
        int modified = 0;
        int current = 0;

        if (source == null || source.getPosts() == null) {
            return;
        }

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

            if (animate) {
                if (current >= data.size()) {
                    data.add(item);
                } else if (entity.getId() != 0 && entity.getId() <= id && !entity.getModified().equals(data.get(current).getModified())) {
                    data.remove(current);
                    data.add(current, item);
                    modified ++;
                } else if (entity.getId() > id) {
                    data.add(added, item);
                    added++;
                    current++;
                }
                current ++;
            } else {
                data.add(item);
            }
        }

        mTwitterAdapter.setData(data);
        if (animate) {
            mTwitterAdapter.notifyItemRangeInserted(0, added);
            mTwitterAdapter.notifyItemRangeChanged(added, modified);
            if (added > 0 && mRecyclerView.getScrollY() <= 50) {
                mRecyclerView.scrollToPosition(0);
                showSnackbar(String.format(getString(R.string.new_twitter), added), Snackbar.LENGTH_SHORT);
            }

            if (added == 0) {
                showSnackbar(R.string.no_new_tweet, Snackbar.LENGTH_SHORT);
            }
        }
        else {
            mTwitterAdapter.notifyDataSetChanged();
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
                .baseUrl("http://static.kcwiki.moe")
                .build();

        RetrofitAPI.TwitterService service = retrofit.create(RetrofitAPI.TwitterService.class);
        mCall2 = service.getAvatarUrl();
        mCall2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                _refresh(json, count);

                try {
                    String url = response.body().string();
                    mTwitterAdapter.setAvatarUrl(url);
                    Settings.instance(getContext()).putString(Settings.TWITTER_AVATAR_URL, url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                _refresh(json, count);
            }
        });
    }

    private void _refresh(int json, int count) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://t.kcwiki.moe")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.TwitterService service = retrofit.create(RetrofitAPI.TwitterService.class);
        mCall = service.get(json, count);

        mCall.enqueue(new Callback<Twitter>() {
            @Override
            public void onResponse(Call<Twitter> call, Response<Twitter> response) {
                mSwipeRefreshLayout.setRefreshing(false);

                updateData(response.body(), true);
                // save result to local
                Gson gson = new Gson();
                Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                        CACHE_DIR + CACHE_TWITTER_JSON_NAME);
            }

            @Override
            public void onFailure(Call<Twitter> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);

                showSnackbar(R.string.refresh_fail, Snackbar.LENGTH_SHORT);
            }
        });
    }

    private String cutString(String string, String a, String b) {
        int len = string.length();
        return string.substring(a.length(), len - b.length());
    }


    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        if (action.getKey().equals(Settings.TWITTER_COUNT)) {
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
        } else if (action.getKey().equals(Settings.TWITTER_LANGUAGE)) {
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
        }
    }
}