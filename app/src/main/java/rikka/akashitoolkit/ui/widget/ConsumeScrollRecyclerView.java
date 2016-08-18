package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 用这样的糟糕方法解决当 RecyclerView 放在 CoordinatorLayout 里面时 overscroll 效果会没有的问题..
 */
public class ConsumeScrollRecyclerView extends RecyclerView {

    private static final String TAG = "ConsumeScrollRecyclerView";

    public ConsumeScrollRecyclerView(Context context) {
        this(context, null);
    }

    public ConsumeScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConsumeScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

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
