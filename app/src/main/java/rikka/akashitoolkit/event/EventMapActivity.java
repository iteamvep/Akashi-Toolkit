package rikka.akashitoolkit.event;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventMapActivity extends BaseItemDisplayActivity {

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.content_container);

        setToolBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected ViewGroup getRootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected View[] getAnimFadeViews() {
        return new View[0];
    }

    /*@Override
    protected String getTaskDescriptionLabel() {
        return mTitle;
    }*/
}
