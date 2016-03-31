package rikka.akashitoolkit.ui;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipType;
import rikka.akashitoolkit.staticdata.QuestList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.ShipTypeList;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipDisplayActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_START_Y = "EXTRA_START_Y";
    public static final String EXTRA_START_HEIGHT = "EXTRA_START_HEIGHT";

    private static final int ANIM_DURATION = 200;
    private static final int ANIM_DURATION_EXIT = 200;
    private static final int ANIM_DURATION_TEXT_FADE = 150;
    private static final int ANIM_DURATION_TEXT_FADE_DELAY = 150;

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Ship mItem;

    private int mItemHeight;
    private int mItemY;
    private LinearLayout mItemAttrContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            id = intent.getIntExtra(EXTRA_ITEM_ID, 0);
            mItem = ShipList.findItemById(this, id);
            if (mItem == null) {
                Log.d("QAQ", "No item find? id=" + Integer.toString(id));
                finish();
                return;
            }
            mItemY =  intent.getIntExtra(EXTRA_START_Y, 0);
            mItemHeight = intent.getIntExtra(EXTRA_START_HEIGHT, 0);
        } else {
            finish();
            return;
        }

        setContentView(R.layout.activity_item_display);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        if (savedInstanceState == null) {
            animEnter();
        }

        setViews();
    }

    private void setViews() {
        mAppBarLayout
                .setAlpha(0.0f);

        mAppBarLayout
                .animate()
                .setStartDelay(ANIM_DURATION_TEXT_FADE_DELAY)
                .setDuration(ANIM_DURATION_TEXT_FADE)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(1)
                .start();

        mLinearLayout
                .setAlpha(0.0f);
        mLinearLayout
                .animate()
                .setStartDelay(ANIM_DURATION_TEXT_FADE_DELAY)
                .setDuration(ANIM_DURATION_TEXT_FADE)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(1)
                .start();

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Utils.isNightMode(getResources())) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);
            mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp_dark);
            mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        }

        if (mItem.getName() != null) {
            getSupportActionBar().setTitle(mItem.getName().getZh_cn());
        }
    }

    private void animEnter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.colorAnimation(
                    ContextCompat.getColor(this, android.R.color.transparent),
                    ContextCompat.getColor(this, R.color.colorItemDisplayStatusBar),
                    ANIM_DURATION,
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                            }
                        }
                    });
        }
        mCoordinatorLayout.post(new Runnable() {
            @Override
            public void run() {
                mCoordinatorLayout.setTranslationY(mItemY - mCoordinatorLayout.getHeight() / 2);
                mCoordinatorLayout.setScaleY((float) mItemHeight / (mCoordinatorLayout.getHeight()));

                mCoordinatorLayout
                        .animate()
                        .setDuration(ANIM_DURATION)
                        .scaleY(1)
                        .translationY(0)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
            }
        });
    }

    private void animExit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.colorAnimation(
                    ContextCompat.getColor(this, R.color.colorItemDisplayStatusBar),
                    ContextCompat.getColor(this, android.R.color.transparent),
                    ANIM_DURATION_EXIT,
                    new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                            }
                        }
                    });
        }

        mCoordinatorLayout.removeAllViews();
        mCoordinatorLayout.setScaleY(1);
        mCoordinatorLayout.setTranslationY(0);
        mCoordinatorLayout.setAlpha(1);

        mCoordinatorLayout
                .animate()
                .setDuration(ANIM_DURATION_EXIT)
                .scaleY((float) mItemHeight / mCoordinatorLayout.getHeight())
                .translationY(mItemY - mCoordinatorLayout.getHeight() / 2)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                        overridePendingTransition(0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        animExit();
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
}
