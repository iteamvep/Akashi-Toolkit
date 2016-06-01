package rikka.akashitoolkit.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Rikka on 2016/4/27.
 */
public class ShowAboveSnackbarBehavior extends CoordinatorLayout.Behavior<View> {
    private ValueAnimator mTranslationYAnimator;
    private float mFabTranslationY;

    public ShowAboveSnackbarBehavior() {
    }

    public ShowAboveSnackbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        updateTranslationForSnackbar(parent, child, dependency);
        return false;
    }

    private void updateTranslationForSnackbar(CoordinatorLayout parent,
                                              final View view, View snackbar) {
        final float targetTransY = getTranslationYForSnackbar(parent, view);
        if (mFabTranslationY == targetTransY) {
            // We're already at (or currently animating to) the target value, return...
            return;
        }

        final float currentTransY = ViewCompat.getTranslationY(view);

        // Make sure that any current animation is cancelled
        if (mTranslationYAnimator != null && mTranslationYAnimator.isRunning()) {
            mTranslationYAnimator.cancel();
        }

        if (view.isShown()
                && Math.abs(currentTransY - targetTransY) > (view.getHeight() * 0.667f)) {
            // If the FAB will be travelling by more than 2/3 of it's height, let's animate
            // it instead
            if (mTranslationYAnimator == null) {
                mTranslationYAnimator = ValueAnimator.ofFloat(currentTransY, targetTransY);
                mTranslationYAnimator.setInterpolator(new FastOutSlowInInterpolator());
                mTranslationYAnimator.addUpdateListener(
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                ViewCompat.setTranslationY(view,
                                        (float) animation.getAnimatedValue());
                            }
                        }
                );
            }
            mTranslationYAnimator.setFloatValues(currentTransY, targetTransY);
            mTranslationYAnimator.start();
        } else {
            // Now update the translation Y
            ViewCompat.setTranslationY(view, targetTransY);
        }

        mFabTranslationY = targetTransY;
    }

    private float getTranslationYForSnackbar(CoordinatorLayout parent,
                                             View v) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(v);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(view, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
