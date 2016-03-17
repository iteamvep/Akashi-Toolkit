package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/6.
 */
public class QuestDisplayFragment extends BaseFragmet {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;

    @Override
    public void onHide() {
        super.onHide();

        AVAnalytics.onFragmentEnd("QuestDisplayFragment");
    }

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.quest));

        AVAnalytics.onFragmentStart("QuestDisplayFragment");
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
        adapter.addFragment(QuestFragment.class, genrateArgs(0), "編成");
        adapter.addFragment(QuestFragment.class, genrateArgs(1), "出擊");
        adapter.addFragment(QuestFragment.class, genrateArgs(2), "演習");
        adapter.addFragment(QuestFragment.class, genrateArgs(3), "遠征");
        adapter.addFragment(QuestFragment.class, genrateArgs(4), "補給/入渠");
        adapter.addFragment(QuestFragment.class, genrateArgs(5), "工廠");
        adapter.addFragment(QuestFragment.class, genrateArgs(6), "改裝");

        return adapter;
    }

    private Bundle genrateArgs(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        return bundle;
    }
}