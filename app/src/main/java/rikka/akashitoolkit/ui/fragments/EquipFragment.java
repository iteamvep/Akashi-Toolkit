package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.adapter.EquipAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipFragment extends BaseDisplayFragment<EquipAdapter> {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        boolean bookmark = false;
        if (args != null) {
            mType = args.getInt("TYPE");
            bookmark = args.getBoolean("BOOKMARKED");
        }

        setAdapter(new EquipAdapter(getActivity(), mType, bookmark));
    }

    @Subscribe
    public void onlyBookmarkedChanged(BookmarkAction.Changed action) {
        if (action.isBookmarked()) {
            getAdapter().setType(0);
        } else {
            getAdapter().setType(mType);
        }
        getAdapter().setBookmarked(action.isBookmarked());
        getAdapter().rebuildDataList();
    }
}
