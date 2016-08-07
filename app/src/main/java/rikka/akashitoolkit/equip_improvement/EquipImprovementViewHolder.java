package rikka.akashitoolkit.equip_improvement;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/8/7.
 */
public class EquipImprovementViewHolder extends RecyclerView.ViewHolder {
    public TextView mName;
    public TextView mShip;
    public ImageView mImageView;

    public EquipImprovementViewHolder(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(android.R.id.title);
        mShip = (TextView) itemView.findViewById(android.R.id.summary);
        mImageView = (ImageView) itemView.findViewById(android.R.id.icon);
    }
}