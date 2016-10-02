package rikka.akashitoolkit.fleet_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseItemTouchHelperAdapter;
import rikka.akashitoolkit.model.AttributeEntity;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.ship.ShipDetailActivity;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.ui.widget.EditTextAlertDialog;
import rikka.akashitoolkit.ui.widget.ListBottomSheetDialog;
import rikka.akashitoolkit.utils.KCStringFormatter;

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
        holder.mContent.setVisibility(View.GONE);
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
    private void bindViewHolder(final FleetViewHolder holder, int position, Fleet.Ship item) {
        Context context = holder.itemView.getContext();

        holder.mContent.setVisibility(View.VISIBLE);
        holder.mButton.setVisibility(View.VISIBLE);
        holder.mRecyclerView.setVisibility(View.VISIBLE);

        holder.swipe = false;
        holder.drag = true;

        Ship ship = ShipList.findItemById(context, item.getId());

        holder.mTitle.setText(String.format("%s Lv.%d", ship.getName().get(), item.getLevel()));

        holder.mSummary.setText(String.format("%s %s",
                KCStringFormatter.getSpeed(context, ship.getAttribute().getSpeed()),
                ship.getShipType().getName().get()));

        resetEquipRelatedText(holder, position);

        holder.mAdapter.setItemList(item.getEquips());
        holder.mAdapter.setEquipEntity(ship.getEquip());
        holder.mAdapter.setPosition(position);
        holder.mAdapter.setFleetViewHolder(holder);
        holder.mAdapter.setFleetAdapter(this);

        Log.d(TAG, item.getEquips().toString());

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(holder, holder.getAdapterPosition());
            }
        });
    }

    private void showDialog(final FleetViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        ListBottomSheetDialog dialog = new ListBottomSheetDialog(context);
        dialog.setItems(new int[]{
                R.string.fleet_view_ship,
                R.string.fleet_change_level,
                R.string.fleet_change_ship,
                R.string.fleet_delete_ship
        }, new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Context context = holder.itemView.getContext();
                switch (i) {
                    case 0:
                        Intent intent = new Intent(context, ShipDetailActivity.class);
                        intent.putExtra(ShipDetailActivity.EXTRA_ITEM_ID, getItem(position).getId());
                        ShipDetailActivity.start(context, intent);
                        break;
                    case 1:
                        new EditTextAlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
                                .setTitle(R.string.fleet_change_level)
                                .setPositiveButton(android.R.string.ok, null)
                                .setNegativeButton(android.R.string.cancel, null)
                                .setEditText(
                                        R.layout.dialog_edittext,
                                        Integer.toString(getItem(position).getLevel()),
                                        InputType.TYPE_CLASS_NUMBER,
                                        new EditTextAlertDialog.OnPositiveButtonClickListener() {
                                            @Override
                                            public void onPositiveButtonClickListener(DialogInterface dialog, String text) {
                                                int value = 0;
                                                try {
                                                    value = Integer.parseInt(text);
                                                } catch (Exception ignored) {
                                                }
                                                value = value < 1 ? 1 : value;
                                                value = value > 155 ? 155 : value;

                                                getItem(position).setLevel(value);

                                                resetLevelRelatedText(holder, position);
                                            }
                                        })
                                .show();
                        break;
                    case 2:
                        BusProvider.instance().post(new ItemSelectAction.StartShip(holder.getAdapterPosition()));
                        break;
                    case 3:
                        getItemList().remove(position);
                        notifyItemRemoved(position);
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    public void resetLevelRelatedText(FleetViewHolder holder, int position) {
        Fleet.Ship item = getItem(position);
        if (item == null) {
            return;
        }

        item.calc();

        Context context = holder.itemView.getContext();

        Ship ship = ShipList.findItemById(context, item.getId());

        holder.mTitle.setText(String.format("%s Lv.%d", ship.getName().get(), item.getLevel()));

    }

    @SuppressLint("DefaultLocale")
    public void resetEquipRelatedText(FleetViewHolder holder, int position) {
        Fleet.Ship item = getItem(position);
        if (item == null) {
            return;
        }

        item.calc();

        Context context = holder.itemView.getContext();

        Ship ship = ShipList.findItemById(context, item.getId());

        AttributeEntity attr = item.getAttr();

        StringBuilder sb = new StringBuilder();
        appendAttrString(context, sb, R.string.attr_firepower, attr.getFirepower());
        if (ship.getType() != 5             /* 重巡洋舰 */
                && ship.getType() != 8      /* 高速战舰 */
                && ship.getType() != 9)     /* 战舰 */ {
            appendAttrString(context, sb, R.string.attr_boom, attr.getBombing());
        }
        appendAttrString(context, sb, R.string.attr_torpedo, attr.getTorpedo());
        appendAttrString(context, sb, R.string.attr_aa, attr.getAA());
        if (ship.getType() != 5             /* 重巡洋舰 */
                && ship.getType() != 8      /* 高速战舰 */
                && ship.getType() != 9      /* 战舰 */
                && ship.getType() != 11     /* 正规空母 */
                && ship.getType() != 18)    /* 装甲空母 */ {
            appendAttrString(context, sb, R.string.attr_asw, attr.getASW());
        }

        sb.delete(sb.length() - 3, sb.length() - 1);

        if (item.getAA()[0] > 0) {
            sb.append('\n');
            sb.append(String.format("%s %.2f ~ %.2f", context.getString(R.string.fleet_fp), item.getAA()[0], item.getAA()[1]));
        }

        holder.mContent.setText(sb.toString());
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
