package rikka.akashitoolkit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.ItemImprovement;
import rikka.akashitoolkit.staticdata.ItemImprovementList;
import rikka.akashitoolkit.staticdata.QuestList;

/**
 * Created by Rikka on 2016/3/17.
 */
public class ItemImprovementAdapter extends RecyclerView.Adapter<ViewHolder.ItemImprovement> {
    private List<ItemImprovement> mData;
    private List<String> mDataShip;

    public ItemImprovementAdapter(Context context, int type) {
        List<ItemImprovement> data = ItemImprovementList.get(context);
        mData = new ArrayList<>();
        mDataShip = new ArrayList<>();

        for (ItemImprovement item :
                data) {

            boolean add = false;
            StringBuilder sb = new StringBuilder();
            for (ItemImprovement.SecretaryEntity entity : item.getSecretary()) {
                if (entity.getDay().get(type)) {
                    add = true;
                    sb.append(sb.length() > 0 ? " / " : "" );
                    sb.append(entity.getName());
                }
            }

            if (add) {
                mData.add(item);
                mDataShip.add(sb.toString());
            }

        }
    }

    @Override
    public ViewHolder.ItemImprovement onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_improvement, parent, false);
        return new ViewHolder.ItemImprovement(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder.ItemImprovement holder, int position) {
        holder.mName.setText(mData.get(position).getName());
        holder.mType.setText(mData.get(position).getType());
        holder.mShip.setText("二号舰娘: " + mDataShip.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
