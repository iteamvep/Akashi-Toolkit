package rikka.akashitoolkit.event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import rikka.akashitoolkit.model.Event;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/8/12.
 */
public class TitleSummaryViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder {

    public TextView mTitle;
    public TextView mSummary;

    public TitleSummaryViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mSummary = (TextView) itemView.findViewById(android.R.id.summary);

        if (mSummary == null) {
            mSummary = (TextView) itemView.findViewById(android.R.id.content);
        }
    }

    @Override
    public void bind(Object _data, int position) {
        if (!(_data instanceof Event.Title)) {
            return;
        }

        Event.Title data = (Event.Title) _data;

        mTitle.setText(data.getTitle());
        mSummary.setText(data.getContent());
    }
}
