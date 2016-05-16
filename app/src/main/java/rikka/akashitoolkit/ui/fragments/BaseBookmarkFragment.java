package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.widget.MyViewPager;

/**
 * Created by Rikka on 2016/5/16.
 */
public abstract class BaseBookmarkFragment extends BaseDrawerItemFragment {
    private MyViewPager mViewPager;

    private boolean mBookmarked;

    protected abstract Fragment getInsideFragment();

    protected abstract String getSettingKey();

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

        ((MainActivity) getActivity()).getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBookmarked = buttonView.isChecked();
                //mViewPager.setCurrentItem(mBookmarked ? 1 : 0);

                BusProvider.instance().post(new BookmarkAction.Changed(mBookmarked));

                setTabLayoutVisible();
            }
        });

        setTabLayoutVisible();

        mBookmarked = Settings
                .instance(getContext())
                .getBoolean(getSettingKey(), false);

        ((MainActivity) getActivity()).getSwitch().setChecked(mBookmarked);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);

        mViewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setSwipeEnabled(false);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return getInsideFragment();
                    case 1:
                        return new BookmarkNoItemFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

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

    @Subscribe
    public void bookmarkNoItem(BookmarkAction.NoItem action) {
        mViewPager.setSwipeEnabled(false);
        mViewPager.setCurrentItem(1);
    }
}
