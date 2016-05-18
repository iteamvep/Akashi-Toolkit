package rikka.akashitoolkit.ui.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.adapter.EquipImprovementAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.materialpreference.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementFragment extends BaseDisplayFragment<EquipImprovementAdapter> {
    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        boolean bookmarked = false;
        if (args != null) {
            mType = args.getInt("TYPE");
            bookmarked = args.getBoolean("BOOKMARKED");
        }

        setAdapter(new EquipImprovementAdapter(getActivity(), mType, bookmarked));
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        super.onPostCreateView(recyclerView);

        recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));
        recyclerView.setItemAnimator(null);
    }

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

    @Subscribe
    public void onlyBookmarkedChanged(BookmarkAction.Changed action) {
        getAdapter().setBookmarked(action.isBookmarked());
        getAdapter().rebuildDataList();
    }
}