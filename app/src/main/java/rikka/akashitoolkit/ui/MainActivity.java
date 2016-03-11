package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
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
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.fragments.DataDisplayFragment;
import rikka.akashitoolkit.ui.fragments.HomeFragment;
import rikka.akashitoolkit.ui.fragments.TwitterFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private Map<Integer, Fragment> mFragmentMap;

    private int mLastDrawerItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);

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
            findFragmentByNavId(mFragmentMap, R.id.nav_maps);
            findFragmentByNavId(mFragmentMap, R.id.nav_quest);

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
                case R.id.nav_maps:
                case R.id.nav_quest:
                    break;
                default:
                    id = R.id.nav_home;
            }

            mNavigationView.setCheckedItem(id);
            selectDrawerItem(id);
        }
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
            case R.id.nav_twitter:
            case R.id.nav_maps:
            case R.id.nav_quest:
                selectDrawerItem(id);
                break;

            case R.id.nav_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private String genrateFragmentTAG(int id) {
        return String.format("%s %d", "FragmentTag", id);
    }

    private void selectDrawerItem(int id) {
        if (mLastDrawerItemId == id) {
            return;
        }

        Fragment from = mFragmentMap.get(mLastDrawerItemId);
        mLastDrawerItemId = id;
        Settings.instance(this)
                .putInt(Settings.LAST_DRAWER_ITEM_ID, id);

        Fragment to = mFragmentMap.get(id);
        if (to == null) {
            to = instanceFragment(id);
            mFragmentMap.put(id, to);
        }

        switchFragment(from, to, genrateFragmentTAG(id));
    }

    private Fragment instanceFragment(int id) {
        switch (id) {
            case R.id.nav_home:
                return new HomeFragment();
            case R.id.nav_twitter:
                return new TwitterFragment();
            case R.id.nav_maps:
                return new DataDisplayFragment();
            case R.id.nav_quest:
                return new DataDisplayFragment();
        }
        return new HomeFragment();
    }

    private void findFragmentByNavId(Map<Integer, Fragment> map, int id) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(genrateFragmentTAG(id));
        if (fragment != null) {
            map.put(id, fragment);
            /*getSupportFragmentManager().beginTransaction()
                    .hide(fragment)
                    .commit();*/
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
