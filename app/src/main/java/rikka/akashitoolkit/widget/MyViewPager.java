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

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !mSwipeEnabled;
            }
        });

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

    public boolean isSwipeEnabled() {
        return mSwipeEnabled;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        mSwipeEnabled = swipeEnabled;
    }
}
