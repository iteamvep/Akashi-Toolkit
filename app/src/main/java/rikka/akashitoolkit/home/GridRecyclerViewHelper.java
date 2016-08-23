package rikka.akashitoolkit.home;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.utils.Utils;

/**
 * 根据是否是平板来设置 LayoutManager 的帮助类
 */
public class GridRecyclerViewHelper {

    static RecyclerView.ItemDecoration sItemDecoration;

    static {
        sItemDecoration = new RecyclerView.ItemDecoration() {
            int width = 0;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (width == 0) {
                    width = Utils.dpToPx(560 + 8 + 8 + 8);
                }

                outRect.left = (parent.getWidth() - width) / 2;
                outRect.right = (parent.getWidth() - width) / 2;
            }
        };
    }

    /**
     * 根据是否是平板来设置 LayoutManager
     *
     * @param recyclerView 被设置的 RecyclerView
     */

    public static void init(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();

        RecyclerView.LayoutManager layoutManager;
        if (StaticData.instance(context).isTablet && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Settings.instance(context).getBoolean(Settings.TWITTER_GRID_LAYOUT, false)) {
                layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);

                recyclerView.removeItemDecoration(sItemDecoration);
            } else {
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

                if (context.getResources().getDimension(R.dimen.card_width) != -1) {
                    recyclerView.removeItemDecoration(sItemDecoration);
                    recyclerView.addItemDecoration(sItemDecoration);
                }
            }
        } else {
            layoutManager = new rikka.akashitoolkit.ui.widget.LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
                @Override
                public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                    LinearSmoothScroller linearSmoothScroller =
                            new LinearSmoothScroller(recyclerView.getContext()) {
                                @Override
                                protected int getVerticalSnapPreference() {
                                    return LinearSmoothScroller.SNAP_TO_START;
                                }
                            };
                    linearSmoothScroller.setTargetPosition(position);
                    startSmoothScroll(linearSmoothScroller);
                }
            };
        }

        recyclerView.setLayoutManager(layoutManager);
    }
}
