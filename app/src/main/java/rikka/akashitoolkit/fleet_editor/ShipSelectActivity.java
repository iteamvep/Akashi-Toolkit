package rikka.akashitoolkit.fleet_editor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.otto.ShipAction;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.ship.ShipFragment;
import rikka.akashitoolkit.ui.BaseSearchActivity;

/**
 * Created by Rikka on 2016/7/29.
 */
public class ShipSelectActivity extends BaseSearchActivity {

    private ShipFragment mShipFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ship_select));

        if (savedInstanceState == null) {
            mShipFragment = new ShipFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(ShipFragment.ARG_SELECT_MODE, true);
            mShipFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, mShipFragment, "SHIP_FRAGMENT")
                    .commit();
        } else {
            mShipFragment = (ShipFragment) getSupportFragmentManager().findFragmentByTag("SHIP_FRAGMENT");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onItemSelected(ItemSelectAction.Finish action) {
        Intent intent = new Intent();
        intent.putExtra(FleetEditActivity.EXTRA_ID, action.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSearchExpand() {
        if (mShipFragment != null) {
            mShipFragment.isSearchingChanged(new ShipAction.IsSearchingChanged(true));
            mShipFragment.getAdapter().rebuildDataList();
        }
    }

    @Override
    public void onSearchCollapse() {
        if (mShipFragment != null) {
            mShipFragment.isSearchingChanged(new ShipAction.IsSearchingChanged(false));
            mShipFragment.getAdapter().rebuildDataList();
        }
    }

    @Override
    public void onSearchTextChange(String newText) {
        if (mShipFragment != null) {
            mShipFragment.keywordChanged(new ShipAction.KeywordChanged(newText.replace(" ", "")));
        }
    }
}
