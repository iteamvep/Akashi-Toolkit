package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Rikka on 2016/7/2.
 */
public class ConsumeScrollRecyclerView extends RecyclerView {
    public ConsumeScrollRecyclerView(Context context) {
        super(context);
    }

    public ConsumeScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ConsumeScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        //Log.d(getClass().getSimpleName(), "" + dxConsumed + " " + dyConsumed + " " + dxUnconsumed + " " + dyUnconsumed);
        dxUnconsumed = dyUnconsumed = 0;
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }
}
