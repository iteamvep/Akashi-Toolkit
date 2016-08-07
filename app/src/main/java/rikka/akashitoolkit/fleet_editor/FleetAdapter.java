package rikka.akashitoolkit.fleet_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseItemTouchHelperAdapter;
import rikka.akashitoolkit.model.AttrEntity;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.viewholder.FleetViewHolder;

/**
 * Created by Rikka on 2016/7/29.
 */
public class FleetAdapter extends BaseItemTouchHelperAdapter<FleetViewHolder, Fleet.Ship> {

    private static final String TAG = "FleetAdapter";

    @Override
    public FleetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FleetViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fleet_ship, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(FleetViewHolder holder, int position) {
        Fleet.Ship item = getItem(position);

        if (item == null) {
            bindViewHolderEmpty(holder, position);
        } else {
            bindViewHolder(holder, position, item);
        }
    }

    private void bindViewHolderEmpty(final FleetViewHolder holder, int position) {
        holder.mSummary.setVisibility(View.GONE);
        holder.mButton.setVisibility(View.GONE);
        holder.mRecyclerView.setVisibility(View.GONE);
        holder.mTitle.setText(holder.itemView.getContext().getString(R.string.fleet_select_ship));

        holder.swipe = false;
        holder.drag = false;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.instance().post(new ItemSelectAction.StartShip(holder.getAdapterPosition()));
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void bindViewHolder(FleetViewHolder holder, int position, Fleet.Ship item) {
        Context context = holder.itemView.getContext();

        holder.mSummary.setVisibility(View.VISIBLE);
        holder.mButton.setVisibility(View.VISIBLE);
        holder.mRecyclerView.setVisibility(View.VISIBLE);

        holder.swipe = true;
        holder.drag = true;

        Ship ship = ShipList.findItemById(context, item.getId());

        holder.mTitle.setText(String.format("%s Lv.%d", ship.getName().get(context), item.getLevel()));

        resetEquipRelatedText(holder, position);

        holder.mAdapter.setItemList(item.getEquips());
        holder.mAdapter.setEquipEntity(ship.getEquip());
        holder.mAdapter.setPosition(position);
        holder.mAdapter.setFleetViewHolder(holder);
        holder.mAdapter.setFleetAdapter(this);

        Log.d(TAG, item.getEquips().toString());
    }

    @SuppressLint("DefaultLocale")
    public void resetEquipRelatedText(FleetViewHolder holder, int position) {
        Fleet.Ship item = getItem(position);
        if (item == null) {
            return;
        }

        item.calc();

        Context context = holder.itemView.getContext();

        holder.mSummary.setVisibility(View.VISIBLE);
        holder.mButton.setVisibility(View.VISIBLE);
        holder.mRecyclerView.setVisibility(View.VISIBLE);

        holder.swipe = true;
        holder.drag = true;

        Ship ship = ShipList.findItemById(context, item.getId());

        AttrEntity attr = item.getAttr();

        StringBuilder sb = new StringBuilder();
        appendAttrString(context, sb, R.string.attr_firepower, attr.getFirepower());
        appendAttrString(context, sb, R.string.attr_boom, attr.getBombing());
        appendAttrString(context, sb, R.string.attr_torpedo, attr.getTorpedo());
        appendAttrString(context, sb, R.string.attr_aa, attr.getAA());
        if (ship.getType() != 11) {
            appendAttrString(context, sb, R.string.attr_asw, attr.getASW());
        }

        sb.delete(sb.length() - 3, sb.length() - 1);

        if (item.getAA()[0] > 0) {
            sb.append('\n');
            sb.append(String.format("%s %.2f ~ %.2f", context.getString(R.string.fleet_fp), item.getAA()[0], item.getAA()[1]));
        }

        holder.mSummary.setText(sb.toString());
    }

    @Override
    public void onViewRecycled(FleetViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder.mAdapter != null) {
            holder.mAdapter.setFleetViewHolder(null);
            holder.mAdapter.setFleetAdapter(null);
        }
    }

    private void appendAttrString(Context context, StringBuilder sb, @StringRes int stringRes, int value) {
        if (value > 0) {
            sb.append(context.getString(stringRes))
                    .append(" ")
                    .append(value)
                    .append(" · ");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() < 6 ? super.getItemCount() + 1 : super.getItemCount();
    }

    @Override
    public void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPosition, int toPosition) {
        if (toPosition != super.getItemCount()) {
            super.onItemMove(recyclerView, viewHolder, target, fromPosition, toPosition);
        }
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        super.onItemDismiss(viewHolder, direction, position);
    }
}
