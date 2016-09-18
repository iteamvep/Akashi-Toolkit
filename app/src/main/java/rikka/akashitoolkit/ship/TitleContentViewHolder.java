package rikka.akashitoolkit.ship;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/9/17.
 */
public class TitleContentViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public TextView mContent;

    public TitleContentViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mContent = (TextView) itemView.findViewById(android.R.id.content);
    }
}
