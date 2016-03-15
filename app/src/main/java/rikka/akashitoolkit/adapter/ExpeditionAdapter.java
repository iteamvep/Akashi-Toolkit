package rikka.akashitoolkit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.staticdata.ExpeditionList;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionAdapter extends RecyclerView.Adapter<ViewHolder.Expedition> {
    private List<ExpeditionList.Expedition> mData;

    public ExpeditionAdapter(Context context, int type) {
        List<ExpeditionList.Expedition> data = ExpeditionList.get(context);
        mData = new ArrayList<>();

        for (ExpeditionList.Expedition item :
                data) {
            if (item.getType() == type) {
                mData.add(item);
            }
        }
    }

    @Override
    public ViewHolder.Expedition onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expedition, parent, false);
        return new ViewHolder.Expedition(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder.Expedition holder, int position) {
        holder.mName.setText(mData.get(position).getName());
        holder.mRequire.setText(formatRequire(
                holder.mRequire.getContext(),
                mData.get(position).getRequire(),
                mData.get(position).getFlagShipLevel(),
                mData.get(position).getTotalShipLevel()));

        setCostText(holder, position, 0);
        setCostText(holder, position, 1);
        ((LinearLayout)holder.mConsumeText[2].getParent()).setVisibility(View.GONE);
        ((LinearLayout)holder.mConsumeText[3].getParent()).setVisibility(View.GONE);
        holder.mConsumeText[4].setText(formatTime(mData.get(position).getTime()));

        setRewardText(holder, position, 0);
        setRewardText(holder, position, 1);
        setRewardText(holder, position, 2);
        setRewardText(holder, position, 3);

        holder.mRewardText[4].setVisibility(View.GONE);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private String formatRequire(Context context, String base, String flagShip, String total) {
        StringBuilder sb = new StringBuilder();
        sb.append(base);

        if (flagShip != null && !flagShip.equals("0")) {
            sb.append('\n').append(String.format(context.getString(R.string.expedition_flagship_format), flagShip));
        }

        if (total != null && !total.equals("0")) {
            sb.append('\n').append(String.format(context.getString(R.string.expedition_flagship_total), total));
        }

        return sb.toString();
    }

    private String formatTime(int time) {
        if (time > 3600) {
            return String.format("%d:%02d:%d:00", time / 3600, time % 3600 / 60, time % 60);
        } else if (time > 60) {
            return String.format("%d:%02d:00", time / 60, time % 60);
        } else {
            return String.format("%d:00", time);
        }
    }

    private void setCostText(ViewHolder.Expedition holder, int position, int id) {
        String text = mData.get(position).getCost(id);
        if (text != null &&text.length() > 0) {
            ((LinearLayout)holder.mConsumeText[id].getParent()).setVisibility(View.VISIBLE);
            holder.mConsumeText[id].setText(text + "%");
        } else {
            ((LinearLayout)holder.mConsumeText[id].getParent()).setVisibility(View.GONE);
        }
    }

    private void setRewardText(ViewHolder.Expedition holder, int position, int id) {
        String text = mData.get(position).getAward(id);
        if (text != null &&text.length() > 0) {
            ((LinearLayout)holder.mRewardText[id].getParent()).setVisibility(View.VISIBLE);
            holder.mRewardText[id].setText(text);
        } else {
            ((LinearLayout)holder.mRewardText[id].getParent()).setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

