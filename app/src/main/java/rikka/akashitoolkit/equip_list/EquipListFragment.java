package rikka.akashitoolkit.equip_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.ui.fragments.SaveVisibilityFragment;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipListFragment extends SaveVisibilityFragment implements CompoundButton.OnCheckedChangeListener {

    private String mTitle;
    private EquipAdapter mAdapter;

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
        mAdapter = new EquipAdapter(getArguments());
        recyclerView.setAdapter(mAdapter);

        BusProvider.instance().register(this);
    }

    @Override
    public void onShow() {
        super.onShow();

        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportActionBar().setTitle(mTitle);
            //activity.getSwitch().setOnCheckedChangeListener(this);

            mAdapter.setBookmarked(activity.getSwitch().isChecked());
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mAdapter.setBookmarked(isChecked);
        mAdapter.rebuildDataList();
    }
}
