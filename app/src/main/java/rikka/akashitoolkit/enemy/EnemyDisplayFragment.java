package rikka.akashitoolkit.enemy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerStateAdapter;
import rikka.akashitoolkit.equip_list.EquipListFragment;
import rikka.akashitoolkit.ship.ShipFragment;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.fragments.DrawerFragment;

/**
 * Created by Rikka on 2016/6/13.
 */
public class EnemyDisplayFragment extends DrawerFragment {
    private ViewPager mViewPager;

    @Override
    protected boolean onSetTabLayout(TabLayout tabLayout) {
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
        return inflater.inflate(R.layout.content_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());
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
        adapter.addFragment(EquipListFragment.class, getString(R.string.equip));

        return adapter;
    }
}
