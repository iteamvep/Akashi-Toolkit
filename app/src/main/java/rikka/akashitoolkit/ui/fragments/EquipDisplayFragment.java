package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipDisplayFragment extends BaseFragment {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.item));
        activity.setRightDrawerLocked(true);

        Statistics.onFragmentStart("EquipDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("EquipDisplayFragment");
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

        for (int i = 0; i < EquipTypeList.getsParentList(getContext()).size(); i++) {
            for (Map.Entry<String, Integer> entry:
                    EquipTypeList.getsParentList(getContext()).entrySet()){
                if (i == entry.getValue()) {
                    adapter.addFragment(EquipFragment.class, entry.getKey());
                }
            }
        }


        return adapter;
    }
}
