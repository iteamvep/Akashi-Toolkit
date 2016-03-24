package rikka.akashitoolkit.ui;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.staticdata.ItemTypeList;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.utils.Utils;

public class ItemDisplayActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_START_Y = "EXTRA_START_Y";
    public static final String EXTRA_START_HEIGHT = "EXTRA_START_HEIGHT";

    private static final int ANIM_DURATION = 200;
    private static final int ANIM_DURATION_EXIT = 200;
    private static final int ANIM_DURATION_TEXT_FADE = 150;

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Item mItem;

    private int mItemHeight;
    private int mItemY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }*/

        int id;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            id = intent.getIntExtra(EXTRA_ITEM_ID, 0);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAnimation(
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
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                setViews();
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
        });
        /*mCoordinatorLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mCoordinatorLayout.removeOnLayoutChangeListener(this);


            }
        });*/





        mItem = ItemList.findItemById(this, id);
    }

    private void setViews() {
        /*mCoordinatorLayout
                        .setScaleY(0.5f);*/

        mAppBarLayout
                .setAlpha(0.2f);

        mAppBarLayout
                .animate()
                .setDuration(ANIM_DURATION_TEXT_FADE)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(1)
                .start();

        mLinearLayout
                .setAlpha(0.2f);
        mLinearLayout
                .animate()
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
            getSupportActionBar().setTitle(mItem.getName());
        }
        addTextView(mLinearLayout, String.format("No. %d %s", mItem.getId(), ItemTypeList.findItemById(this, mItem.getIcon()).getName()));
        addTextView(mLinearLayout, formatStars(mItem.getRarity()));
        addAttrTextView(mLinearLayout, "火力", mItem.getAttr().getFire());
        addAttrTextView(mLinearLayout, "对空", mItem.getAttr().getAa());
        addAttrTextView(mLinearLayout, "命中", mItem.getAttr().getAcc());
        addAttrTextView(mLinearLayout, "雷装", mItem.getAttr().getTorpedo());
        addAttrTextView(mLinearLayout, "爆装", mItem.getAttr().getBomb());
        addAttrTextView(mLinearLayout, "对潜", mItem.getAttr().getAs());
        addAttrTextView(mLinearLayout, "回避", mItem.getAttr().getDodge());
        addAttrTextView(mLinearLayout, "索敌", mItem.getAttr().getSearch());
        addRangeTextView(mLinearLayout, "射程", mItem.getAttr().getRange());
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

    private String formatStars(int value) {
        String star = "";
        while (value > 0) {
            star += "★";
            value --;
        }
        return star;
    }

    private void addRangeTextView(ViewGroup parent, String attrName, int value) {
        if (value == 0) {
            return;
        }

        addTextView(parent, formatRangeString(attrName, value));
    }

    private String formatRangeString(String attrName, int value) {
        String range = "";
        switch (value) {
            case 1: range = "短"; break;
            case 2: range = "中"; break;
            case 3: range = "长"; break;
            case 4: range = "超长"; break;
        }
        return String.format("%s: %s", attrName, range);
    }

    private void addAttrTextView(ViewGroup parent, String attrName, int value) {
        if (value == 0) {
            return;
        }

        addTextView(parent, formatAttrString(attrName, value));
    }

    private String formatAttrString(String attrName, int value) {
        return String.format("%s: %s%d", attrName, value > 0 ? "+" : "", value);
    }

    private void addTextView(ViewGroup parent, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        parent.addView(textView);
    }

    private void animExit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAnimation(
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

        /*mCoordinatorLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCoordinatorLayout
                        .animate()
                        //.setStartDelay((int) (ANIM_DURATION_EXIT * 0.95))
                        .setDuration((int) (ANIM_DURATION_EXIT * 0.2))
                        .alpha(0.2f)
                        .start();
            }
        }, (int) (ANIM_DURATION_EXIT * 0.8));*/

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

    void colorAnimation(int colorFrom, int colorTo, int duration, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration);
        colorAnimation.addUpdateListener(listener);
        colorAnimation.start();
    }
}
