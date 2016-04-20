package rikka.akashitoolkit.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.Utils;

public class MapActivity extends BaseItemDisplayActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_ITEM_NAME = "EXTRA_ITEM_NAME";

    private int mSeaId;
    private int mId;
    private String mTitle;

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            mId = intent.getIntExtra(EXTRA_ITEM_ID, 11);
            mSeaId = mId % 10;
            mId = mId / 10;
            mTitle = intent.getStringExtra(EXTRA_ITEM_NAME);
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        setViews();
    }

    @Override
    protected ViewGroup getRootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected View[] getAnimFadeViews() {
        return new View[0];
    }

    @Override
    protected String getTaskDescriptionLabel() {
        return mTitle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("DefaultLocale")
    private void setViews() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mTitle);

        if (isNightMode()) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);
            mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            mToolbar.setSubtitleTextColor(Color.parseColor("#ffe0e0e0"));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp_dark);
            mToolbar.setTitleTextColor(Color.parseColor("#000000"));
            mToolbar.setSubtitleTextColor(Color.parseColor("#ff757575"));
        }

        Glide.with(this)
                .load(Utils.getKCWikiFileUrl(String.format("Map%d-%d.png", mSeaId, mId)))
                .crossFade()
                .into((ImageView) findViewById(R.id.imageView));
    }
}
