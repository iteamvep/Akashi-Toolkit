package rikka.akashitoolkit.ship;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/8/7.
 */
public class ShipViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public TextView mContent;
    public ImageView mIcon;
    public View mIconContainer;

    public ShipViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mContent = (TextView) itemView.findViewById(android.R.id.content);
        mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
        mIconContainer = itemView.findViewById(R.id.content_container);
    }
}
