package rikka.akashitoolkit.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

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
    public void onResume() {
        super.onResume();

        getAdapter().notifyDataSetChanged();
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
        if (args != null) {
            mType = args.getInt("TYPE");
            bookmark = args.getBoolean("BOOKMARKED");
            mPosition = args.getInt("POSITION");
        }

        setAdapter(new EquipAdapter(getActivity(), mType, bookmark));
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

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);

                if (position != 0 && getAdapter().getItemViewType(position) == 1) {
                    outRect.top = Utils.dpToPx(8);
                }
            }
        });
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
