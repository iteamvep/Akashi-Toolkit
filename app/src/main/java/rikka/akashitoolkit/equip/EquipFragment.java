package rikka.akashitoolkit.equip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.ShipType;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.ShipTypeList;
import rikka.akashitoolkit.ui.fragments.BaseDisplayFragment;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipFragment extends BaseDisplayFragment<EquipAdapter> {

    public static final String TAG = "EquipFragment";

    public static final String ARG_TYPE = "TYPE";
    public static final String ARG_BOOKMARKED = "BOOKMARKED";
    public static final String ARG_SELECT_MODE = "SELECT_MODE";
    public static final String ARG_SHOW_ENEMY = "ENEMY";
    public static final String ARG_POSITION = "POSITION";
    public static final String ARG_SHIP_TYPE = "SHIP_TYPE";

    private int mType;
    private int mPosition;
    private boolean mShowEnemy;
    private boolean mSelectMode;

    @Override
    public void onStart() {
        super.onStart();

        if (!mShowEnemy && !mSelectMode) {
            BusProvider.instance().register(this);
        }
    }

    @Override
    public void onStop() {
        if (!mShowEnemy && !mSelectMode) {
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
        ShipType shipType = null;

        if (args != null) {
            mType = args.getInt(ARG_TYPE);
            bookmark = args.getBoolean(ARG_BOOKMARKED);
            mPosition = args.getInt(ARG_POSITION);
            mShowEnemy = args.getBoolean(ARG_SHOW_ENEMY);
            mSelectMode = args.getBoolean(ARG_SELECT_MODE);
            int type = args.getInt(ARG_SHIP_TYPE, -1);
            if (type != -1) {
                shipType = ShipTypeList.findItemById(getActivity(), type);
            }
        }

        setAdapter(new EquipAdapter(getActivity(), mType, bookmark, mShowEnemy, mSelectMode, shipType));
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
