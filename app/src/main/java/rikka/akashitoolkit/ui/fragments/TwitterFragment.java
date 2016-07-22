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

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
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
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterFragment extends Fragment implements TwitterAdapter.OnMoreButtonClickedListener {
    private static final String TAG = "TwitterFragment";

    private static final String JSON_NAME = "/json/twitter.json";
    private String CACHE_FILE;

    private RecyclerView mRecyclerView;
    private TwitterAdapter mTwitterAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Call<Twitter> mCall;
    private Call<ResponseBody> mCall2;

    /*@Override
    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        activity.getSupportActionBar().setTitle(getString(R.string.official_twitter));

        mSwipeRefreshLayout.setRefreshing(false);

        Statistics.onFragmentStart("TwitterFragment");
    }*/

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

    /*@Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("TwitterFragment");
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        BusProvider.instance().register(this);

        CACHE_FILE = getContext().getCacheDir().getAbsolutePath() + JSON_NAME;
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
        mTwitterAdapter.setOnMoreButtonClickedListener(this);
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

        setUpRecyclerView(false);

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

    private void setUpRecyclerView(boolean refresh) {
        /*if (mTwitterAdapter != null) {
            mTwitterAdapter.setData(new ArrayList<TwitterAdapter.DataModel>());
            mTwitterAdapter.notifyDataSetChanged();
        }*/

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

        /*if (refresh) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadFromCache();
                }
            }, 3000);
        }*/
    }

    private void loadFromCache() {
        Twitter twitter;
        try {
            Gson gson = new Gson();
            twitter = gson.fromJson(
                    new FileReader(CACHE_FILE),
                    Twitter.class);

            updateData(twitter, false);
        } catch (FileNotFoundException ignored) {
        }

        /*if (twitter != null) {
            updateData(twitter, false);
        }*/
    }

    private void updateData(Twitter source, boolean animate) {
        if (source == null || source.getPosts() == null) {
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

            /*if (added == 0) {
                Snackbar.make(mSwipeRefreshLayout, R.string.no_new_tweet, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mSwipeRefreshLayout, String.format(getString(R.string.new_twitter), added), Snackbar.LENGTH_SHORT).show();
            }*/
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
                setUpRecyclerView(true);
                break;
        }

    }

    @Override
    public void onClicked(TwitterAdapter.ShareDataModel data) {
        TwitterMoreDialogFragment f = new TwitterMoreDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", data.text);
        bundle.putString("TRANSLATE", data.translated);
        f.setArguments(bundle);

        f.setTargetFragment(this, 0);
        f.show(getChildFragmentManager(), TAG);
    }
}