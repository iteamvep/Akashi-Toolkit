package rikka.akashitoolkit.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/31.
 */
public abstract class BaseItemDisplayActivity extends BaseActivity {
    public static final String EXTRA_START_Y = "EXTRA_START_Y";
    public static final String EXTRA_START_HEIGHT = "EXTRA_START_HEIGHT";

    private static final int ANIM_DURATION = 200;
    private static final int ANIM_DURATION_EXIT = 200;
    private static final int ANIM_DURATION_TEXT_FADE = 150;
    private static final int ANIM_DURATION_TEXT_FADE_DELAY = 150;

    private int mItemHeight;
    private int mItemY;

    private boolean mShouldAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_START_Y)) {
            mItemY =  intent.getIntExtra(EXTRA_START_Y, 0);
            mItemHeight = intent.getIntExtra(EXTRA_START_HEIGHT, 0);
            mShouldAnim = true;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(getTaskDescriptionLabel(), null, ContextCompat.getColor(this, R.color.background)));
        }

        if (!mShouldAnim) {
            return;
        }

        if (savedInstanceState == null) {
            animEnter();
            setViewsFadeAnim();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_display, menu);
        return true;
    }

    protected String getTaskDescriptionLabel() {
        return getString(R.string.app_name);
    }

    protected abstract ViewGroup getRootView();
    protected abstract View[] getAnimFadeViews();

    protected void setViewsFadeInAnim(View[] views) {
        for (View view : views) {

            view.setAlpha(0.0f);

            view.animate()
                    .setStartDelay(ANIM_DURATION_TEXT_FADE_DELAY)
                    .setDuration(ANIM_DURATION_TEXT_FADE)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .alpha(1)
                    .start();
        }
    }

    private void setViewsFadeAnim() {
        setViewsFadeInAnim(getAnimFadeViews());
    }

    protected void animEnter() {
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

        getRootView().post(new Runnable() {
            @Override
            public void run() {
                getRootView().setTranslationY(mItemY - getRootView().getHeight() / 2);
                getRootView().setScaleY((float) mItemHeight / (getRootView().getHeight()));

                getRootView()
                        .animate()
                        .setDuration(ANIM_DURATION)
                        .scaleY(1)
                        .translationY(0)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
            }
        });
    }

    protected void animExit() {
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

        getRootView().removeAllViews();
        getRootView().setScaleY(1);
        getRootView().setTranslationY(0);
        getRootView().setAlpha(1);

        getRootView()
                .animate()
                .setDuration(ANIM_DURATION_EXIT)
                .scaleY((float) mItemHeight / getRootView().getHeight())
                .translationY(mItemY - getRootView().getHeight() / 2)
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
        if (!mShouldAnim) {
            super.onBackPressed();
        } else {
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        animExit();
    }

    public void setToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);

        toolbar.setTitleTextAppearance(this, R.style.ItemActivity_Title);
        toolbar.setSubtitleTextAppearance(this, R.style.ItemActivity_Subtitle);
    }
}
