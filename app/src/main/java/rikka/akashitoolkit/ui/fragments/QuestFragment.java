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
public class QuestFragment extends BaseDisplayFragment<QuestAdapter> {
    private boolean mIgnoreSearch;

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
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("TYPE");
            flag = args.getInt("FLAG");
            mIgnoreSearch = args.getInt("IGNORE_SEARCH") == 1;
            mJumpIndex = args.getInt("JUMP_INDEX");
            mJumpType = args.getInt("JUMP_TYPE");
        }

        setAdapter(new QuestAdapter(getContext(), mType, flag));
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
        getAdapter().setFilterFlag(getContext(), action.getFlag());
    }

    @Subscribe
    public void questFilterChanged(QuestAction.KeywordChanged action) {
        if (mIgnoreSearch) {
            return;
        }
        getAdapter().setKeyword(getContext(), action.getKeyword());
    }

    @Subscribe
    public void jumpToIndex(QuestAction.JumpToQuest action) {
        if (mType == action.getType() || mIgnoreSearch) {
            jumpTo(action.getIndex());
        }
    }

    private void jumpTo(int index) {
        final int position = getAdapter().getPositionByIndex(index);
        getRecyclerView().post(new Runnable() {
            @Override
            public void run() {
                getRecyclerView().scrollToPosition(position);
                getAdapter().notifyItemChanged(position);
            }
        });

        BusProvider.instance().post(new QuestAction.JumpedToQuest());
    }
}