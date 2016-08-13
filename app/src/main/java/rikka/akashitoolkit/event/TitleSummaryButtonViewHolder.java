package rikka.akashitoolkit.event;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rikka.akashitoolkit.model.Event;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/8/13.
 */
public class TitleSummaryButtonViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder {

    public TextView mTitle;
    public TextView mSummary;
    public Button mButton;

    public TitleSummaryButtonViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mSummary = (TextView) itemView.findViewById(android.R.id.summary);
        mButton = (Button) itemView.findViewById(android.R.id.button1);

        if (mSummary == null) {
            mSummary = (TextView) itemView.findViewById(android.R.id.content);
        }
    }

    @Override
    public void bind(Object _data, int position) {
        if (!(_data instanceof Event.Url)) {
            return;
        }

        final Event.Url data = (Event.Url) _data;

        mTitle.setText(data.getTitle());
        mSummary.setText(data.getContent());
        mButton.setText(data.getUrlText());

        mSummary.setVisibility(TextUtils.isEmpty(data.getContent()) ? View.GONE : View.VISIBLE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
            }
        });
    }
}
