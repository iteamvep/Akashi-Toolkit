package rikka.akashitoolkit.voice;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/8/7.
 */
public class VoiceViewHolder extends RecyclerView.ViewHolder {
    public TextView mScene;
    public TextView mTextJa;
    public TextView mTextZh;

    public VoiceViewHolder(View itemView) {
        super(itemView);

        mScene = (TextView) itemView.findViewById(android.R.id.title);
        mTextJa = (TextView) itemView.findViewById(android.R.id.content);
        mTextZh = (TextView) itemView.findViewById(android.R.id.summary);
    }
}
