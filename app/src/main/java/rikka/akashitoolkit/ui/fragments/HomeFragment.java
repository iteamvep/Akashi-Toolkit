package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;

/**
 * Created by Rikka on 2016/6/11.
 */
public class HomeFragment extends BaseDrawerItemFragment {
    private ViewPager mViewPager;

    @Override
    protected boolean getTabLayoutVisible() {
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();

        mActivity.getTabLayout().setupWithViewPager(mViewPager);
        mActivity.getTabLayout().setTabMode(TabLayout.MODE_FIXED);
        mActivity.getTabLayout().setPadding(0, 0, 0, 0);

        if (mActivity.getTabLayout().getTabAt(0).getIcon() == null) {
            mActivity.getTabLayout().getTabAt(0).setIcon(R.drawable.ic_nav_twitter_24dp);
            mActivity.getTabLayout().getTabAt(1).setIcon(R.drawable.ic_explore_black_24dp);
            mActivity.getTabLayout().getTabAt(2).setIcon(R.drawable.ic_nav_new_24dp);
            DrawableCompat.setTintList(mActivity.getTabLayout().getTabAt(0).getIcon(), mActivity.getTabLayout().getTabTextColors());
            DrawableCompat.setTintList(mActivity.getTabLayout().getTabAt(1).getIcon(), mActivity.getTabLayout().getTabTextColors());
            DrawableCompat.setTintList(mActivity.getTabLayout().getTabAt(2).getIcon(), mActivity.getTabLayout().getTabTextColors());
        }

        mActivity.getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onHide() {
        super.onHide();
        mActivity.getTabLayout().setTabMode(TabLayout.MODE_SCROLLABLE);
        int padding = getResources().getDimensionPixelSize(R.dimen.tab_layout_padding);
        mActivity.getTabLayout().setPadding(padding, 0, padding, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.twitter, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(1);

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private ViewPagerAdapter getAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                return null;
            }
        };
        adapter.addFragment(TwitterFragment.class, "");
        adapter.addFragment(MessageFragment.class, "");
        adapter.addFragment(SeasonalFragment.class, "");

        return adapter;
    }
}
