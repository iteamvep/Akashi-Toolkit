package rikka.akashitoolkit.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

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
import rikka.akashitoolkit.model.Avatars;
import rikka.akashitoolkit.model.LatestAvatar;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.gallery.GalleryActivity;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.NetworkUtils;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterFragment extends Fragment implements TwitterAdapter.Listener {
    private static final String TAG = "TwitterFragment";

    private RecyclerView mRecyclerView;
    private TwitterAdapter mTwitterAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Call<List<Twitter>> mCall;
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
                    refresh(false);
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

        GridRecyclerViewHelper.init(mRecyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refresh(true);
            }
        });

        if (savedInstanceState == null) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh(false);
                }
            }, 500);
        }
        /*if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }*/

        return view;
    }

    private void updateData(List<Twitter> list, boolean animate) {
        if (list == null) {
            return;
        }

        if (getContext() == null) {
            return;
        }

        long timestamp = -1;
        int added = 0;
        if (mTwitterAdapter.getItemList().size() > 0) {
            timestamp = mTwitterAdapter.getItemList().get(0).getTimestamp();
        }

        for (Twitter item :
                list) {

            Pattern r = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
            Matcher m = r.matcher(item.getDate());
            if (m.find()) {
                int yy = Integer.parseInt(m.group(1));
                int MM = Integer.parseInt(m.group(2));
                int DD = Integer.parseInt(m.group(3));
                int HH = Integer.parseInt(m.group(4));
                int mm = Integer.parseInt(m.group(5));
                int ss = Integer.parseInt(m.group(6));
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.getDefault());
                calendar.set(yy, MM - 1, DD, HH, mm, ss);
                item.setTimestamp(calendar.getTimeInMillis());
            }

            if (timestamp != -1 && item.getTimestamp() > timestamp) {
                added++;
            }
        }

        mTwitterAdapter.setItemList(list);
        mTwitterAdapter.notifyDataSetChanged();
        if (animate) {
            mRecyclerView.scrollToPosition(0);

            // 为了有些数量设置为 0 的人..
            if (mTwitterAdapter.getItemCount() > 0) {
                if (added == 0) {
                    Snackbar.make(mSwipeRefreshLayout, R.string.no_new_tweet, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mSwipeRefreshLayout, String.format(getString(R.string.new_twitter), added), Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void refresh(boolean force_cache) {
        mSwipeRefreshLayout.setRefreshing(true);
        refresh(Settings.instance(getContext())
                        .getIntFromString(Settings.TWITTER_COUNT, 30)
                , force_cache);

        Log.d(TAG, "start refresh");
    }

    private void refresh(final int count, boolean force_cache) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(NetworkUtils.getClient(force_cache))
                .baseUrl("https://acc.kcwiki.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.TwitterAPI service = retrofit.create(RetrofitAPI.TwitterAPI.class);
        mCall2 = service.getLatestAvatarUrl();
        mCall2.enqueue(new Callback<LatestAvatar>() {
            @Override
            public void onResponse(Call<LatestAvatar> call, Response<LatestAvatar> response) {
                if (response.code() >= 400) {
                    return;
                }

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

        retrofit.create(RetrofitAPI.TwitterAPI.class)
                .getAvatars()
                .enqueue(new Callback<Avatars>() {
                    @Override
                    public void onResponse(Call<Avatars> call, Response<Avatars> response) {
                        if (response.code() >= 400) {
                            return;
                        }

                        mAvatars = response.body();
                    }

                    @Override
                    public void onFailure(Call<Avatars> call, Throwable t) {

                    }
                });

        RetrofitAPI.TwitterAPI service2 = retrofit.create(RetrofitAPI.TwitterAPI.class);
        mCall = service2.getTweets(count);

        mCall.enqueue(new Callback<List<Twitter>>() {
            @Override
            public void onResponse(Call<List<Twitter>> call, Response<List<Twitter>> response) {
                mSwipeRefreshLayout.setRefreshing(false);

                if (response.code() >= 400) {
                    return;
                }

                updateData(response.body(), true);
            }

            @Override
            public void onFailure(Call<List<Twitter>> call, Throwable t) {
                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);

                Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        switch (action.getKey()) {
            case Settings.TWITTER_COUNT:
                mTwitterAdapter.setMaxItem(
                        Settings
                                .instance(getContext())
                                .getIntFromString(Settings.TWITTER_COUNT, 30));

                mTwitterAdapter.clearItemList();
                mTwitterAdapter.notifyDataSetChanged();

                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        refresh(false);
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
                GridRecyclerViewHelper.init(mRecyclerView);
                break;
        }
    }

    @Override
    public void onMoreButtonClick(Twitter data) {
        TwitterMoreDialogFragment f = new TwitterMoreDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("DATA", data);
        bundle.putString("URL", mTwitterAdapter.getAvatarUrl());
        f.setArguments(bundle);

        f.setTargetFragment(this, 0);
        f.show(getFragmentManager(), TAG);
    }

    @Override
    public void onAvatarLongClick() {
        if (FlavorsUtils.shouldSafeCheck()) {
            return;
        }

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