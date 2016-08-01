package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.ui.FleetEditActivity;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.FleetListViewHolder;

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
            holder.mSummary.setText("没有舰娘");
            holder.mRecyclerView.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("总等级 ").append(item.getLevel()).append('\n');
            sb.append(item.isLowSpeed() ? "低速舰队" : "高速舰队").append('\n');
            //sb.append("制空战力 ").append(item.getAA()).append(" (").append(item.getAA() * 1.5).append(") ").append(" · 索敌能力 0").append('\n');
            if (item.getAA()[0] > 0) {
                sb.append(String.format("%s %.2f ~ %.2f\n", context.getString(R.string.fleet_fp), item.getAA()[0], item.getAA()[1]));
            } else {
                sb.append(String.format("%s 0\n", context.getString(R.string.fleet_fp)));

            }
            sb.append("消耗 ").append(item.getFuel()).append(" / ").append(item.getAmmo());

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
    }
}
