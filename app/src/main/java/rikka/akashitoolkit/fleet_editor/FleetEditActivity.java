package rikka.akashitoolkit.fleet_editor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.staticdata.FleetList;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.ui.widget.ItemTouchHelperCallback;
import rikka.akashitoolkit.utils.Utils;

public class FleetEditActivity extends AppCompatActivity {

    private static final String TAG = "FleetEditActivity";

    public static final String EXTRA_ID = "EXTRA_ID";

    private static final String JSON_NAME = "/json/fleets.json";
    private String CACHE_FILE;

    public static final int REQUEST_CODE_SELECT_SHIP = 0;
    public static final int REQUEST_CODE_SELECT_EQUIP = 1;

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, FleetEditActivity.class);
        intent.putExtra(EXTRA_ID, id);
        context.startActivity(intent);
    }

    private Fleet mFleet;
    private FleetAdapter mAdapter;

    /**
     * 第几个舰娘
     */
    private int mCurrentEditingPosition;
    /** 第几个装备槽 */
    private int mCurrentEditingSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.fleet_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.content_container);
        mAdapter = new FleetAdapter();
        recyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager;
        if (StaticData.instance(this).isTablet && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

            int padding = (int) getResources().getDimension(R.dimen.list_padding);
            recyclerView.setPadding(padding, padding, padding, padding);
            recyclerView.setClipToPadding(false);
        } else {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, 0, 0, Utils.dpToPx(8));

                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = Utils.dpToPx(8);
                    }
                }
            });
        }
        recyclerView.setLayoutManager(layoutManager);

        int position = getIntent().getIntExtra(EXTRA_ID, 0);
        mFleet = FleetList.get(this).get(position);
        mAdapter.setItemList(mFleet.getShips());

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        CACHE_FILE = getFilesDir().getAbsolutePath() + JSON_NAME;
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
    public void onBackPressed() {
        super.onBackPressed();

        mFleet.calc();
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

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(FleetList.get(this)).getBytes()),
                CACHE_FILE);
    }

    @Subscribe
    public void onSelectShipClicked(ItemSelectAction.StartShip action) {
        Intent intent = new Intent(this, ShipSelectActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SHIP);

        mCurrentEditingPosition = action.getPosition();
    }

    @Subscribe
    public void onSelectEquipClicked(ItemSelectAction.StartEquip action) {
        mCurrentEditingPosition = action.getPosition();
        mCurrentEditingSlot = action.getSlot();

        Log.d(TAG, "open equip select: position " + mCurrentEditingPosition
                + " slot " + mCurrentEditingSlot);

        Intent intent = new Intent(this, EquipSelectActivity.class);
        intent.putExtra(EquipSelectActivity.EXTRA_SHIP_ID, mFleet.getShips().get(mCurrentEditingPosition).getShip().getId());
        startActivityForResult(intent, REQUEST_CODE_SELECT_EQUIP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_SHIP) {
            if (resultCode != RESULT_OK) {
                return;
            }

            if (mFleet.getShips().size() == mCurrentEditingPosition) { // add
                Fleet.Ship item = new Fleet.Ship();
                item.setId(data.getIntExtra(EXTRA_ID, 1));

                if (item.init(this)) {
                    mFleet.getShips().add(item);
                }

                Log.d(TAG, "add ship position: " + mCurrentEditingPosition
                        /*+ " ship: " + mFleet.getShips().toString()*/);

                if (mFleet.getShips().size() != 6) {
                    mAdapter.notifyItemInserted(mCurrentEditingPosition);
                } else {
                    mAdapter.notifyItemChanged(mCurrentEditingPosition);
                }
            } else { // TODO edit
                mAdapter.getItem(mCurrentEditingPosition).setId(data.getIntExtra(EXTRA_ID, 1));

                Log.d(TAG, "edit ship position: " + mCurrentEditingPosition
                        /*+ " ship: " + mFleet.getShips().toString()*/);

                mAdapter.getItem(mCurrentEditingPosition).init(this);
                mAdapter.notifyItemChanged(mCurrentEditingPosition);
            }

            mFleet.calc();

        } else if (requestCode == REQUEST_CODE_SELECT_EQUIP) {
            if (resultCode != RESULT_OK) {
                return;
            }

            Fleet.Ship.Equip item = mFleet.getShips().get(mCurrentEditingPosition).getEquips().get(mCurrentEditingSlot);
            item.setId(data.getIntExtra(EXTRA_ID, 1));
            item.init(this);

            Log.d(TAG, "edit equip position: " + mCurrentEditingPosition
                    + " slot: " + mCurrentEditingSlot
                    + " list: " + mFleet.getShips().get(mCurrentEditingPosition).getEquips());

            mFleet.calc();

            mAdapter.notifyItemChanged(mCurrentEditingPosition);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
