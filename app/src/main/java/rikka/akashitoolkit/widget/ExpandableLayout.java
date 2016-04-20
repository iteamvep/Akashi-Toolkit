package rikka.akashitoolkit.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by Rikka on 2016/4/15.
 */
public class ExpandableLayout extends FrameLayout {
    private int mHeight;
    private int mAnimHeight;
    private boolean mExpanded = false;
    private boolean mAnimating;
    private ValueAnimator mValueAnimator;

    public ExpandableLayout(Context context) {
        super(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isInEditMode()) {
            return;
        }

        mHeight = getMeasuredHeight();

        if (mAnimating) {
            setMeasuredDimension(getMeasuredWidth(), mAnimHeight);
        } else if (!mExpanded) {
            setMeasuredDimension(getMeasuredWidth(), 0);
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, true);
    }

    public void setExpanded(boolean expanded, boolean anim) {
        if (mExpanded == expanded) {
            return;
        }

        mExpanded = expanded;

        if (mAnimating) {
            mValueAnimator.cancel();
        }

        if (!anim) {
            mAnimating = false;
            requestLayout();
            return;
        }

        final int from, to;
        if (mExpanded) {
            from = 0;
            to = mHeight;
        } else {
            from = mHeight;
            to = 0;
        }

        mValueAnimator = ValueAnimator.ofInt(from, to);
        mValueAnimator.setDuration(100);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.d("QAQ", Integer.toString((int) animation.getAnimatedValue()));
                //getLayoutParams().height = (int) animation.getAnimatedValue();
                mAnimHeight = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();
    }
}
