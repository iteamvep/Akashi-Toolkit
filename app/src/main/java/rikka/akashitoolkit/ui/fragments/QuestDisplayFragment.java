package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.QuestAction;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.widget.CheckBoxGroup;

/**
 * Created by Rikka on 2016/3/6.
 */
public class QuestDisplayFragment extends BaseFragmet implements CheckBoxGroup.OnCheckedChangeListener {
    private static final int TAB_LAYOUT_VISIBILITY = View.VISIBLE;

    private ViewPager mViewPager;
    private SearchView mSearchView;
    private ViewPagerAdapter[] mViewPagerAdapter;
    private MainActivity mActivity;
    private int mType;
    private int mFlag = -1;

    @Override
    public void onHide() {
        super.onHide();

        AVAnalytics.onFragmentEnd("QuestDisplayFragment");
    }

    @Override
    public void onShow() {
        mFlag = 1 + 2 + 4 + 8;

        setUpViewPager(0);

        mActivity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        mActivity.getTabLayout().setupWithViewPager(mViewPager);
        mActivity.getSupportActionBar().setTitle(getString(R.string.quest));
        mActivity.setRightDrawerLocked(false);

        mActivity.getRightDrawerContent().removeAllViews();
        mActivity.getRightDrawerContent().addTitle(getString(R.string.action_filter));
        mActivity.getRightDrawerContent().addDivider();
        CheckBoxGroup cbg = new CheckBoxGroup(getContext());
        cbg.addItem("単発任務");
        cbg.addItem("日次任務");
        cbg.addItem("週間任務");
        cbg.addItem("月間任務");
        cbg.setOnCheckedChangeListener(this);
        cbg.setChecked(mFlag);
        mActivity.getRightDrawerContent().addView(cbg);

        AVAnalytics.onFragmentStart("QuestDisplayFragment");
    }

    @Override
    public void onCheckedChanged(View view, int checked) {
        if (mType == 0) {
            mFlag = checked;

            /*for (int i = 0; i < mViewPagerAdapter[0].getCount(); i++) {
                QuestFragment fragment = (QuestFragment) mViewPagerAdapter[0].getItem(i, false);
                if (fragment != null) {
                    fragment.getAdapter().setFilterFlag(getContext(), checked);
                }
            }*/
            BusProvider.instance().post(new QuestAction.FilterChanged(checked));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quest, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        setUpViewPager(1);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        setUpViewPager(0);
                        return true;
                    }
                });

        mSearchView = (SearchView) item.getActionView();
        //mSearchView.setMaxWidth(9999);
        //mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BusProvider.instance().post(new QuestAction.KeywordChanged(newText));
                return false;
            }
        });

        /*if (serachString != null) {
            item.expandActionView();
            mSearchView.setQuery(serachString, false);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                mActivity.getDrawerLayout().openDrawer(GravityCompat.END);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter[2];
        mViewPagerAdapter[0] = getAdapter(0);
        mViewPagerAdapter[1] = getAdapter(1);

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private void setUpViewPager(int type) {
        mViewPager.setAdapter(mViewPagerAdapter[type]);
        mViewPager.setCurrentItem(type);
        mActivity.getTabLayout().setupWithViewPager(mViewPager);
        mType = type;
    }

    private ViewPagerAdapter getAdapter(int type) {
        ViewPagerAdapter adapter;

        if (type == 0) {
            adapter = new ViewPagerAdapter(getChildFragmentManager()) {
                @Override
                public Bundle getArgs(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("TYPE", position + 1);
                    bundle.putInt("FLAG", mFlag);
                    bundle.putInt("IGNORE_SEARCH", 0);
                    return bundle;
                }
            };
            adapter.addFragment(QuestFragment.class, "編成");
            adapter.addFragment(QuestFragment.class, "出擊");
            adapter.addFragment(QuestFragment.class, "演習");
            adapter.addFragment(QuestFragment.class, "遠征");
            adapter.addFragment(QuestFragment.class, "補給/入渠");
            adapter.addFragment(QuestFragment.class, "工廠");
            adapter.addFragment(QuestFragment.class, "改裝");
        } else {
            adapter = new ViewPagerAdapter(getChildFragmentManager()) {
                @Override
                public Bundle getArgs(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("TYPE", 0);
                    bundle.putInt("IGNORE_SEARCH", 1 - position);
                    bundle.putInt("FLAG", mFlag);
                    return bundle;
                }
            };
            adapter.addFragment(QuestFragment.class, "全部");
            adapter.addFragment(QuestFragment.class, "搜索结果");
        }

        return adapter;
    }
}