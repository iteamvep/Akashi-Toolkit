package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import rikka.akashitoolkit.viewholder.DividerViewHolder;

/**
 * Created by Rikka on 2016/7/28.
 */
public abstract class BaseDividerItemTouchHelperAdapter<VH extends DividerViewHolder, T>
        extends BaseItemTouchHelperAdapter<VH, T> implements DividerItemTouchHelperAdapter {

    Map<Integer, Boolean> map;

    public BaseDividerItemTouchHelperAdapter() {
        map = new HashMap<>();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //Log.d("QAQ", "onBindViewHolder " + position);

        if (map.get(position) != null) {
            if (position != getItemCount() - 1) {
                holder.mDivider.setVisibility(map.get(position) ? View.VISIBLE : View.INVISIBLE);
            } else {
                holder.mDivider.setVisibility(position == getItemCount() - 1 ? View.INVISIBLE : View.VISIBLE);
            }
            map.remove(position);

            return;
        }

        holder.mDivider.setVisibility(position == getItemCount() - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPosition, int toPosition) {
        super.onItemMove(recyclerView, viewHolder, target, fromPosition, toPosition);

        map.put(fromPosition, fromPosition > toPosition);
        notifyItemChanged(fromPosition);

        if (fromPosition > 0) {
            map.put(fromPosition - 1, true);
            notifyItemChanged(fromPosition - 1);
        }

        if (toPosition > 0) {
            map.put(toPosition - 1, false);
            notifyItemChanged(toPosition - 1);
        }

        map.put(toPosition, false);
        notifyItemChanged(toPosition);

        //Log.d("QAQ", map.toString());
    }

    @Override
    public void onItemSelected(int position, int actionState) {
        if (position != 0) {
            map.put(position - 1, actionState == 0);

            notifyItemChanged(position - 1);
        }
    }
}
