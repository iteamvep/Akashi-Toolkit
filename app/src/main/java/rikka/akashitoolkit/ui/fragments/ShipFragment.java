package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ShipAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ShipAction;
import rikka.akashitoolkit.ui.widget.ItemAnimator;
import rikka.akashitoolkit.ui.widget.LinearLayoutManager;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipFragment extends BaseDisplayFragment<ShipAdapter> {
    public static final String TAG = "ShipFragment";

    private boolean showEnemy;

    @Override
    public void onStart() {
        super.onStart();

        if (!showEnemy) {
            BusProvider.instance().register(this);
        }
    }

    @Override
    public void onStop() {
        if (!showEnemy) {
            BusProvider.instance().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flag = 0;
        int speed = 0;
        int sort = 0;
        int finalVersion = 0;
        showEnemy = false;
        boolean bookmarked = false;
        Bundle args = getArguments();
        if (args != null) {
            flag = args.getInt("FLAG");
            finalVersion = args.getInt("FINAL_VERSION");
            speed = args.getInt("SPEED");
            sort = args.getInt("SORT");
            bookmarked = args.getBoolean("BOOKMARKED");
            showEnemy = args.getBoolean("ENEMY");
        }

        setAdapter(new ShipAdapter(getActivity(), finalVersion, flag, speed, sort, bookmarked, showEnemy));
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
