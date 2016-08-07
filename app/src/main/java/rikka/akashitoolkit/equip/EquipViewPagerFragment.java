package rikka.akashitoolkit.equip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerStateAdapter;
import rikka.akashitoolkit.model.MultiLanguageEntry;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.main.MainActivity;
import rikka.akashitoolkit.ui.widget.MyViewPager;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipViewPagerFragment extends Fragment {
    private MyViewPager mViewPager;

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
        if (!action.getTag().equals(EquipFragment.TAG)) {
            return;
        }

        mViewPager.setSwipeEnabled(!action.isBookmarked());

        BusProvider.instance().post(
                new BookmarkAction.Changed2(action.getTag(), action.isBookmarked(), mViewPager.getCurrentItem()));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void setViewPager() {
        if (mViewPager != null) {
            ((MainActivity) getActivity()).getTabLayout().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) getActivity()).getTabLayout().setupWithViewPager(mViewPager);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());
        mViewPager.setCurrentItem(0, false);

        if (Settings.instance(getActivity())
                .getInt(Settings.LAST_DRAWER_ITEM_ID, 0) == R.id.nav_item)
            setViewPager();

        return view;
    }

    private ViewPagerStateAdapter mViewPagerAdapter;

    private PagerAdapter getAdapter() {
        mViewPagerAdapter = new ViewPagerStateAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", position + 1);
                bundle.putBoolean("BOOKMARKED", false);
                bundle.putInt("POSITION", position);

                return bundle;
            }
        };

        for (MultiLanguageEntry entry : EquipTypeList.getsParentList(getContext())) {
            mViewPagerAdapter.addFragment(EquipFragment.class, entry.get(getActivity()));
        }

        return mViewPagerAdapter;
    }
}
