package rikka.akashitoolkit.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.event.Event;
import rikka.akashitoolkit.event.EventAdapter;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/4/30.
 */
public class SeasonalFragment extends BaseRefreshFragment<Event> {

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false));
        mAdapter = new EventAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setClipToPadding(false);

        GridRecyclerViewHelper.init(mRecyclerView);

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    @Override
    public void onSuccess(@NonNull Event data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clearItemList();

        data.parse(new Event.OnItemParseListener() {
            @Override
            public void onItemParse(int type, Object object) {
                mAdapter.addItem(RecyclerView.NO_ID, type, object);
            }
        });

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<Event> call, Throwable t) {
        Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh(Call<Event> call, boolean force_cache) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.kcwiki.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SeasonalAPI service = retrofit.create(RetrofitAPI.SeasonalAPI.class);
        call = service.get(Event.API_VERSION);

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
