package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerStateAdapter;
import rikka.akashitoolkit.support.Statistics;

/**
 * Created by Rikka on 2016/6/13.
 */
public class EnemyDisplayFragment extends BaseDrawerItemFragment {
    private ViewPager mViewPager;

    @Override
    protected boolean getTabLayoutVisible() {
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();

        mActivity.getTabLayout().setupWithViewPager(mViewPager);
        mActivity.getSupportActionBar().setTitle(getString(R.string.enemy));
        mActivity.getTabLayout().setTabMode(TabLayout.MODE_FIXED);
        mActivity.getTabLayout().setPadding(0, 0, 0, 0);

        Statistics.onFragmentStart("EnemyDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("EnemyDisplayFragment");

        mActivity.getTabLayout().setTabMode(TabLayout.MODE_SCROLLABLE);
        int padding = getResources().getDimensionPixelSize(R.dimen.tab_layout_padding);
        mActivity.getTabLayout().setPadding(padding, 0, padding, 0);
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

    private ViewPagerStateAdapter getAdapter() {
        ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("ENEMY", true);
                return bundle;
            }
        };
        adapter.addFragment(ShipFragment.class, getString(R.string.enemy));
        adapter.addFragment(EquipFragment.class, getString(R.string.item));

        return adapter;
    }
}
