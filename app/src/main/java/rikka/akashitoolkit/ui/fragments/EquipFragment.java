package rikka.akashitoolkit.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.EquipAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipFragment extends BaseDisplayFragment<EquipAdapter> {
    public static final String TAG = "EquipFragment";

    private int mType;
    private int mPosition;
    private boolean mShowEnemy;

    @Override
    public void onStart() {
        super.onStart();

        if (!mShowEnemy) {
            BusProvider.instance().register(this);
        }
    }

    @Override
    public void onStop() {
        if (!mShowEnemy) {
            BusProvider.instance().unregister(this);
        }

        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mShowEnemy) {
            getAdapter().notifyDataSetChanged();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        boolean bookmark = false;
        mShowEnemy = false;

        if (args != null) {
            mType = args.getInt("TYPE");
            bookmark = args.getBoolean("BOOKMARKED");
            mPosition = args.getInt("POSITION");
            mShowEnemy = args.getBoolean("ENEMY");
        }

        setAdapter(new EquipAdapter(getActivity(), mType, bookmark, mShowEnemy));
    }


    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        super.onPostCreateView(recyclerView);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        /*/*if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }*/

        recyclerView.setItemAnimator(null);
        recyclerView.setBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.cardBackground));

    }

    @Subscribe
    public void onlyBookmarkedChanged(BookmarkAction.Changed2 action) {
        if (!action.getTag().equals(TAG)) {
            return;
        }

        if (action.isBookmarked() && action.getType() == mPosition) {
            getAdapter().setType(0);
            getAdapter().setBookmarked(action.isBookmarked());
            getAdapter().rebuildDataList();
        } else if (!action.isBookmarked()) {
            getAdapter().setType(mType);
            getAdapter().setBookmarked(action.isBookmarked());
            getAdapter().rebuildDataList();
        }
    }
}
