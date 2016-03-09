package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.fragments.DataDisplayFragment;
import rikka.akashitoolkit.ui.fragments.HomeFragment;
import rikka.akashitoolkit.ui.fragments.TwitterFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private int mLastDrawerItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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

        if (savedInstanceState == null) {
            switchFragment(new HomeFragment(), R.id.nav_home);
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
                switchFragment(new HomeFragment(), R.id.nav_home);
                break;
            case R.id.nav_twitter:
                switchFragment(new TwitterFragment(), R.id.nav_twitter);
                break;
            case R.id.nav_maps:
                switchFragment(new DataDisplayFragment(), R.id.nav_maps);
                break;
            case R.id.nav_quest:
                switchFragment(new DataDisplayFragment(), R.id.nav_maps);
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

    public void switchFragment(Fragment fragment, int id) {
        if (mLastDrawerItemId == id) {
            return;
        }

        mLastDrawerItemId = id;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment).commit();
    }
}
