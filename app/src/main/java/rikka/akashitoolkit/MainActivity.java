package rikka.akashitoolkit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

import rikka.akashitoolkit.enemy.EnemyDisplayFragment;
import rikka.akashitoolkit.equip_improvement.EquipImprovementDisplayFragment;
import rikka.akashitoolkit.equip_list.EquipTypeListFragment;
import rikka.akashitoolkit.event.EventFragment;
import rikka.akashitoolkit.expedition.ExpeditionDisplayFragment;
import rikka.akashitoolkit.home.HomeFragment;
import rikka.akashitoolkit.map.MapDisplayFragment;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ChangeNavigationDrawerItemAction;
import rikka.akashitoolkit.quest.QuestDisplayFragment;
import rikka.akashitoolkit.settings.AboutActivity;
import rikka.akashitoolkit.settings.SettingActivity;
import rikka.akashitoolkit.ship.ShipDisplayFragment;
import rikka.akashitoolkit.support.Push;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.tools.ToolsFragment;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.ui.fragments.DrawerFragment;
import rikka.akashitoolkit.ui.fragments.IBackFragment;
import rikka.akashitoolkit.ui.widget.IconSwitchCompat;
import rikka.akashitoolkit.ui.widget.SimpleDrawerView;
import rikka.minidrawer.MiniDrawerLayout;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;
    private MiniDrawerLayout mMiniDrawerLayout;
    private ScrimInsetsFrameLayout mNavigationViewRight;
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private IconSwitchCompat mSwitch;

    private Map<Integer, Fragment> mFragmentMap;

    private int mLastDrawerItemId;
    private SimpleDrawerView mRightDrawerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Push.init(this);

        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.background)));

        mSwitch = (IconSwitchCompat) findViewById(R.id.switch_bookmark);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (!StaticData.instance(this).isTablet) {
            mNavigationView = (NavigationView) findViewById(R.id.nav_view);
            mNavigationView.setNavigationItemSelectedListener(this);
            mNavigationView.setCheckedItem(R.id.nav_home);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        } else {
            mMiniDrawerLayout = (MiniDrawerLayout) findViewById(R.id.mini_drawer_layout);
            mMiniDrawerLayout.setNavigationItemSelectedListener(this);
            mMiniDrawerLayout.setCheckedItem(R.id.nav_home);

            DrawerArrowDrawable drawable = new DrawerArrowDrawable(this);
            drawable.setColor(Color.parseColor("#FFFFFF"));
            getSupportActionBar().setHomeAsUpIndicator(drawable);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mNavigationViewRight = (ScrimInsetsFrameLayout) findViewById(R.id.nav_view_right_out);
        mRightDrawerContent = (SimpleDrawerView) findViewById(R.id.nav_view_right);

        mFragmentMap = new HashMap<>();

        int id = Settings
                .instance(this)
                .getInt(Settings.LAST_DRAWER_ITEM_ID, R.id.nav_home);

        if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)) {
            String extra = getIntent().getStringExtra(EXTRA_EXTRA);
            if (extra != null) {
                switch (extra) {
                    case "nav_home":
                        id = R.id.nav_home;
                        break;
                    /*case "nav_twitter":
                        id = R.id.nav_twitter;
                        break;
                    case "nav_new":
                        id = R.id.nav_new;*/
                    //break;
                }
            }
        }

        if (savedInstanceState != null) {
            findFragmentByNavId(mFragmentMap, R.id.nav_home);
            /*findFragmentByNavId(mFragmentMap, R.id.nav_twitter);*/
            findFragmentByNavId(mFragmentMap, R.id.nav_new);
            findFragmentByNavId(mFragmentMap, R.id.nav_equip_improve);
            findFragmentByNavId(mFragmentMap, R.id.nav_enemy);
            findFragmentByNavId(mFragmentMap, R.id.nav_equip);
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
                /*case R.id.nav_twitter:*/
                case R.id.nav_new:
                case R.id.nav_enemy:
                case R.id.nav_equip_improve:
                case R.id.nav_equip:
                case R.id.nav_quest:
                case R.id.nav_map:
                case R.id.nav_ship:
                case R.id.nav_expedition:
                case R.id.nav_tools:
                    break;
                default:
                    id = R.id.nav_home;
            }

            if (!StaticData.instance(this).isTablet) {
                mNavigationView.setCheckedItem(id);
            } else {
                mMiniDrawerLayout.setCheckedItem(id);
            }

            selectDrawerItem(id);
        }
    }

    public SimpleDrawerView getRightDrawerContent() {
        return mRightDrawerContent;
    }

    public IconSwitchCompat getSwitch() {
        return mSwitch;
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

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        if (mLastDrawerItemId != R.id.nav_home) {
            boolean consumed = false;
            for (Fragment f : getSupportFragmentManager().getFragments()) {
                if (f == null || !f.isVisible()) {
                    continue;
                }

                if (f.getChildFragmentManager().getFragments() != null) {
                    for (Fragment f2 : f.getChildFragmentManager().getFragments()) {
                        if (f2 == null || !f2.isVisible()) {
                            continue;
                        }

                        if (f2 instanceof IBackFragment) {
                            consumed |= ((IBackFragment) f2).onBackPressed();
                        }
                    }
                }

                if (f instanceof IBackFragment) {
                    consumed |= ((IBackFragment) f).onBackPressed();
                }
            }

            if (!consumed) {
                selectDrawerItem(R.id.nav_home);
                if (!StaticData.instance(this).isTablet)
                    mNavigationView.setCheckedItem(R.id.nav_home);
                else {
                    mMiniDrawerLayout.setCheckedItem(R.id.nav_home);
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!StaticData.instance(this).isTablet) {
            return super.onOptionsItemSelected(item);
        }

        if (item.getItemId() == android.R.id.home) {
            mMiniDrawerLayout.toggle();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("DefaultLocale")
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
            if (!(to instanceof DrawerFragment)) {
                throw new RuntimeException("must be subclass of DrawerFragment");
            }
            mFragmentMap.put(id, to);
        }

        switchFragment(from, to, generateFragmentTAG(id));
    }

    private Fragment instanceFragment(int id) {
        switch (id) {
            case R.id.nav_home:
                return new HomeFragment();
            /*case R.id.nav_twitter:
                return new TwitterFragment();*/
            case R.id.nav_new:
                return new EventFragment();
            case R.id.nav_enemy:
                return new EnemyDisplayFragment();
            case R.id.nav_equip_improve:
                return new EquipImprovementDisplayFragment();
            case R.id.nav_equip:
                return new EquipTypeListFragment();
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

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.instance().register(this);
    }

    @Override
    protected void onStop() {
        BusProvider.instance().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void changeNavigationItem(ChangeNavigationDrawerItemAction action) {
        selectDrawerItem(action.getItem());

        if (mMiniDrawerLayout != null) {
            mMiniDrawerLayout.setCheckedItem(action.getItem());
        } else {
            mNavigationView.setCheckedItem(action.getItem());
        }
    }
}
