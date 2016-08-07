package rikka.akashitoolkit.fleet_editor;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import rikka.akashitoolkit.viewholder.ItemTouchHelperViewHolder;

/**
 * Created by Rikka on 2016/7/29.
 */
public class FleetEquipViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    public static FleetEquipViewHolder create(ViewGroup parent, @LayoutRes int resId) {
        return new FleetEquipViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }

    public TextView mTitle;
    public TextView mSummary;
    public ImageView mIcon;

    public boolean drag;
    public boolean swipe;

    public FleetEquipViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mSummary = (TextView) itemView.findViewById(android.R.id.summary);
        mIcon = (ImageView) itemView.findViewById(android.R.id.icon);

        drag = true;
        swipe = false;
    }

    @Override
    public int getDragFlags() {
        return drag ? dragFlags : 0;
    }

    @Override
    public int getSwipeFlags() {
        return swipe ? swipeFlags : 0;
    }
}
