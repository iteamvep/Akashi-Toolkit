package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/6.
 */
public class DataDisplayFragment extends Fragment {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MainActivity activity = ((MainActivity) getActivity());
        TabLayout tabLayout = activity.getTabLayout();
        tabLayout.setVisibility(TAB_LAYOUT_VISIBILITY);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new DataFragment(), "Test");
        adapter.addFragment(new DataFragment(), "Test2");
        adapter.addFragment(new DataFragment(), "Test3");
        adapter.addFragment(new DataFragment(), "Test4");
        adapter.addFragment(new DataFragment(), "Test5");
        adapter.addFragment(new DataFragment(), "Test6");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}