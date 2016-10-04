package rikka.akashitoolkit.equip_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.fragments.BaseShowHideFragment;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipListFragment extends BaseShowHideFragment {

    private String mTitle;

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
    }

    @Override
    public void onShow() {
        super.onShow();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
        }
    }
}
