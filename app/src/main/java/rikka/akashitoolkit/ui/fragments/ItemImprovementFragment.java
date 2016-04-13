package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.adapter.ItemImprovementAdapter;
import rikka.materialpreference.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/3/17.
 */
public class ItemImprovementFragment extends BaseDisplayFragment {
    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("TYPE");
        }

        setAdapter(new ItemImprovementAdapter(getActivity(), mType));
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));
    }
}