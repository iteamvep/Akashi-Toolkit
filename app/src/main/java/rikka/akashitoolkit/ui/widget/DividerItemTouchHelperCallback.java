package rikka.akashitoolkit.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import rikka.akashitoolkit.adapter.DividerItemTouchHelperAdapter;
import rikka.akashitoolkit.viewholder.DividerViewHolder;

/**
 * Created by Rikka on 2016/7/28.
 */
public class DividerItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private DividerItemTouchHelperAdapter mAdapter;
    private RecyclerView.ViewHolder mViewHolder;

    public DividerItemTouchHelperCallback(DividerItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = android.support.v7.widget.helper.ItemTouchHelper.UP | android.support.v7.widget.helper.ItemTouchHelper.DOWN;
        int swipeFlags = android.support.v7.widget.helper.ItemTouchHelper.START | android.support.v7.widget.helper.ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //Log.d("ItemTouchHelperCallback", "onMove from: " + viewHolder.getAdapterPosition() + " to: " + target.getAdapterPosition());
        mAdapter.onItemMove(recyclerView, viewHolder, target, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (viewHolder != null) {
            setDividerVisibility(viewHolder, false);
            mViewHolder = viewHolder;
            //Log.d("ItemTouchHelperCallback", "onSelectedChanged pos:" + viewHolder.getAdapterPosition() + " actionState: " + actionState);
        } else {
            setDividerVisibility(mViewHolder, true);
            //Log.d("ItemTouchHelperCallback", "null actionState: " + actionState);
        }

        mAdapter.onItemSelected(mViewHolder.getAdapterPosition(), actionState);
    }

    private void setDividerVisibility(RecyclerView.ViewHolder _viewHolder, boolean visible) {
        if (!(_viewHolder instanceof DividerViewHolder)) {
            return;
        }

        DividerViewHolder viewHolder = (DividerViewHolder) _viewHolder;
        viewHolder.mDivider.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
