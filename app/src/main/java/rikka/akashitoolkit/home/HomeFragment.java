package rikka.akashitoolkit.home;

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
import rikka.akashitoolkit.ui.fragments.DrawerFragment;

/**
 * Created by Rikka on 2016/6/11.
 */
public class HomeFragment extends DrawerFragment {
    private ViewPager mViewPager;

    @Override
    protected boolean onSetTabLayout(TabLayout tabLayout) {
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setPadding(0, 0, 0, 0);

        if (tabLayout.getTabAt(0).getIcon() == null) {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_nav_twitter_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_explore_black_24dp);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_nav_new_24dp);
            DrawableCompat.setTintList(tabLayout.getTabAt(0).getIcon(), tabLayout.getTabTextColors());
            DrawableCompat.setTintList(tabLayout.getTabAt(1).getIcon(), tabLayout.getTabTextColors());
            DrawableCompat.setTintList(tabLayout.getTabAt(2).getIcon(), tabLayout.getTabTextColors());
        }
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();

        setToolbarTitle(getString(R.string.app_name));
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
        return inflater.inflate(R.layout.content_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(1);
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
