package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.adapter.QuestAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.QuestAction;
import rikka.materialpreference.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/3/6.
 */
public class QuestFragment extends BaseDisplayFragment<QuestAdapter> {
    private boolean mSearching;

    private int mType;
    private int mJumpIndex;
    private int mJumpType;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int flag = 0;
        boolean latest = false;
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("TYPE");
            flag = args.getInt("FLAG");
            mSearching = args.getBoolean("SEARCHING");
            mJumpIndex = args.getInt("JUMP_INDEX");
            mJumpType = args.getInt("JUMP_TYPE");
            latest = args.getBoolean("LATEST_ONLY");
        }

        setAdapter(new QuestAdapter(getContext(), mType, flag, mSearching, latest));
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        super.onPostCreateView(recyclerView);

        recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));

        if (mJumpIndex != -1 && mJumpType == mType) {
            jumpTo(mJumpIndex);
        }
    }

    @Subscribe
    public void questFilterChanged(QuestAction.FilterChanged action) {
        getAdapter().setFilterFlag(action.getFlag());
    }

    @Subscribe
    public void questFilterChanged(QuestAction.KeywordChanged action) {
        if (getAdapter().isSearching()) {
            getAdapter().setKeyword(action.getKeyword());
        }
    }

    @Subscribe
    public void isSearchingChanged(QuestAction.IsSearchingChanged action) {
        getAdapter().setSearching(action.isSearching());
    }

    @Subscribe
    public void jumpToIndex(QuestAction.JumpToQuest action) {
        if (mType == action.getType() || mSearching) {
            jumpTo(action.getIndex());
        }
    }

    private void jumpTo(final int index) {
        getRecyclerView().post(new Runnable() {
            @Override
            public void run() {
                final int position = getAdapter().getPositionByIndex(index);
                Log.d("QuestFragment", String.format("jump to %d", position));

                ((LinearLayoutManager) getRecyclerView().getLayoutManager())
                        .scrollToPositionWithOffset(position, 0);
                //getRecyclerView().smoothScrollToPosition(position);
                getAdapter().notifyItemChanged(position);
            }
        });

        BusProvider.instance().post(new QuestAction.JumpedToQuest());
    }
}