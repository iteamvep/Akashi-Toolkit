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
import rikka.akashitoolkit.adapter.QuestAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.QuestAction;

/**
 * Created by Rikka on 2016/3/6.
 */
public class QuestFragment extends Fragment {
    private QuestAdapter mAdapter;
    private boolean mIgnoreSearch;

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

        int type = 0;
        int flag = 0;
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt("TYPE");
            flag = args.getInt("FLAG");
            mIgnoreSearch = args.getInt("IGNORE_SEARCH") == 1;
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new QuestAdapter(getContext(), type, flag);
        mAdapter.rebuildDataList(getContext());
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public QuestAdapter getAdapter() {
        return mAdapter;
    }

    @Subscribe
    public void questFilterChanged(QuestAction.FilterChanged action) {
        mAdapter.setFilterFlag(getContext(), action.getFlag());
    }

    @Subscribe
    public void questFilterChanged(QuestAction.KeywordChanged action) {
        if (mIgnoreSearch) {
            return;
        }
        mAdapter.setKeyword(getContext(), action.getKeyword());
    }
}