package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionDisplayFragment extends BaseFragmet {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.expedition));

        AVAnalytics.onFragmentStart("ExpeditionDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        AVAnalytics.onFragmentEnd("ExpeditionDisplayFragment");
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ExpeditionFragment.class, genrateArgs(0), "镇守府海域");
        adapter.addFragment(ExpeditionFragment.class, genrateArgs(1), "南西诸岛海域");
        adapter.addFragment(ExpeditionFragment.class, genrateArgs(2), "北方海域");
        adapter.addFragment(ExpeditionFragment.class, genrateArgs(3), "西方海域");
        adapter.addFragment(ExpeditionFragment.class, genrateArgs(4), "南方海域");

        return adapter;
    }

    private Bundle genrateArgs(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        return bundle;
    }
}
