package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerStateAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.widget.MyViewPager;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());
        mViewPager.setCurrentItem(0, false);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).getTabLayout().setupWithViewPager(mViewPager);
            }
        });

        return view;
    }

    private ViewPagerStateAdapter mViewPagerAdapter;

    private PagerAdapter getAdapter() {
        mViewPagerAdapter = new ViewPagerStateAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                if (position == EquipTypeList.getsParentList(getContext()).size() - 1) {
                    bundle.putInt("TYPE", 0);
                    bundle.putBoolean("BOOKMARKED", true);
                } else {
                    bundle.putInt("TYPE", position + 1);
                    bundle.putBoolean("BOOKMARKED", false);
                }
                bundle.putInt("POSITION", position);

                return bundle;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        for (int i = 0; i < EquipTypeList.getsParentList(getContext()).size(); i++) {
            for (Map.Entry<String, Integer> entry:
                    EquipTypeList.getsParentList(getContext()).entrySet()){
                if (i == entry.getValue()) {
                    mViewPagerAdapter.addFragment(EquipFragment.class, entry.getKey());
                }
            }
        }

        return mViewPagerAdapter;
    }

    public MyViewPager getViewPager() {
        return mViewPager;
    }
}
