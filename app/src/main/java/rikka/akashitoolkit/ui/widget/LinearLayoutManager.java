package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/7/1.
 */
public class LinearLayoutManager extends android.support.v7.widget.LinearLayoutManager {

    private static final String TAG = "LinearLayoutManager";

    public LinearLayoutManager(Context context) {
        super(context);
    }

    public LinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*@Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
        Log.d(TAG, "requestChildRectangleOnScreen");
        return super.requestChildRectangleOnScreen(parent, child, rect, immediate);
    }

    @Override
    public void scrollToPosition(int position) {
        Log.d(TAG, "scrollToPosition");
        super.scrollToPosition(position);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        Log.d(TAG, "smoothScrollToPosition");
        super.smoothScrollToPosition(recyclerView, state, position);
    }*/

    public void smoothScrollToPosition(RecyclerView recyclerView, int position, int snapPreference) {
        //Log.d(TAG, "smoothScrollToPosition");

        smoothScrollToPositionWithOffset(recyclerView, position, snapPreference, 0);
    }

    public void smoothScrollToPositionWithOffset(RecyclerView recyclerView, final int position, final int snapPreference, final int offset) {
        //Log.d(TAG, "smoothScrollToPositionWithOffset");

        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return LinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    public int calculateDyToMakeVisible(View view, int snapPreference) {
                        return super.calculateDyToMakeVisible(view, snapPreference) + offset;
                    }

                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return 60f / displayMetrics.densityDpi;
                    }

                    @Override
                    protected int getVerticalSnapPreference() {
                        return snapPreference;
                    }
                };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
