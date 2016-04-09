package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.fragments.ExpeditionDisplayFragment;
import rikka.akashitoolkit.ui.fragments.ItemDisplayFragment;
import rikka.akashitoolkit.ui.fragments.ItemImprovementDisplayFragment;
import rikka.akashitoolkit.ui.fragments.MapDisplayFragment;
import rikka.akashitoolkit.ui.fragments.QuestDisplayFragment;
import rikka.akashitoolkit.ui.fragments.HomeFragment;
import rikka.akashitoolkit.ui.fragments.ShipDisplayFragment;
import rikka.akashitoolkit.ui.fragments.ToolsFragment;
import rikka.akashitoolkit.ui.fragments.TwitterFragment;
import rikka.akashitoolkit.widget.SimpleDrawerView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;
    private ScrimInsetsFrameLayout mNavigationViewRight;
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private Map<Integer, Fragment> mFragmentMap;

    private int mLastDrawerItemId;
    private SimpleDrawerView mRightDrawerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);

        mNavigationViewRight = (ScrimInsetsFrameLayout) findViewById(R.id.nav_view_right_out);
        mRightDrawerContent = (SimpleDrawerView) findViewById(R.id.nav_view_right);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mFragmentMap = new HashMap<>();

        int id = Settings
                .instance(this)
                .getInt(Settings.LAST_DRAWER_ITEM_ID, R.id.nav_home);

        if (savedInstanceState != null) {
            findFragmentByNavId(mFragmentMap, R.id.nav_home);
            findFragmentByNavId(mFragmentMap, R.id.nav_twitter);
            findFragmentByNavId(mFragmentMap, R.id.nav_item_improve);
            findFragmentByNavId(mFragmentMap, R.id.nav_item);
            findFragmentByNavId(mFragmentMap, R.id.nav_quest);
            findFragmentByNavId(mFragmentMap, R.id.nav_map);
            findFragmentByNavId(mFragmentMap, R.id.nav_ship);
            findFragmentByNavId(mFragmentMap, R.id.nav_expedition);
            findFragmentByNavId(mFragmentMap, R.id.nav_tools);

            mLastDrawerItemId = id;

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();

            for (Map.Entry<Integer, Fragment> entry : mFragmentMap.entrySet()) {
                if (entry.getKey() != id) {
                    trans.hide(entry.getValue());
                }
            }
            trans.commit();

        } else {
            switch (id) {
                case R.id.nav_home:
                case R.id.nav_twitter:
                case R.id.nav_item_improve:
                case R.id.nav_item:
                case R.id.nav_quest:
                case R.id.nav_map:
                case R.id.nav_ship:
                case R.id.nav_expedition:
                case R.id.nav_tools:
                    break;
                default:
                    id = R.id.nav_home;
            }

            mNavigationView.setCheckedItem(id);
            selectDrawerItem(id);
        }
    }

    public SimpleDrawerView getRightDrawerContent() {
        return mRightDrawerContent;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setRightDrawerLocked(boolean locked) {
        mDrawerLayout.setDrawerLockMode(locked ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNDEFINED, GravityCompat.END);
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else if (mLastDrawerItemId != R.id.nav_home) {
            selectDrawerItem(R.id.nav_home);
            mNavigationView.setCheckedItem(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                selectDrawerItem(id);
                break;
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private String generateFragmentTAG(int id) {
        return String.format("%s %d", "FragmentTag", id);
    }

    private void selectDrawerItem(int id) {
        if (mLastDrawerItemId == id) {
            return;
        }

        //if (id == R.id.nav_home) {
            mAppBarLayout.setExpanded(true, false);
        //}

        Fragment from = mFragmentMap.get(mLastDrawerItemId);
        mLastDrawerItemId = id;
        Settings.instance(this)
                .putInt(Settings.LAST_DRAWER_ITEM_ID, id);

        Fragment to = mFragmentMap.get(id);
        if (to == null) {
            to = instanceFragment(id);
            mFragmentMap.put(id, to);
        }

        switchFragment(from, to, generateFragmentTAG(id));
    }

    private Fragment instanceFragment(int id) {
        switch (id) {
            case R.id.nav_home:
                return new HomeFragment();
            case R.id.nav_twitter:
                return new TwitterFragment();
            case R.id.nav_item_improve:
                return new ItemImprovementDisplayFragment();
            case R.id.nav_item:
                return new ItemDisplayFragment();
            case R.id.nav_map:
                return new MapDisplayFragment();
            case R.id.nav_quest:
                return new QuestDisplayFragment();
            case R.id.nav_ship:
                return new ShipDisplayFragment();
            case R.id.nav_expedition:
                return new ExpeditionDisplayFragment();
            case R.id.nav_tools:
                return new ToolsFragment();
        }
        return new HomeFragment();
    }

    private void findFragmentByNavId(Map<Integer, Fragment> map, int id) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(generateFragmentTAG(id));
        if (fragment != null) {
            map.put(id, fragment);
        }
    }

    private void switchFragment(Fragment from, Fragment to, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        if (from != null) {
            transaction.hide(from);
        }

        if (!to.isAdded()) {
            transaction
                    .add(R.id.fragment_container, to, tag);
        } else {
            transaction.show(to);
        }

        transaction.commit();
    }

    public void showSnackbar(@StringRes int resId, @Snackbar.Duration int duration) {
        Snackbar.make(mCoordinatorLayout, resId, duration)
                .show();
    }

    public void showSnackbar(CharSequence text, @Snackbar.Duration int duration) {
        Snackbar.make(mCoordinatorLayout, text, duration)
                .show();
    }
}
