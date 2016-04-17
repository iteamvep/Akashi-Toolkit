package rikka.akashitoolkit.ui.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.QuestAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.QuestAction;
import rikka.akashitoolkit.utils.Utils;
import rikka.materialpreference.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/3/6.
 */
public class QuestFragment extends Fragment {
    private QuestAdapter mAdapter;
    private boolean mIgnoreSearch;
    private RecyclerView mRecyclerView;

    private int mType;

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
        int jump_index = -1;
        int jump_type = -1;
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("TYPE");
            flag = args.getInt("FLAG");
            mIgnoreSearch = args.getInt("IGNORE_SEARCH") == 1;
            jump_index = args.getInt("JUMP_INDEX");
            jump_type = args.getInt("JUMP_TYPE");
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //mRecyclerView.setPadding(0, Utils.dpToPx(2), 0, Utils.dpToPx(2));
        mAdapter = new QuestAdapter(getContext(), mType, flag);
        mAdapter.rebuildDataList(getContext());
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));

        if (jump_index != -1 && jump_type == mType) {
            jumpTo(jump_index);
        }

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

    @Subscribe
    public void jumpToIndex(QuestAction.JumpToQuest action) {
        if (mType == action.getType() || mIgnoreSearch) {
            jumpTo(action.getIndex());
        }
    }

    private void jumpTo(int index) {
        final int position = mAdapter.getPositionByIndex(index);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition(position);
                mAdapter.notifyItemChanged(position);
            }
        });

        BusProvider.instance().post(new QuestAction.JumpedToQuest());
    }
}