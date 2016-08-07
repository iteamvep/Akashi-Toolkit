package rikka.akashitoolkit.map;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.widget.ExpandableLayout;

/**
 * Created by Rikka on 2016/8/7.
 */
public class MapViewHolder extends RecyclerView.ViewHolder {

    public ExpandableLayout mDetailContainer;
    public TextView mTitle;
    public TextView mTextView;
    public Button mButton;

    public MapViewHolder(View itemView) {
        super(itemView);

        mDetailContainer = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);
        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mTextView = (TextView) itemView.findViewById(android.R.id.content);
        mButton = (Button) itemView.findViewById(android.R.id.button1);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailContainer.isExpanded()) {
                    mDetailContainer.setExpanded(false);
                    mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                } else {
                    mDetailContainer.setExpanded(true);
                    mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }
        });
    }
}
