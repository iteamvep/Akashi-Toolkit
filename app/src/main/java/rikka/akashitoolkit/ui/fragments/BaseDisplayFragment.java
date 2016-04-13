package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;

/**
 * Created by Rikka on 2016/4/13.
 */
public abstract class BaseDisplayFragment extends Fragment {
    protected Object mBusEventListener;
    private BaseRecyclerAdapter mAdapter;

    @Override
    public void onStart() {
        super.onStart();
        mBusEventListener = new Object() {
            @Subscribe
            public void dataChanged(final DataChangedAction event) {
                BaseDisplayFragment.this.dataChanged(event);
            }
        };

        BusProvider.instance().register(mBusEventListener);
    }

    @Override
    public void onStop() {
        BusProvider.instance().unregister(mBusEventListener);
        super.onStop();
    }

    public void setAdapter(BaseRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        onPostCreateView(recyclerView);

        return view;
    }

    public void onPostCreateView(RecyclerView recyclerView) {

    }

    @Subscribe
    public void dataChanged(DataChangedAction action) {
        if (action.getClassName().equals(this.getClass().getSimpleName())) {
            mAdapter.rebuildDataList();
        }
    }
}
