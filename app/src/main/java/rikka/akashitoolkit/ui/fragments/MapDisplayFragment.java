package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MapDisplayFragment extends BaseFragmet {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.maps));
        activity.setRightDrawerLocked(true);

        Statistics.onFragmentStart("MapDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("MapDisplayFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private ViewPagerAdapter getAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", position);
                return bundle;
            }
        };

        adapter.addFragment(MapFragment.class, "镇守府海域");
        adapter.addFragment(MapFragment.class, "南西诸岛海域");
        adapter.addFragment(MapFragment.class, "北方海域");
        adapter.addFragment(MapFragment.class, "西方海域");
        adapter.addFragment(MapFragment.class, "南方海域");
        adapter.addFragment(MapFragment.class, "中部海域");

        return adapter;
    }
}
