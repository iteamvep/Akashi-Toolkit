package rikka.akashitoolkit.viewholder;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/9/24.
 */

public class SimpleStringHolder extends RecyclerView.ViewHolder implements IBindViewHolder {

    public TextView mText;

    public SimpleStringHolder(View itemView, @IdRes int id) {
        super(itemView);

        mText = (TextView) itemView.findViewById(id);

        if (mText == null) {
            throw new RuntimeException("view not found");
        }
    }

    @Override
    public void bind(Object data, int position) {
        if (data instanceof String) {
            mText.setText((String) data);
        }
    }
}
