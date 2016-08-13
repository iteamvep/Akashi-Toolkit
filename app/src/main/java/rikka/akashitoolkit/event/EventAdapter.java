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

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_GALLERY = 1;
    public static final int TYPE_CONTENT = 2;
    public static final int TYPE_MAPS = 3;
    public static final int TYPE_URL = 4;

    public EventAdapter() {
    }


    @Override
    public void rebuildDataList() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return new TitleSummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_title, parent, false));
            case TYPE_GALLERY:
                return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_gallery, parent, false));
            case TYPE_CONTENT:
                return new TitleSummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_text, parent, false));
            case TYPE_MAPS:
                return new EventMapViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_voice, parent, false));
            case TYPE_URL:
                return new TitleSummaryButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_action, parent, false));
        }
        return null;
    }
}
