package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.squareup.otto.Subscribe;

import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.model.BaseDataModel;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.widget.MyViewPager;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipDisplayFragment extends BaseFragment {
    private MyViewPager mViewPager;

    private boolean mBookmarked;
    private int mLastItem;

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.instance().register(this);
    }

    @Override
    public void onStop() {
        BusProvider.instance().unregister(this);
        super.onStop();
    }

    @Override
    protected boolean getTabLayoutVisible() {
        return !mBookmarked;
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
        activity.getSupportActionBar().setTitle(getString(R.string.item));

        ((MainActivity) getActivity()).getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mBookmarked) {
                    mLastItem = mViewPager.getCurrentItem();
                }

                int checked = buttonView.isChecked() ? 1 : 0;

                BusProvider.instance().post(new BookmarkAction.Changed(checked > 0));

                mBookmarked = checked > 0;
                Settings
                        .instance(getContext())
                        .putBoolean(Settings.EQUIP_BOOKMARKED, checked > 0);

                /*if (mBookmarked) {
                    int count = 0;
                    for (BaseDataModel d :
                            EquipList.get(getActivity())) {
                        if (d.isBookmarked()) {
                            count++;
                        }
                    }

                    if (count == 0) {
                        mViewPager.setCurrentItem(mViewPagerAdapter.getCount(), false);
                    } else {
                        mViewPager.setCurrentItem(mViewPagerAdapter.getCount() - 1, false);
                        BusProvider.instance().post(new BookmarkAction.Changed(checked > 0));
                    }
                }*/


                mViewPagerAdapter.notifyDataSetChanged();
                /*mViewPager.setAdapter(mViewPagerAdapter);
                MainActivity activity = ((MainActivity) getActivity());
                activity.getTabLayout().setupWithViewPager(mViewPager);*/

                if (!mBookmarked) {
                    //mViewPager.setSwipeEnabled(true);
                    mViewPager.setCurrentItem(mLastItem, false);
                    /*mViewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setSwipeEnabled(true);
                        }
                    }, 500);*/
                }

                setTabLayoutVisible();
            }
        });

        mBookmarked = Settings
                .instance(getContext())
                .getBoolean(Settings.EQUIP_BOOKMARKED, false);

        ((MainActivity) getActivity()).getSwitch().setChecked(mBookmarked);

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
        mViewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());
        mViewPager.setCurrentItem(0, false);

        // so bad
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setOffscreenPageLimit(99);
            }
        }, 1000);

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private ViewPagerAdapter mViewPagerAdapter;

    private ViewPagerAdapter getAdapter() {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                if (position == EquipTypeList.getsParentList(getContext()).size() - 1) {
                    bundle.putInt("TYPE", 0);
                    bundle.putBoolean("BOOKMARKED", true);
                } else {
                    bundle.putInt("TYPE", position + 1);
                    bundle.putBoolean("BOOKMARKED", false);
                }

                return bundle;
            }

            @Override
            public int getCount() {
                if (mBookmarked) {
                    return super.getCount();
                }
                return super.getCount() - 1;
            }
        };

        for (int i = 0; i < EquipTypeList.getsParentList(getContext()).size(); i++) {
            for (Map.Entry<String, Integer> entry:
                    EquipTypeList.getsParentList(getContext()).entrySet()){
                if (i == entry.getValue()) {
                    mViewPagerAdapter.addFragment(EquipFragment.class, entry.getKey());
                }
            }
        }

        mViewPagerAdapter.addFragment(BookmarkNoItemFragment.class, "No item");

        return mViewPagerAdapter;
    }

    @Subscribe
    public void bookmarkNoItem(BookmarkAction.NoItem action) {
        mViewPager.setCurrentItem(mViewPagerAdapter.getCount() - 1, false);
    }
}
