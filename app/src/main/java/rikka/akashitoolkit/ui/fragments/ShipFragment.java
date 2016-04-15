package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ShipAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ShipAction;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipFragment extends Fragment {
    private ShipAdapter mAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recycler, container, false);

        int flag = 0;
        int speed = 0;
        int finalVersion = 0;
        Bundle args = getArguments();
        if (args != null) {
            flag = args.getInt("FLAG");
            finalVersion = args.getInt("FINAL_VERSION");
            speed = args.getInt("SPEED");
        }

        mAdapter = new ShipAdapter(getActivity(), finalVersion > 0, flag, speed);
        mAdapter.rebuildDataList(/*getContext()*/);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setPadding(0, Utils.dpToPx(2), 0, Utils.dpToPx(2));
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));

        return view;
    }

    @Subscribe
    public void showFinalVersionChanged(ShipAction.ShowFinalVersionChangeAction action) {
        mAdapter.setShowOnlyFinalVersion(action.isShowFinalVersion());
        mAdapter.rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void typeChanged(ShipAction.TypeChangeAction action) {
        mAdapter.setTypeFlag(action.getType());
        mAdapter.rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void speedChanged(ShipAction.SpeedChangeAction action) {
        mAdapter.setShowSpeed(action.getType());
        mAdapter.rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void keywordChanged(ShipAction.KeywordChanged action) {
        mAdapter.setKeyword(action.getKeyword());
        mAdapter.rebuildDataList(/*getContext()*/);
    }

    @Subscribe
    public void isSearchingChanged(ShipAction.IsSearchingChanged action) {
        mAdapter.setSearching(action.isSearching());
    }
}
