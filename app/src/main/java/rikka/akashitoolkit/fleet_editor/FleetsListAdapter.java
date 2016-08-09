package rikka.akashitoolkit.fleet_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import rikka.akashitoolkit.ui.widget.ListBottomSheetDialog;
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

            holder.mSummary.setText(sb.toString());
            holder.mRecyclerView.setVisibility(View.VISIBLE);
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
                Context context = view.getContext();
                ListBottomSheetDialog dialog = new ListBottomSheetDialog(context);
                dialog.setItems(new int[]{
                        R.string.fleet_rename,
                        R.string.fleet_export
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
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