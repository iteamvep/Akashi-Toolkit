package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用糟糕方法解决当 RecyclerView 放在 CoordinatorLayout 里面时 overscroll 效果会没有的问题..
 * 用糟糕方法解决 RecyclerView 会自己动..
 */
public class ConsumeScrollRecyclerView extends RecyclerView {

    private static final String TAG = "RecyclerView";

    private boolean mDisableRequestChildFocus;

    public ConsumeScrollRecyclerView(Context context) {
        this(context, null);
    }

    public ConsumeScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConsumeScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mDisableRequestChildFocus = false;
    }

    public boolean isDisableRequestChildFocus() {
        return mDisableRequestChildFocus;
    }

    public void setDisableRequestChildFocus(boolean disableRequestChildFocus) {
        mDisableRequestChildFocus = disableRequestChildFocus;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        //Log.d(TAG, "requestChildFocus child " + child + " focused " + focused);

        if (!isDisableRequestChildFocus()) {
            super.requestChildFocus(child, focused);
        }
    }

    /*@Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        Log.d(TAG, "requestChildRectangleOnScreen child " + child + " rect " + rect + " immediate " + immediate);

        return super.requestChildRectangleOnScreen(child, rect, immediate);
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        Log.d(TAG, "smoothScrollBy dx " + dx + " dy " + dy);
        super.smoothScrollBy(dx, dy);
    }*/

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        //Log.d(TAG, "" + dxConsumed + " " + dyConsumed + " " + dxUnconsumed + " " + dyUnconsumed);
        if (getParent() instanceof SwipeRefreshLayout) {
            if (dyUnconsumed > 0) {
                dxUnconsumed = dyUnconsumed = 0;
            }
        } else {
            dxUnconsumed = dyUnconsumed = 0;
        }
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }
}
