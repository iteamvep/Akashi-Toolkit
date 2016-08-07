package rikka.akashitoolkit.equip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/8/7.
 */
public class EquipViewHolder extends RecyclerView.ViewHolder {

    public View mLinearLayout;
    public TextView mName;
    public TextView mTitle;
    public ImageView mImageView;

    public EquipViewHolder(View itemView) {
        super(itemView);

        mLinearLayout = itemView.findViewById(R.id.linearLayout);
        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mName = (TextView) itemView.findViewById(R.id.textView);
        mImageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
