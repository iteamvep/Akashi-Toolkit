package rikka.akashitoolkit.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/8/7.
 */
public class SimpleTitleDividerViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public View mDivider;

    public SimpleTitleDividerViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mDivider = itemView.findViewById(R.id.divider);
    }
}
