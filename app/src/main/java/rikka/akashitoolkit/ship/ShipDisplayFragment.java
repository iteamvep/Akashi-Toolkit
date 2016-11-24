package rikka.akashitoolkit.ship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.model.ShipType;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ShipAction;
import rikka.akashitoolkit.staticdata.ShipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.ui.fragments.BaseSearchFragment;
import rikka.akashitoolkit.ui.fragments.BookmarkNoItemFragment;
import rikka.akashitoolkit.ui.fragments.ISwitchFragment;
import rikka.akashitoolkit.ui.widget.IconSwitchCompat;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.ui.widget.CheckBoxGroup;
import rikka.akashitoolkit.ui.widget.RadioButtonGroup;
import rikka.akashitoolkit.ui.widget.SimpleDrawerView;
import rikka.akashitoolkit.ui.widget.UnScrollableViewPager;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipDisplayFragment extends BaseSearchFragment implements ISwitchFragment {
    private ViewPager mViewPager;
    private int mFlag;
    private int mFinalVersion;
    private int mSpeed;
    private int mSort;
    private boolean mBookmarked;

    @Override
    protected View onCreateRightDrawerContentView(@Nullable Bundle savedInstanceState) {
        SimpleDrawerView body = new SimpleDrawerView(getContext());

        body.setOrientation(LinearLayout.VERTICAL);
        body.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        body.addTitle(getString(R.string.sort));
        body.addDivider();

        RadioButtonGroup sort = new RadioButtonGroup(getContext());
        sort.addItem(R.string.ship_type);
        sort.addItem(R.string.ship_class);
        sort.setOnCheckedChangeListener(new RadioButtonGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int checked) {
                mSort = checked;

                Settings
                        .instance(getContext())
                        .putInt(Settings.SHIP_SORT, mSort);

                BusProvider.instance().post(new ShipAction.SortChangeAction(mSort));
            }
        });

        mSort = Settings
                .instance(getContext())
                .getInt(Settings.SHIP_SORT, 0);

        sort.setChecked(mSort);

        body.addView(sort);
        body.addDivider();

        body.addTitle(getString(R.string.action_filter));
        body.addDivider();

        RadioButtonGroup remodel = new RadioButtonGroup(getContext());
        remodel.addItem(R.string.all);
        remodel.addItem(R.string.not_remodel);
        remodel.addItem(R.string.final_remodel);
        remodel.setOnCheckedChangeListener(new RadioButtonGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int checked) {
                BusProvider.instance().post(new ShipAction.ShowFinalVersionChangeAction(checked));

                mFinalVersion = checked;
                Settings
                        .instance(getContext())
                        .putInt(Settings.SHIP_FINAL_VERSION, checked);
            }
        });

        mFinalVersion = Settings
                .instance(getContext())
                .getInt(Settings.SHIP_FINAL_VERSION, 1);

        remodel.setChecked(mFinalVersion);

        body.addView(remodel);
        body.addDivider();

        CheckBoxGroup speed = new CheckBoxGroup(getContext());
        speed.addItem(R.string.speed_slow);
        speed.addItem(R.string.speed_fast);
        speed.setOnCheckedChangeListener(new CheckBoxGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int checked) {
                BusProvider.instance().post(new ShipAction.SpeedChangeAction(checked));

                mSpeed = checked;
                Settings
                        .instance(getContext())
                        .putInt(Settings.SHIP_SPEED, checked);
            }
        });


        body.addView(speed);
        body.addDivider();

        mSpeed = Settings
                .instance(getContext())
                .getInt(Settings.SHIP_SPEED, 0);

        speed.setChecked(mSpeed);

        CheckBoxGroup type = new CheckBoxGroup(getContext());

        for (ShipType shipType :
                ShipTypeList.get(getActivity())) {
            if (shipType.getId() == 1 || shipType.getId() == 12 || shipType.getId() == 15) {
                continue;
            }

            type.addItem(String.format("%s (%s)", shipType.getName().get(), shipType.getShortX()), shipType.getId());
        }

        type.setOnCheckedChangeListener(new CheckBoxGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int checked) {
                BusProvider.instance().post(new ShipAction.TypeChangeAction(checked));

                mFlag = checked;
                Settings
                        .instance(getContext())
                        .putInt(Settings.SHIP_FILTER, checked);
            }
        });

        body.addView(type);

        mFlag = Settings
                .instance(getContext())
                .getInt(Settings.SHIP_FILTER, 0);

        type.setChecked(mFlag);

        NestedScrollView scrollView = new NestedScrollView(getContext());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.setPadding(0, Utils.dpToPx(4), 0, Utils.dpToPx(4));
        scrollView.setClipToPadding(false);
        scrollView.addView(body);

        return scrollView;
    }

    @Override
    protected boolean onSetTabLayout(TabLayout tabLayout) {
        tabLayout.setupWithViewPager(mViewPager);
        return false;
    }

    @Override
    protected boolean onSetSwitch(IconSwitchCompat switchView) {
        return true;
    }

    @Override
    public void onSwitchCheckedChanged(boolean isChecked) {
        int checked = isChecked ? 1 : 0;

        BusProvider.instance().post(
                new BookmarkAction.Changed(ShipFragment.TAG, checked > 0));

        mBookmarked = checked > 0;
        /*Settings
                .instance(getContext())
                .putBoolean(Settings.SHIP_BOOKMARKED, checked > 0);*/

        if (mViewPager.getCurrentItem() == 1 && !mBookmarked) {
            mViewPager.setCurrentItem(0, false);
        }
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

    @Override
    public void onShow() {
        super.onShow();

        BusProvider.instance().post(new ShipAction.KeywordChanged(null));

        setToolbarTitle(getString(R.string.ship));

        Statistics.onFragmentStart("ShipDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("ShipDisplayFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ship, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSearchExpand() {
        BusProvider.instance().post(new ShipAction.IsSearchingChanged(true));
        BusProvider.instance().post(new ShipAction.KeywordChanged(null));

        if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onSearchCollapse() {
        BusProvider.instance().post(new ShipAction.IsSearchingChanged(false));
        BusProvider.instance().post(new ShipAction.KeywordChanged(null));
    }

    @Override
    public void onSearchTextChange(String newText) {
        BusProvider.instance().post(new ShipAction.KeywordChanged(newText.replace(" ", "")));
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
        return inflater.inflate(R.layout.content_unscrollable_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (UnScrollableViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());

        super.onViewCreated(view, savedInstanceState);
    }

    private ViewPagerAdapter getAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ShipFragment.ARG_TYPE_FLAG, mFlag);
                bundle.putInt(ShipFragment.ARG_FINAL_VERSION, mFinalVersion);
                bundle.putInt(ShipFragment.ARG_SPEED, mSpeed);
                bundle.putInt(ShipFragment.ARG_SORT, mSort);
                bundle.putBoolean(ShipFragment.ARG_BOOKMARKED, mBookmarked);
                return bundle;
            }
        };
        adapter.addFragment(ShipFragment.class, getString(R.string.all));
        adapter.addFragment(BookmarkNoItemFragment.class, getString(R.string.all));

        return adapter;
    }

    @Override
    public String getSearchHint() {
        return getString(R.string.search_hint_ship);
    }

    @Subscribe
    public void bookmarkNoItem(BookmarkAction.NoItem action) {
        mViewPager.setCurrentItem(1, false);
    }
}
