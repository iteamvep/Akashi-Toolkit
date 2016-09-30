package rikka.akashitoolkit.event;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.SimpleAdapter;
import rikka.akashitoolkit.ui.widget.BaseRecyclerViewItemDecoration;
import rikka.akashitoolkit.viewholder.IBindViewHolder;
import rikka.akashitoolkit.voice.VoiceActivity;

/**
 * Created by Rikka on 2016/9/28.
 */

public class VoicesViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Event.Voices> {

    public TextView mTitle;
    public TextView mSummary;
    public RecyclerView mRecyclerView;
    public SimpleAdapter<String> mAdapter;

    public VoicesViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mSummary = (TextView) itemView.findViewById(android.R.id.summary);
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.content_container);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(itemView.getContext()) {
            @Override
            public boolean canDraw(RecyclerView parent, View child, int childCount, int position) {
                if (position == parent.getAdapter().getItemCount() - 1) {
                    return false;
                }
                return super.canDraw(parent, child, childCount, position);
            }
        });
        mAdapter = new SimpleAdapter<>(R.layout.list_item);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void bind(final Event.Voices data, int position) {
        final String title = data.getTitle().get();
        mTitle.setText(data.getTitle().get());

        List<String> list = new ArrayList<>();

        int count = 0;
        for (Event.Voices.Voice voice : data.getVoices()) {
            list.add(String.format("%s (%d)", voice.getType(), voice.getVoice().size()));
            count += voice.getVoice().size();
        }

        mSummary.setText(String.format(itemView.getContext().getString(R.string.home_card_voice_summary), count));

        mAdapter.setListener(new SimpleAdapter.Listener() {
            @Override
            public void OnClick(int position) {
                VoiceActivity.start(itemView.getContext(), data.getVoices().get(position).getVoice(), title);
            }
        });
        mAdapter.setItemList(list);
    }
}
