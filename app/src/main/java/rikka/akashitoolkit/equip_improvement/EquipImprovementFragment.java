package rikka.akashitoolkit.equip_improvement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.equip_improvement.EquipImprovementAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.ui.fragments.BaseDisplayFragment;
import rikka.akashitoolkit.ui.widget.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementFragment extends BaseDisplayFragment<EquipImprovementAdapter> {
    public static final String TAG = "EquipImprovementFragment";

    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        boolean bookmarked = false;
        if (args != null) {
            mType = args.getInt("TYPE");
            bookmarked = args.getBoolean("BOOKMARKED");
        }

        setAdapter(new EquipImprovementAdapter(getActivity(), mType, bookmarked));
    }

    @Override
    public void onPostCreateView(RecyclerView recyclerView) {
        super.onPostCreateView(recyclerView);

        recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()));
        recyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.cardBackground));
        recyclerView.setItemAnimator(null);
    }

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

    @Subscribe
    public void onlyBookmarkedChanged(BookmarkAction.Changed action) {
        if (!action.getTag().equals(TAG)) {
            return;
        }

        getAdapter().setBookmarked(action.isBookmarked());
        getAdapter().rebuildDataList();
    }

    @Override
    public void onResume() {
        super.onResume();

        getAdapter().notifyDataSetChanged();
    }
}