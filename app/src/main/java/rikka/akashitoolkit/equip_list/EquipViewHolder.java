package rikka.akashitoolkit.equip_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/8/7.
 */
public class EquipViewHolder extends RecyclerView.ViewHolder {

    public TextView mName;
    public ImageView mIcon;

    public EquipViewHolder(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(android.R.id.title);
        mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
    }
}
