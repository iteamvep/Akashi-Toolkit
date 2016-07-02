/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific languag`e governing permissions and
 * limitations under the License.
 */

package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/7/1.
 */
public class LinearLayoutManager extends android.support.v7.widget.LinearLayoutManager {
    public LinearLayoutManager(Context context) {
        super(context);
    }

    public LinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, int position, int snapPreference) {
        smoothScrollToPositionWithOffset(recyclerView, position, snapPreference, 0);
    }

    public void smoothScrollToPositionWithOffset(RecyclerView recyclerView, final int position, final int snapPreference, final int offset) {
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
