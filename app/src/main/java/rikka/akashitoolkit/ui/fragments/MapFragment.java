package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ItemAdapter;
import rikka.akashitoolkit.adapter.MapAdapter;
import rikka.materialpreference.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MapFragment extends BaseDisplayFragment<MapAdapter> {
    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("TYPE") + 1;
        }

        setAdapter(new MapAdapter(getActivity(), mType));
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        super.onPostCreateView(recyclerView);

        recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));
    }
}
