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

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.model.ItemImprovement;
import rikka.akashitoolkit.staticdata.ItemImprovementList;
import rikka.akashitoolkit.staticdata.ItemTypeList;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.utils.Utils;

public class ItemDisplayActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_START_Y = "EXTRA_START_Y";
    public static final String EXTRA_START_HEIGHT = "EXTRA_START_HEIGHT";

    private static final int ANIM_DURATION = 200;
    private static final int ANIM_DURATION_EXIT = 200;
    private static final int ANIM_DURATION_TEXT_FADE = 300;

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Item mItem;

    private int mItemHeight;
    private int mItemY;
    private LinearLayout mItemAttrContainer;
    private LinearLayout mItemImprovementContainer;

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
            mItem = ItemList.findItemById(this, id);
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
        mItemAttrContainer = (LinearLayout) findViewById(R.id.itemAttrContainer);
        mItemImprovementContainer = (LinearLayout) findViewById(R.id.itemImprovementContainer);

        if (savedInstanceState == null) {
            animEnter();
        }

        setViews();

    }

    private void setViews() {
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

        ((TextView) findViewById(R.id.text_title)).setText(String.format(
                "No. %d %s %s",
                mItem.getId(),
                ItemTypeList.findItemById(this, mItem.getSubType()).getName(),
                formatStars(mItem.getRarity())));

        addAttrView(mItemAttrContainer, "火力", mItem.getAttr().getFire(), R.drawable.item_attr_fire);
        addAttrView(mItemAttrContainer, "对空", mItem.getAttr().getAa(), R.drawable.item_attr_aa);
        addAttrView(mItemAttrContainer, "命中", mItem.getAttr().getAcc(), R.drawable.item_attr_acc);
        addAttrView(mItemAttrContainer, "雷装", mItem.getAttr().getTorpedo(), R.drawable.item_attr_torpedo);
        addAttrView(mItemAttrContainer, "爆装", mItem.getAttr().getBomb(), R.drawable.item_attr_bomb);
        addAttrView(mItemAttrContainer, "对潜", mItem.getAttr().getAs(), R.drawable.item_attr_asw);
        addAttrView(mItemAttrContainer, "回避", mItem.getAttr().getDodge(), R.drawable.item_attr_dodge);
        addAttrView(mItemAttrContainer, "索敌", mItem.getAttr().getSearch(), R.drawable.item_attr_search);
        addAttrView(mItemAttrContainer, "射程", mItem.getAttr().getRange(), R.drawable.item_attr_range);

        addItemImprovementView(mItemImprovementContainer);
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

    private void addItemImprovementView(ViewGroup parent) {
        ItemImprovement itemImprovement = ItemImprovementList.findItemById(this, mItem.getId());
        if (itemImprovement == null || itemImprovement.getSecretary() == null) {
            parent.setVisibility(View.GONE);
            return;
        }

        for (ItemImprovement.SecretaryEntity entry:
                itemImprovement.getSecretary()) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0, Utils.dpToPx(4), 0, Utils.dpToPx(4));

            for (int i = 0; i < entry.getDay().size(); i++) {
                TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.day_cricle, null);
                view.setText(DAY[i]);
                view.setEnabled(entry.getDay().get(i));
                linearLayout.addView(view);
            }

            TextView view = new TextView(this);
            view.setPadding(Utils.dpToPx(8), 0, Utils.dpToPx(8), 0);
            view.setText(entry.getName());
            linearLayout.addView(view);

            parent.addView(linearLayout);
        }
    }

    private static final String[] DAY = {"日", "一", "二", "三", "四", "五", "六"};

    private String formatStars(int value) {
        String star = "";
        while (value > 0) {
            star += "★";
            value --;
        }
        return star;
    }

    private String getRangeString(int value) {
        switch (value) {
            case 1: return "短";
            case 2: return "中";
            case 3: return "长";
            case 4: return "超长";
        }
        return "";
    }

    private void addTextView(ViewGroup parent, String text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        parent.addView(textView);
    }

    private void addTextView(ViewGroup parent, String text) {
        addTextView(parent, text, 14);
    }

    private LinearLayout mCurAttrLinearLayout;
    private int attr = 0;
    private void addAttrView(ViewGroup parent, String title, int value, int icon) {
        if (value == 0) {
            return;
        }

        attr ++;

        if (mCurAttrLinearLayout == null) {
            mCurAttrLinearLayout = new LinearLayout(this);
            mCurAttrLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mCurAttrLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32)));
            mCurAttrLinearLayout.setBaselineAligned(false);
            mCurAttrLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_attr_cell, mCurAttrLinearLayout);
            parent.addView(mCurAttrLinearLayout);
        }

        View cell = mCurAttrLinearLayout
                .findViewById((attr % 2 == 0) ? R.id.item_attr_cell2 : R.id.item_attr_cell);

        cell.setVisibility(View.VISIBLE);

        ((TextView) cell.findViewById(R.id.textView)).setText(title);

        if (icon == R.drawable.item_attr_range) {
            ((TextView) cell.findViewById(R.id.textView2)).setText(getRangeString(value));
        } else {
            ((TextView) cell.findViewById(R.id.textView2)).setText(String.format("%s%d", value > 0 ? "+" : "", value));
        }

        ((ImageView) cell.findViewById(R.id.imageView)).setImageDrawable(ContextCompat.getDrawable(this, icon));

        if (attr % 2 == 0) {
            mCurAttrLinearLayout = null;
        }
    }

    private void animEnter() {
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
