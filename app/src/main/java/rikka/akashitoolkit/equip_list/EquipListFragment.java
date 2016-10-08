package rikka.akashitoolkit.equip_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.ui.fragments.BaseShowHideFragment;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipListFragment extends BaseShowHideFragment {

    private String mTitle;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle = getArguments().getString("TITLE");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EquipAdapter(getArguments()));

        BusProvider.instance().register(this);

        mAdapter = recyclerView.getAdapter();
    }

    @Override
    public void onShow() {
        super.onShow();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
        }
    }

    @Override
    public void onDestroyView() {
        BusProvider.instance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void dataChanged(DataChangedAction action) {
        if (action.getClassName().equals("any")
                || action.getClassName().equals(this.getClass().getSimpleName())) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
