package rikka.akashitoolkit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.ui.ItemDisplayActivity;

/**
 * Created by Rikka on 2016/3/23.
 */
public class ItemAdapter extends RecyclerView.Adapter<ViewHolder.Item> {
    private List<Item> mData;

    public ItemAdapter(Context context) {
        mData = ItemList.get(context);
    }

    @Override
    public ViewHolder.Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder.Item(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder.Item holder, int position) {
        holder.mName.setText(mData.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), ItemDisplayActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
