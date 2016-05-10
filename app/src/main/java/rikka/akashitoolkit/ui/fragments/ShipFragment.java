package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.adapter.ShipAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ShipAction;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipFragment extends BaseDisplayFragment<ShipAdapter> {
    @Override
    public void onStart() {
        super.onStart();
        BusProvider.instance().register(this);
    }

    @Override
    public void onStop() {
        BusProvider.instance().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flag = 0;
        int speed = 0;
        int sort = 0;
        int finalVersion = 0;
        Bundle args = getArguments();
        if (args != null) {
            flag = args.getInt("FLAG");
            finalVersion = args.getInt("FINAL_VERSION");
            speed = args.getInt("SPEED");
            sort = args.getInt("SORT");
        }

        setAdapter(new ShipAdapter(getActivity(), finalVersion, flag, speed, sort));
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        super.onPostCreateView(recyclerView);

        recyclerView.setPadding(0, Utils.dpToPx(2), 0, Utils.dpToPx(2));
        recyclerView.setClipToPadding(false);
    }

    @Subscribe
    public void showFinalVersionChanged(ShipAction.ShowFinalVersionChangeAction action) {
        getAdapter().setShowVersion(action.isShowFinalVersion());
        getAdapter().rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void typeChanged(ShipAction.TypeChangeAction action) {
        getAdapter().setTypeFlag(action.getType());
        getAdapter().rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void speedChanged(ShipAction.SpeedChangeAction action) {
        getAdapter().setShowSpeed(action.getType());
        getAdapter().rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void keywordChanged(ShipAction.KeywordChanged action) {
        getAdapter().setKeyword(action.getKeyword());
        getAdapter().rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void isSearchingChanged(ShipAction.IsSearchingChanged action) {
        getAdapter().setSearching(action.isSearching());
    }

    @Subscribe
    public void sortChanged(ShipAction.SortChangeAction action) {
        getAdapter().setSort(action.getSort());
        getAdapter().rebuildDataList(/*getContext()*/);
    }
}
