package rikka.akashitoolkit.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rikka on 2016/5/15.
 */
public class MyViewPager extends ViewPager {
    private boolean mSwipeEnabled;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSwipeEnabled = true;

        setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (mSwipeEnabled) {
                    return;
                }

                page.setTranslationX(page.getWidth() * -position);

                if (position <= -1 || position >= 1) {
                    page.setAlpha(0);
                } else if (position == 0) {
                    page.setAlpha(1f);
                } else {
                    page.setAlpha(1f - Math.abs(position));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mSwipeEnabled) {
            return true;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mSwipeEnabled) {
            return true;
        }

        return super.onTouchEvent(event);
    }

    public boolean isSwipeEnabled() {
        return mSwipeEnabled;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        mSwipeEnabled = swipeEnabled;
    }
}
