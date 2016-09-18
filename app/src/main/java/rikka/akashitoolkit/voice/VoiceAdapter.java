package rikka.akashitoolkit.voice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/7/25.
 */
public class VoiceAdapter extends BaseRecyclerAdapter<VoiceViewHolder, ShipVoice> {

    public VoiceAdapter(List<ShipVoice> list) {
        setItemList(list);
    }

    @Override
    public VoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_voice, parent, false));
    }
}
