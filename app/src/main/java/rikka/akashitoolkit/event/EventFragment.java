package rikka.akashitoolkit.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.home.GridRecyclerViewHelper;
import rikka.akashitoolkit.model.Event;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.fragments.BaseDrawerItemFragment;
import rikka.akashitoolkit.ui.widget.ConsumeScrollRecyclerView;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventFragment extends BaseEventFragment<Event> {

    private static final String TAG = "EventFragment";

    private static final int API_VERSION = 2;

    private EventAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.instance().register(this);
        setHasOptionsMenu(true);
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
    public void onFailure(Call<Event> call, Throwable t) {
        Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull Event data) {
        fixRecyclerViewRequestChildFocus();

        mSwipeRefreshLayout.setRefreshing(false);

        mAdapter.clearItemList();

        Gson gson = new Gson();
        for (Event.Container container : data) {
            String json = gson.toJson(container.getObject());
            Object object = null;
            switch (container.getType()) {
                case EventAdapter.TYPE_TITLE:
                case EventAdapter.TYPE_CONTENT:
                    object = gson.fromJson(json, Event.Title.class);
                    break;
                case EventAdapter.TYPE_GALLERY:
                    object = gson.fromJson(json, Event.Gallery.class);

                    Event.Gallery _data = (Event.Gallery) object;

                    List<String> urls = new ArrayList<>();
                    for (String url : _data.getUrls()) {
                        if (!url.startsWith("http")) {
                            urls.add(Utils.getKCWikiFileUrl(url));
                        } else {
                            urls.add(url);
                        }
                    }
                    _data.getUrls().clear();
                    _data.getUrls().addAll(urls);
                    break;
                case EventAdapter.TYPE_MAPS:
                    object = gson.fromJson(json, Event.Maps.class);
                    break;
                case EventAdapter.TYPE_URL:
                    object = gson.fromJson(json, Event.Url.class);
                    break;
                default:
                    Log.w(TAG, "unhandled: " + json);
            }
            mAdapter.addItem(RecyclerView.NO_ID, container.getType(), object);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(Call<Event> call, boolean force_cache) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.kcwiki.moe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.EventAPI service = retrofit.create(RetrofitAPI.EventAPI.class);
        call = service.get(API_VERSION);

        super.onRefresh(call, force_cache);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));

        GridRecyclerViewHelper.init(mRecyclerView);

        mAdapter = new EventAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setClipToPadding(false);

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        if (savedInstanceState == null) {
            onShow();
        }
    }

    @Override
    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        activity.getSupportActionBar().setTitle(getString(R.string.event));

        fixRecyclerViewRequestChildFocus();

        Statistics.onFragmentStart("EventFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("EventFragment");
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        switch (action.getKey()) {
            case Settings.TWITTER_GRID_LAYOUT:
                GridRecyclerViewHelper.init(mRecyclerView);
                break;
        }
    }

    /**
     * "修复" RecyclerView 自己动
     */

    private void fixRecyclerViewRequestChildFocus() {
        if (mRecyclerView != null) {
            mRecyclerView.setDisableRequestChildFocus(true);
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setDisableRequestChildFocus(false);
                }
            }, 500);
        }
    }


}
