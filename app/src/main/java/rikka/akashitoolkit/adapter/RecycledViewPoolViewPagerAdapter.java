package rikka.akashitoolkit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import rikka.akashitoolkit.ui.fragments.IRecycledViewPoolFragment;

/**
 * Created by Rikka on 2016/11/18.
 */

public abstract class RecycledViewPoolViewPagerAdapter extends ViewPagerAdapter {

    private RecyclerView.RecycledViewPool mPool;

    public RecycledViewPoolViewPagerAdapter(FragmentManager fm) {
        super(fm);

        mPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = super.getItem(position);
        if (fragment != null && fragment instanceof IRecycledViewPoolFragment) {
            ((IRecycledViewPoolFragment) fragment).setRecycledViewPool(mPool);
        }
        return super.getItem(position);
    }
}
