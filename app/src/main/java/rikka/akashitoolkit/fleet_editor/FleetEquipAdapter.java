package rikka.akashitoolkit.fleet_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseItemTouchHelperAdapter;
import rikka.akashitoolkit.equip.EquipDetailActivity;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.ui.widget.ListBottomSheetDialog;

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
    public void onBindViewHolder(final FleetEquipViewHolder holder, int position) {
        if (mEquipEntity == null) {
            return;
        }

        resetViewHolderSlot(holder, position);
        resetEquipRelatedText(holder, position);

        if (getItemViewType(position) == 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BusProvider.instance().post(new ItemSelectAction.StartEquip(mPosition, holder.getAdapterPosition()));
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    showEditEquipDialog(context, holder, holder.getAdapterPosition());
                }
            });
        }
    }

    private void showEditEquipDialog(final Context context, final FleetEquipViewHolder holder, int position) {
        ListBottomSheetDialog dialog = new ListBottomSheetDialog(context);
        if (getItem(position).getEquip().isImprovable() || getItem(position).getEquip().isRankupable()) {
            dialog.setItems(
                    new CharSequence[]{
                            context.getString(R.string.fleet_view_equip),
                            context.getString(R.string.fleet_change_equip_attr),
                            context.getString(R.string.fleet_change_equip),
                            context.getString(R.string.fleet_delete_equip)},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position = holder.getAdapterPosition();
                            switch (i) {
                                case 0:
                                    context.startActivity(EquipDetailActivity.intent(context, getItem(position).getId()));
                                    break;
                                case 1:
                                    showEditAttrDialog(context, holder, holder.getAdapterPosition());
                                    break;
                                case 2:
                                    BusProvider.instance().post(new ItemSelectAction.StartEquip(mPosition, position));
                                    break;
                                case 3:
                                    removeEquip(position);
                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    });
            dialog.show();
        } else {
            dialog.setItems(
                    new CharSequence[]{
                            context.getString(R.string.fleet_view_equip),
                            context.getString(R.string.fleet_change_equip),
                            context.getString(R.string.fleet_delete_equip)},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position = holder.getAdapterPosition();
                            switch (i) {
                                case 0:
                                    context.startActivity(EquipDetailActivity.intent(context, getItem(position).getId()));
                                    break;
                                case 1:
                                    BusProvider.instance().post(new ItemSelectAction.StartEquip(mPosition, position));
                                    break;
                                case 2:
                                    removeEquip(position);
                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    });
            dialog.show();
        }
    }

    @SuppressLint("DefaultLocale")
    private void showEditAttrDialog(Context context, final FleetEquipViewHolder holder, int position) {
        EquipAttributeDialog dialog = new EquipAttributeDialog(
                context,
                getItem(position).getStar(),
                getItem(position).getRank(),
                getItem(position).getEquip().isImprovable(),
                getItem(position).getEquip().isRankupable()
        );
        dialog.setListener(new EquipAttributeDialog.Listener() {
            @Override
            public void onImprovementChanged(int i) {
                int position = holder.getAdapterPosition();
                getItem(position).setStar(i);
                resetParent();
                resetEquipRelatedText(holder, position);
            }

            @Override
            public void onRankChanged(int i) {
                int position = holder.getAdapterPosition();
                getItem(position).setRank(i);
                resetParent();
                resetEquipRelatedText(holder, position);
            }
        });
        dialog.show();
    }

    private void removeEquip(int position) {
        if (getItem(position) != null) {
            getItem(position).setId(0);
            getItem(position).setRank(0);
            getItem(position).setStar(0);
            notifyItemChanged(position);
        }
    }

    @SuppressLint("SetTextI18n")
    private void resetViewHolderSlot(FleetEquipViewHolder holder, int position) {
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

        resetViewHolderSlot((FleetEquipViewHolder) viewHolder, toPosition);
        resetViewHolderSlot((FleetEquipViewHolder) target, fromPosition);

        resetParent();
    }

    private void resetParent() {
        if (mFleetAdapter != null && mFleetViewHolder != null) {
            mFleetAdapter.resetEquipRelatedText(mFleetViewHolder, mPosition);
        }
    }

    private void resetEquipRelatedText(FleetEquipViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if (getItemViewType(position) == 0) {
            Fleet.Ship.Equip e = getItem(position);

            if (e != null) {
                rikka.akashitoolkit.model.Equip equip = e.getEquip();

                if (equip != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(equip.getName().get());
                    if (e.getStar() > 0) {
                        sb.append(" ★+").append(e.getStar());
                    }
                    if (e.getRank() > 0) {
                        sb.append(" ").append(Fleet.equipRank[e.getRank()]);
                    }
                    holder.mTitle.setText(sb.toString());
                    EquipTypeList.setIntoImageView(holder.mIcon, equip.getIcon());

                    holder.mSummary.setEnabled(equip.isAircraft());
                }
            }
        }
    }
}
