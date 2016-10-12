package rikka.akashitoolkit.fleet_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseItemTouchHelperAdapter;
import rikka.akashitoolkit.gallery.GalleryAdapter;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.ui.widget.EditTextAlertDialog;
import rikka.akashitoolkit.ui.widget.ListBottomSheetDialog;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/7/20.
 */
public class FleetsListAdapter extends BaseItemTouchHelperAdapter<FleetListViewHolder, Fleet> {

    @Override
    public FleetListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fleet, parent, false);
        return new FleetListViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final FleetListViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Fleet item = getItem(position);

        holder.mTitle.setText(item.getName());

        List<String> urls = new ArrayList<>();
        for (Fleet.Ship s : item.getShips()) {
            Ship ship = ShipList.findItemById(context, s.getId());
            if (ship != null) {
                urls.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sBanner.jpg", ship.getWikiId())));
            }
        }

        if (urls.size() == 0) {
            holder.mSummary.setText(context.getString(R.string.fleet_no_ship));
            holder.mRecyclerView.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(context.getString(R.string.fleet_total_level), item.getLevel())).append('\n');
            sb.append(context.getString(item.isLowSpeed() ? R.string.fleet_low_speed : R.string.fleet_high_speed)).append('\n');
            if (item.getAA()[0] > 0) {
                sb.append(String.format("%s %.2f ~ %.2f\n", context.getString(R.string.fleet_fp), item.getAA()[0], item.getAA()[1]));
            } else {
                sb.append(String.format("%s 0\n", context.getString(R.string.fleet_fp)));

            }
            sb.append(String.format(context.getString(R.string.fleet_consume), item.getFuel(), item.getAmmo()));

            // TODO make it more good looking
            if (FlavorsUtils.shouldSafeCheck()) {
                sb.append("\n\n");
                for (Fleet.Ship s : item.getShips()) {
                    Ship ship = ShipList.findItemById(s.getId());
                    if (ship != null) {
                        sb.append(ship.getName().get(false)).append(" Lv.").append(s.getLevel()).append("\n");
                    }
                }
            }

            holder.mSummary.setText(sb.toString().trim());
            holder.mRecyclerView.setVisibility(View.VISIBLE);
        }

        if (FlavorsUtils.shouldSafeCheck()) {
            holder.mRecyclerView.setVisibility(View.GONE);
        }

        ((GalleryAdapter) holder.mRecyclerView.getAdapter()).setUrls(urls);
        holder.setImageSize();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Context context = view.getContext();
                context.startActivity(new Intent(context, FleetEditActivity.class));*/
                FleetEditActivity.start(view.getContext(), holder.getAdapterPosition());
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                ListBottomSheetDialog dialog = new ListBottomSheetDialog(context);
                dialog.setItems(new int[]{
                        R.string.fleet_rename,
                        //R.string.fleet_export,
                        R.string.fleet_delete
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int position = holder.getAdapterPosition();
                        switch (i) {
                            case 0:
                                new EditTextAlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
                                        .setTitle(R.string.fleet_rename)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setNegativeButton(android.R.string.cancel, null)
                                        .setEditText(R.layout.dialog_edittext, getItem(position).getName(), new EditTextAlertDialog.OnPositiveButtonClickListener() {
                                            @Override
                                            public void onPositiveButtonClickListener(DialogInterface dialog, String text) {
                                                if (!TextUtils.isEmpty(text)) {
                                                    getItem(position).setName(text);
                                                    holder.mTitle.setText(text);
                                                }
                                            }
                                        })
                                        .show();
                                break;
                            /*case 1:
                                break;*/
                            case 1:
                                getItemList().remove(position);
                                notifyItemRemoved(position);
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
