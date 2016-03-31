package rikka.akashitoolkit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.ui.ItemDisplayActivity;
import rikka.akashitoolkit.ui.ShipDisplayActivity;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipAdapter extends RecyclerView.Adapter<ViewHolder.Ship> {
    private List<Ship> mData;
    private Activity mActivity;

    public ShipAdapter(Activity activity) {
        mData = ShipList.get(activity);
        mActivity = activity;
    }

    @Override
    public ViewHolder.Ship onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ship, parent, false);
        return new ViewHolder.Ship(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.Ship holder, final int position) {
        holder.mName.setText(mData.get(position).getName().getZh_cn());

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShipDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, mData.get(position).getId());

                int[] location = new int[2];
                holder.mLinearLayout.getLocationOnScreen(location);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
