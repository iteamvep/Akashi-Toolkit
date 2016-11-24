package rikka.akashitoolkit.ship_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.ui.widget.MyPasswordTransformationMethod;
import rikka.akashitoolkit.utils.LocaleUtils;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/18.
 */
public class ShipDetailVoiceViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<ShipVoice> {

    public TextView mScene;
    public TextView mTextJa;
    public TextView mTextZh;

    public ShipDetailVoiceViewHolder(View itemView) {
        super(itemView);

        mScene = (TextView) itemView.findViewById(android.R.id.title);
        mTextJa = (TextView) itemView.findViewById(android.R.id.content);
        mTextZh = (TextView) itemView.findViewById(android.R.id.summary);
    }

    private void setTextViewMode(boolean hide) {
        if (hide) {
            mTextJa.setTransformationMethod(MyPasswordTransformationMethod.getInstance());
            mTextZh.setTransformationMethod(MyPasswordTransformationMethod.getInstance());
        } else {
            mTextJa.setTransformationMethod(null);
            mTextZh.setTransformationMethod(null);
        }
    }

    @Override
    public void bind(final ShipVoice data, int position) {
        mScene.setText(data.getScene());
        mTextJa.setText(data.getJp());
        mTextZh.setText(data.getZh());

        mTextZh.setVisibility(LocaleUtils.isDataLanguageJapanese(itemView.getContext()) ? View.GONE : View.VISIBLE);

        if (data.getVoiceId() == 22) {
            setTextViewMode(true);
        } else {
            setTextViewMode(false);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextJa.getTransformationMethod() != null) {
                    setTextViewMode(false);
                } else {
                    String url = data.getUrl();
                    if (!url.startsWith("http")) {
                        url = Utils.getKCWikiFileUrl(url);
                    }

                    try {
                        MusicPlayer.play(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
