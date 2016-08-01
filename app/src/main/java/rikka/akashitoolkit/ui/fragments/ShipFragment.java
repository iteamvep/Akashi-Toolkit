package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ShipAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ShipAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.widget.ItemAnimator;
import rikka.akashitoolkit.ui.widget.LinearLayoutManager;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipFragment extends BaseDisplayFragment<ShipAdapter> {

    public static final String TAG = "ShipFragment";

    public static final String ARG_TYPE_FLAG = "FLAG";
    public static final String ARG_FINAL_VERSION = "FINAL_VERSION";
    public static final String ARG_SPEED = "SPEED";
    public static final String ARG_SORT = "SORT";
    public static final String ARG_BOOKMARKED = "BOOKMARKED";
    public static final String ARG_SHOW_ENEMY = "ENEMY";
    public static final String ARG_SELECT_MODE = "SELECT_MODE";

    private boolean mShowEnemy;
    private boolean mSelectMode;
    protected Object mBusEventListener;

    @Override
    public void onStart() {
        super.onStart();

        if (!mShowEnemy && !mSelectMode) {
            BusProvider.instance().register(this);
        }
    }

    @Override
    public void onStop() {
        if (!mShowEnemy && !mSelectMode) {
            BusProvider.instance().unregister(this);
        }

        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BusProvider.instance().register(mBusEventListener);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.instance().unregister(mBusEventListener);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mShowEnemy) {
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flag = 0;
        int speed = 0;
        int sort = 0;
        int finalVersion = 0;
        boolean bookmarked = false;
        Bundle args = getArguments();
        if (args != null) {
            flag = args.getInt(ARG_TYPE_FLAG);
            finalVersion = args.getInt(ARG_FINAL_VERSION);
            speed = args.getInt(ARG_SPEED);
            sort = args.getInt(ARG_SORT);
            bookmarked = args.getBoolean(ARG_BOOKMARKED);
            mShowEnemy = args.getBoolean(ARG_SHOW_ENEMY);
            mSelectMode = args.getBoolean(ARG_SELECT_MODE);
        }

        mBusEventListener = new Object() {
            @Subscribe
            public void dataChanged(PreferenceChangedAction event) {
                ShipFragment.this.preferenceChanged(event);
            }
        };

        setAdapter(new ShipAdapter(getActivity(), finalVersion, flag, speed, sort, bookmarked, mShowEnemy, mSelectMode));
    }

    @Override
    public boolean onBackPressed() {
        return getAdapter().collapseLastType();
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new ItemAnimator());
        recyclerView.setBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.cardBackground));
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        switch (action.getKey()) {
            case Settings.SHOW_SHIP_BANNER:
                getAdapter().notifyDataSetChanged();
                break;
        }
    }

    @Subscribe
    public void showFinalVersionChanged(ShipAction.ShowFinalVersionChangeAction action) {
        getAdapter().setShowVersion(action.isShowFinalVersion());
        getAdapter().rebuildDataList();
    }

    @Subscribe
    public void typeChanged(ShipAction.TypeChangeAction action) {
        getAdapter().setTypeFlag(action.getType());
        getAdapter().rebuildDataList();
    }

    @Subscribe
    public void speedChanged(ShipAction.SpeedChangeAction action) {
        getAdapter().setShowSpeed(action.getType());
        getAdapter().rebuildDataList();
    }

    @Subscribe
    public void keywordChanged(ShipAction.KeywordChanged action) {
        getAdapter().setKeyword(action.getKeyword());
        getAdapter().rebuildDataList();
    }

    @Subscribe
    public void isSearchingChanged(ShipAction.IsSearchingChanged action) {
        getAdapter().setSearching(action.isSearching());
    }

    @Subscribe
    public void sortChanged(ShipAction.SortChangeAction action) {
        getAdapter().setSort(action.getSort());
        getAdapter().rebuildDataList();
    }

    @Subscribe
    public void onlyBookmarkedChanged(BookmarkAction.Changed action) {
        if (!action.getTag().equals(TAG)) {
            return;
        }

        getAdapter().setBookmarked(action.isBookmarked());
        getAdapter().rebuildDataList();
    }
}
