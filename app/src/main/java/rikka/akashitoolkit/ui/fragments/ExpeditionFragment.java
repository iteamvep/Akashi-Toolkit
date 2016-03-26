package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ExpeditionAdapter;
import rikka.akashitoolkit.adapter.QuestAdapter;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recycler, container, false);

        int type = 0;
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt("TYPE") + 1;
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setPadding(0, Utils.dpToPx(2), 0, Utils.dpToPx(2));
        recyclerView.setAdapter(new ExpeditionAdapter(getContext(), type));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
