package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.Collections;

/**
 * Created by Rikka on 2016/7/28.
 */
public abstract class BaseItemTouchHelperAdapter<VH extends RecyclerView.ViewHolder, T extends Object> extends BaseRecyclerAdapter<VH, T> implements ItemTouchHelperAdapter {

    @Override
    public void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(getItemList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(getItemList(), i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        getItemList().remove(position);
        notifyItemRemoved(position);
    }
}
