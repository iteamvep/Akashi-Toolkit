package rikka.akashitoolkit.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/4/15.
 */
public class ExpandableLayout extends FrameLayout {
    private int mHeight;
    private int mAnimHeight;
    private int mLastAnimHeight;
    private boolean mExpanded;
    private boolean mAnimating;
    private ValueAnimator mValueAnimator;

    private OnHeightUpdatedListener mOnHeightUpdatedListener;

    public interface OnHeightUpdatedListener {
        void OnHeightUpdate(ExpandableLayout v, int height, int changed);
    }

    public void setOnHeightUpdatedListener(OnHeightUpdatedListener onHeightUpdatedListener) {
        mOnHeightUpdatedListener = onHeightUpdatedListener;
    }

    public ExpandableLayout(Context context) {
        this(context, null);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.ExpandableLayout, defStyleAttr, 0);

        mExpanded = a.getBoolean(R.styleable.ExpandableLayout_isExpanded, false);

        a.recycle();
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

    public void toggle() {
        setExpanded(!mExpanded);
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

        mLastAnimHeight = from;
        mValueAnimator = ValueAnimator.ofInt(from, to);
        mValueAnimator.setDuration(100);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.d("QAQ", Integer.toString((int) animation.getAnimatedValue()));
                //getLayoutParams().height = (int) animation.getAnimatedValue();
                mAnimHeight = (int) animation.getAnimatedValue();
                requestLayout();

                if (mOnHeightUpdatedListener != null) {
                    mOnHeightUpdatedListener.OnHeightUpdate(ExpandableLayout.this, mAnimHeight, mAnimHeight - mLastAnimHeight);
                }

                mLastAnimHeight = mAnimHeight;
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnHeightUpdatedListener != null) {
                    mOnHeightUpdatedListener.OnHeightUpdate(ExpandableLayout.this, mHeight, mHeight - mLastAnimHeight);
                }

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
