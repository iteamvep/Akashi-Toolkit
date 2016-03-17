package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/17.
 */
public class ItemImprovementDisplayFragment  extends BaseFragmet {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;
    private int mDay;

    @Override
    public void onHide() {
        super.onHide();

        AVAnalytics.onFragmentEnd("ItemImprovementDisplayFragment");
    }

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.item_improvement));
        mViewPager.setCurrentItem(mDay);

        AVAnalytics.onFragmentStart("ItemImprovementDisplayFragment");
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));
        mDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] dayNames = symbols.getShortWeekdays();

        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(0), getDayName(dayNames, 0, mDay));
        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(1), getDayName(dayNames, 1, mDay));
        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(2), getDayName(dayNames, 2, mDay));
        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(3), getDayName(dayNames, 3, mDay));
        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(4), getDayName(dayNames, 4, mDay));
        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(5), getDayName(dayNames, 5, mDay));
        adapter.addFragment(ItemImprovementFragment.class, genrateArgs(6), getDayName(dayNames, 6, mDay));


        return adapter;
    }

    private String getDayName(String[] dayNames, int day, int cur) {
        if (day == cur) {
            return dayNames[day + 1] + " (今日)";
        }
        return dayNames[day + 1];
    }

    private Bundle genrateArgs(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        return bundle;
    }
}