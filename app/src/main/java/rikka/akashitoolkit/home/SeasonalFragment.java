package rikka.akashitoolkit.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Seasonal;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/4/30.
 */
public class SeasonalFragment extends BaseRefreshFragment<List<Seasonal>> {
    private static final int API_VERSION = 5;

    private SeasonalAdapter mAdapter;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false));
        mAdapter = new SeasonalAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setClipToPadding(false);

        GridRecyclerViewHelper.init(mRecyclerView);

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    @Override
    public void onSuccess(@NonNull List<Seasonal> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        //setAdapter(data);
        mAdapter.parseData(data);
    }

    @Override
    public void onFailure(Call<List<Seasonal>> call, Throwable t) {
        Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh(Call<List<Seasonal>> call, boolean force_cache) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.kcwiki.moe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SeasonalAPI service = retrofit.create(RetrofitAPI.SeasonalAPI.class);
        call = service.get(API_VERSION);

        super.onRefresh(call, force_cache);
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        switch (action.getKey()) {
            case Settings.TWITTER_GRID_LAYOUT:
                GridRecyclerViewHelper.init(mRecyclerView);
                break;
        }
    }
}
