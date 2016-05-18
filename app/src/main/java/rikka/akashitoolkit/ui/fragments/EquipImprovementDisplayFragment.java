package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerStateAdapter;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementDisplayFragment extends BaseDrawerItemFragment {
    private ViewPager mViewPager;
    private int mDay;
    private boolean mBookmarked;

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("EquipImprovementDisplayFragment");
    }

    @Override
    protected boolean getTabLayoutVisible() {
        return true;
    }

    @Override
    protected boolean getSwitchVisible() {
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.item_improvement));
        mViewPager.setCurrentItem(mDay);

        ((MainActivity) getActivity()).getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBookmarked = buttonView.isChecked();

                BusProvider.instance().post(new BookmarkAction.Changed(mBookmarked));

                Settings
                        .instance(getContext())
                        .putBoolean(Settings.EQUIP_IMPROVEMENT_BOOKMARKED, mBookmarked);
            }
        });

        mBookmarked = Settings
                .instance(getContext())
                .getBoolean(Settings.EQUIP_IMPROVEMENT_BOOKMARKED, false);

        ((MainActivity) getActivity()).getSwitch().setChecked(mBookmarked);

        Statistics.onFragmentStart("EquipImprovementDisplayFragment");
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
                bundle.putInt("TYPE", position);
                bundle.putBoolean("BOOKMARKED", mBookmarked);
                return bundle;
            }
        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));
        mDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] dayNames = symbols.getShortWeekdays();

        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 0, mDay));
        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 1, mDay));
        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 2, mDay));
        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 3, mDay));
        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 4, mDay));
        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 5, mDay));
        adapter.addFragment(EquipImprovementFragment.class, getDayName(dayNames, 6, mDay));


        return adapter;
    }

    private String getDayName(String[] dayNames, int day, int cur) {
        if (day == cur) {
            return dayNames[day + 1] + " " + getString(R.string.today);
        }
        return dayNames[day + 1];
    }
}