package rikka.akashitoolkit.equip;

import android.support.v4.app.Fragment;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.ui.fragments.BaseBookmarkFragment;

/**
 * Created by Rikka on 2016/5/16.
 */
public class EquipDisplayFragment extends BaseBookmarkFragment {
    @Override
    public void onShow() {
        super.onShow();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.item));

        // so bad way
        if (getChildFragmentManager().getFragments() != null) {
            for (Fragment f :
                    getChildFragmentManager().getFragments()) {
                if (f instanceof EquipViewPagerFragment) {
                    ((EquipViewPagerFragment) f).setViewPager();
                }
            }
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
        return new EquipViewPagerFragment();
    }

    @Override
    protected String getSettingKey() {
        return Settings.EQUIP_BOOKMARKED;
    }

    @Override
    protected String getFragmentTAG() {
        return EquipFragment.TAG;
    }
}
