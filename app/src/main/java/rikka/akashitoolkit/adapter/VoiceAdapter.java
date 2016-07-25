package rikka.akashitoolkit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/7/25.
 */
public class VoiceAdapter extends BaseRecyclerAdapter<ViewHolder.Voice> {

    public VoiceAdapter(List<Object> list) {
        setItemList(list);
    }

    private ShipVoice getItem(int position) {
        return (ShipVoice) getItemData(position);
    }

    @Override
    public ViewHolder.Voice onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder.Voice(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_voice, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder.Voice holder, int position) {
        ShipVoice item = getItem(position);

        holder.mScene.setText(item.getScene());
        holder.mTextJa.setText(item.getJp());
        holder.mTextZh.setText(item.getZh());

        holder.mTextJa.setVisibility(Utils.isJapanese(holder.itemView.getContext()) ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShipVoice item = getItem(holder.getAdapterPosition());
                String url = item.getUrl();
                if (!url.startsWith("http")) {
                    url = Utils.getKCWikiFileUrl(url);
                }

                try {
                    MusicPlayer.play(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
