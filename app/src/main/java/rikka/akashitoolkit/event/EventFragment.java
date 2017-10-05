package rikka.akashitoolkit.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.home.GridRecyclerViewHelper;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.widget.RecyclerView;
import rikka.akashitoolkit.utils.NetworkUtils;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventFragment extends BaseEventFragment<Event> {

    private static final String TAG = "EventFragment";

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

        data.parse(new Event.OnItemParseListener() {
            @Override
            public void onItemParse(int type, Object object) {
                mAdapter.addItem(RecyclerView.NO_ID, type, object);
            }
        });

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(Call<Event> call, boolean force_cache) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.kcwiki.moe/")
                .client(NetworkUtils.getClient(force_cache))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.EventAPI service = retrofit.create(RetrofitAPI.EventAPI.class);
        call = service.get(Event.API_VERSION);

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
