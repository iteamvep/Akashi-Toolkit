package rikka.akashitoolkit.equip_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/8/7.
 */
public class EquipViewHolder extends RecyclerView.ViewHolder {

    public final TextView mNameTranslate;
    public final TextView mName;
    public final ImageView mIcon;

    public EquipViewHolder(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(android.R.id.text1);
        mNameTranslate = (TextView) itemView.findViewById(android.R.id.text2);
        mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
    }
}
