package rikka.akashitoolkit.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/8/7.
 */
public class SimpleTitleViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder {

    public TextView mTitle;

    public SimpleTitleViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
    }

    @Override
    public void bind(Object data, int position) {
        if (data instanceof String) {
            mTitle.setText((String) data);
        }
    }
}
