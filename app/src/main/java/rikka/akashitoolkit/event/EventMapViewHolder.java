package rikka.akashitoolkit.event;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zzhoujay.markdown.MarkDown;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.SimpleAdapter;
import rikka.akashitoolkit.model.Event;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.ui.widget.BaseRecyclerViewItemDecoration;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventMapViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder {

    public TextView mTitle;
    public TextView mSummary;
    public RecyclerView mRecyclerView;
    public SimpleAdapter<String> mAdapter;

    public EventMapViewHolder(View itemView) {
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

    @Override
    public void bind(Object _data, int position) {
        if (!(_data instanceof Event.Maps)) {
            return;
        }

        final Event.Maps data = (Event.Maps) _data;

        if (data.getIds() == null) {
            return;
        }

        mTitle.setText(data.getTitle());
        mSummary.setVisibility(View.GONE);

        List<String> list = new ArrayList<>();
        for (Integer id : data.getIds()) {
            list.add(Integer.toString(id));
        }
        mAdapter.setItemList(list);
        mAdapter.setListener(new SimpleAdapter.Listener() {
            @Override
            public void OnClick(int position) {
                int id = data.getIds().get(position);

                Intent intent = new Intent(itemView.getContext(), EventMapActivity.class);
                intent.putExtra(EventMapActivity.EXTRA_ITEM_ID, id);
                BaseItemDisplayActivity.start(itemView.getContext(), intent);
            }
        });
    }
}
