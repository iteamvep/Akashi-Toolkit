package rikka.akashitoolkit.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/5/16.
 */
public abstract class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Class> mClasses = new ArrayList<>();
    private final List<String> mTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Class c, String title) {
        mClasses.add(c);
        mTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        try {
            Fragment fragment = (Fragment) mClasses.get(position).newInstance();
            Bundle args = getArgs(position);
            if (args != null) {
                fragment.setArguments(args);
            }
            Log.d("QAQ", Integer.toString(position));
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }

    public abstract Bundle getArgs(int position);
}
