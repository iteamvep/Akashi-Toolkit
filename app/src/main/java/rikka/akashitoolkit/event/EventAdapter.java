package rikka.akashitoolkit.event;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, Object> {

    private static final String TAG = "EventAdapter";

    public EventAdapter() {
    }


    @Override
    public void rebuildDataList() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Event.TYPE_TITLE:
                return new TitleSummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_title, parent, false));
            case Event.TYPE_GALLERY:
                return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_gallery, parent, false));
            case Event.TYPE_CONTENT:
                return new TitleSummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_text, parent, false));
            case Event.TYPE_URL:
                return new TitleSummaryButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_action, parent, false));
            case Event.TYPE_VOICE:
                return new VoicesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_voice, parent, false));
        }
        return null;
    }
}
