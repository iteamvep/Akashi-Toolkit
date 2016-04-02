package rikka.akashitoolkit.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
public abstract class BaseItemDisplayActivity extends BaseDayNightModeActivity {
    public static final String EXTRA_START_Y = "EXTRA_START_Y";
    public static final String EXTRA_START_HEIGHT = "EXTRA_START_HEIGHT";

    private static final int ANIM_DURATION = 200;
    private static final int ANIM_DURATION_EXIT = 200;
    private static final int ANIM_DURATION_TEXT_FADE = 150;
    private static final int ANIM_DURATION_TEXT_FADE_DELAY = 150;

    private int mItemHeight;
    private int mItemY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_START_Y)) {
            mItemY =  intent.getIntExtra(EXTRA_START_Y, 0);
            mItemHeight = intent.getIntExtra(EXTRA_START_HEIGHT, 0);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState == null) {
            animEnter();
            setViewsFadeAnim();
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(getTaskDescriptionLabel(), null, ContextCompat.getColor(this, R.color.background)));
        }
    }

    protected String getTaskDescriptionLabel() {
        return getString(R.string.app_name);
    }

    protected abstract ViewGroup getRootView();
    protected abstract View[] getAnimFadeViews();

    private void setViewsFadeAnim() {
        for (View view :
                getAnimFadeViews()) {

            view.setAlpha(0.0f);

            view.animate()
                    .setStartDelay(ANIM_DURATION_TEXT_FADE_DELAY)
                    .setDuration(ANIM_DURATION_TEXT_FADE)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .alpha(1)
                    .start();
        }
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
        animExit();
    }
}
