package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
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
import rikka.akashitoolkit.adapter.SeasonalAdapter;
import rikka.akashitoolkit.model.Seasonal;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/4/30.
 */
public class SeasonalFragment extends Fragment {
    private static final int API_VERSION = 5;

    private static final String JSON_NAME = "/json/seasonal_v4.json";
    private String CACHE_FILE;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SeasonalAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        CACHE_FILE = getContext().getCacheDir().getAbsolutePath() + JSON_NAME;
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false));
        mAdapter = new SeasonalAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPadding(0, Utils.dpToPx(4), 0, Utils.dpToPx(4));
        mRecyclerView.setClipToPadding(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        if (savedInstanceState == null) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 500);
        }

        loadFromCache();

        return view;
    }

    private void loadFromCache() {
        List<Seasonal> data;
        try {
            Gson gson = new Gson();
            data = gson.fromJson(
                    new FileReader(CACHE_FILE),
                    new TypeToken<ArrayList<Seasonal>>() {
                    }.getType());

            updateData(data);
        } catch (FileNotFoundException ignored) {
        }
    }

    private void updateData(List<Seasonal> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        //setAdapter(data);
        mAdapter.parseData(data);
    }

    private void refresh() {
        Log.d("SeasonalFragment", "refreshing");

        mSwipeRefreshLayout.setRefreshing(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.minamion.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SeasonalAPI service = retrofit.create(RetrofitAPI.SeasonalAPI.class);
        Call<List<Seasonal>> call = service.get(API_VERSION);

        call.enqueue(new Callback<List<Seasonal>>() {
            @Override
            public void onResponse(Call<List<Seasonal>> call, Response<List<Seasonal>> response) {
                Log.d("SeasonalFragment", "refresh succeeded");

                if (getContext() == null) {
                    return;
                }

                updateData(response.body());

                Gson gson = new Gson();
                Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                        CACHE_FILE);
            }

            @Override
            public void onFailure(Call<List<Seasonal>> call, Throwable t) {
                Log.d("SeasonalFragment", "refresh failed");
                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);
                Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
