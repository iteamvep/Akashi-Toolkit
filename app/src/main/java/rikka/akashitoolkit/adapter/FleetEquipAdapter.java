package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.viewholder.FleetEquipViewHolder;
import rikka.akashitoolkit.viewholder.FleetViewHolder;

/**
 * Created by Rikka on 2016/7/29.
 */
public class FleetEquipAdapter extends BaseItemTouchHelperAdapter<FleetEquipViewHolder, Fleet.Ship.Equip> {

    private FleetAdapter mFleetAdapter;
    private FleetViewHolder mFleetViewHolder;
    private Ship.EquipEntity mEquipEntity;
    private int mPosition;

    public void setFleetAdapter(FleetAdapter fleetAdapter) {
        mFleetAdapter = fleetAdapter;
    }

    public void setFleetViewHolder(FleetViewHolder fleetViewHolder) {
        mFleetViewHolder = fleetViewHolder;
    }

    public void setEquipEntity(Ship.EquipEntity equipEntity) {
        mEquipEntity = equipEntity;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    @Override
    public FleetEquipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return FleetEquipViewHolder.create(parent, R.layout.item_fleet_ship_equip);
            case 1:
                return FleetEquipViewHolder.create(parent, R.layout.item_fleet_ship_equip_empty);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FleetEquipViewHolder holder, final int position) {
        if (mEquipEntity == null) {
            return;
        }

        Context context = holder.itemView.getContext();

        holder.mSummary.setText(Integer.toString(mEquipEntity.getSpace()[position]));

        if (getItemViewType(position) == 0) {
            Fleet.Ship.Equip e = getItem(position);

            if (e != null) {
                rikka.akashitoolkit.model.Equip equip = EquipList.findItemById(context, e.getId());

                if (equip != null) {
                    holder.mTitle.setText(equip.getName().get(context));
                    EquipTypeList.setIntoImageView(holder.mIcon, equip.getIcon());

                    holder.mSummary.setEnabled(equip.isAircraft());
                }
            }

            /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getAdapterPosition();
                    getItem(position).setId(0);
                    notifyItemChanged(position);
                    return true;
                }
            });*/
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.instance().post(new ItemSelectAction.StartEquip(mPosition, holder.getAdapterPosition()));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setViewHolderSlot(FleetEquipViewHolder holder, int position) {
        if (mEquipEntity == null) {
            return;
        }

        holder.mSummary.setText(Integer.toString(mEquipEntity.getSpace()[position]));
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null) {
            return 1;
        }
        return getItem(position).getId() == 0 ? 1 : 0;
    }

    @Override
    public void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPosition, int toPosition) {
        super.onItemMove(recyclerView, viewHolder, target, fromPosition, toPosition);

        setViewHolderSlot((FleetEquipViewHolder) viewHolder, toPosition);
        setViewHolderSlot((FleetEquipViewHolder) target, fromPosition);

        if (mFleetAdapter != null && mFleetViewHolder != null) {
            mFleetAdapter.resetEquipRelatedText(mFleetViewHolder, mPosition);
        }
    }
}
