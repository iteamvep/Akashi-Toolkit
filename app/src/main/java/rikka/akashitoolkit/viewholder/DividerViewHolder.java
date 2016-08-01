package rikka.akashitoolkit.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/7/28.
 */
public class DividerViewHolder extends RecyclerView.ViewHolder {

    public View mDivider;

    public DividerViewHolder(View itemView) {
        super(itemView);

        mDivider = itemView.findViewById(R.id.divider);
    }
}
