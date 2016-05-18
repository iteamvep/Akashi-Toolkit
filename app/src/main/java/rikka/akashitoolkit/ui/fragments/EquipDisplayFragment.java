package rikka.akashitoolkit.ui.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/5/16.
 */
public class EquipDisplayFragment extends BaseBookmarkFragment {
    private EquipViewPagerFragment mEquipViewPagerFragment;

    @Override
    public void onShow() {
        super.onShow();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.item));

        if (mEquipViewPagerFragment != null) {
            ((MainActivity) getActivity()).getTabLayout().post(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) getActivity()).getTabLayout().setupWithViewPager(mEquipViewPagerFragment.getViewPager());
                }
            });
        }

        Statistics.onFragmentStart("EquipDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("EquipDisplayFragment");
    }

    @Override
    protected Fragment getInsideFragment() {
        if (mEquipViewPagerFragment == null) {
            mEquipViewPagerFragment = new EquipViewPagerFragment();
        }

        return mEquipViewPagerFragment;
    }

    @Override
    protected String getSettingKey() {
        return Settings.EQUIP_BOOKMARKED;
    }
}
