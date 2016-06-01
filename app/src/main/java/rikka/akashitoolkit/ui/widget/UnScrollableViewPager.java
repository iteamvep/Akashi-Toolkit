package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rikka on 2016/5/17.
 */
public class UnScrollableViewPager extends ViewPager {
    public UnScrollableViewPager(Context context) {
        this(context, null);
    }

    public UnScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
