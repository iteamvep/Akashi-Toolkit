package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/6.
 */
public class DataDisplayFragment extends BaseFragmet {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getTabLayout().setupWithViewPager(mViewPager);
        //activity.getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new DataFragment(), "編成");
        adapter.addFragment(new DataFragment(), "出擊");
        adapter.addFragment(new DataFragment(), "演習");
        adapter.addFragment(new DataFragment(), "遠征");
        adapter.addFragment(new DataFragment(), "補給/入渠");
        adapter.addFragment(new DataFragment(), "工廠");
        adapter.addFragment(new DataFragment(), "改裝");
        viewPager.setAdapter(adapter);
    }
}