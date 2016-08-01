package rikka.akashitoolkit.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import rikka.akashitoolkit.adapter.ItemTouchHelperAdapter;
import rikka.akashitoolkit.viewholder.ItemTouchHelperViewHolder;

/**
 * Created by Rikka on 2016/7/28.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemTouchHelperAdapter mAdapter;

    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            dragFlags = ((ItemTouchHelperViewHolder) viewHolder).getDragFlags();
            swipeFlags = ((ItemTouchHelperViewHolder) viewHolder).getSwipeFlags();
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(recyclerView, viewHolder, target, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder, direction, viewHolder.getAdapterPosition());
    }
}
