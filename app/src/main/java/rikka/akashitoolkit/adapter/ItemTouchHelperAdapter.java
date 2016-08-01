package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Rikka on 2016/7/28.
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPosition, int toPosition);

    void onItemDismiss(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
