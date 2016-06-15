package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Expedition;
import rikka.akashitoolkit.staticdata.ExpeditionList;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionAdapter extends RecyclerView.Adapter<ViewHolder.Expedition> {
    private List<Expedition> mData;
    private List<Integer> mFilter;

    public ExpeditionAdapter(final Context context) {
        mData = new ArrayList<>();

        rebuildDataList(context);
    }

    public void setFilter(List<Integer> filter, Context context) {
        mFilter = filter;

        rebuildDataList(context);
    }

    public void rebuildDataList(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Expedition> data = ExpeditionList.get(context);
                mData = new ArrayList<>();

                for (Expedition item :
                        data) {
                    if (mFilter == null || mFilter.size() == 0 || mFilter.indexOf(item.getId()) != -1) {
                        mData.add(item);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public ViewHolder.Expedition onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expedition, parent, false);
        return new ViewHolder.Expedition(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final ViewHolder.Expedition holder, int position) {
        Context context = holder.itemView.getContext();
        Expedition item = mData.get(position);

        holder.mTitle.setText(String.format("%d %s", item.getId(), item.getName().get(context)));
        holder.mTime.setText(Html.fromHtml(item.getTimeString()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mExpandableLayout.isExpanded()) {
                    holder.mExpandableLayout.setExpanded(false);
                    holder.mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                } else {
                    setViewDetail(holder, holder.getAdapterPosition());
                    holder.mExpandableLayout.setExpanded(true);
                    holder.mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }
        });
    }

    private void setViewDetail(ViewHolder.Expedition holder, int position) {
        Expedition item = mData.get(position);

        for (int i = 0; i < 4; i++) {
            holder.setRewardResource(item.getReward().getResourceString().get(i), i);
        }
        holder.setRewardText(item.getReward().getAward(), item.getReward().getAward2());

        Expedition.RequireEntity require = item.getRequire();
        //holder.mRequireNumber[0].setText(Html.fromHtml(item.getTimeString()));
        holder.setRequireResource(require.getConsumeString().get(0), 1);
        holder.setRequireResource(require.getConsumeString().get(1), 2);

        holder.setFleetRequire(require.getTotalLevel(), require.getFlagshipLevel(), require.getMinShips());
        holder.setShipRequire(require.getEssentialShip(), require.getBucket());
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
        if (time > 60) {
            return String.format("%d:%02d:00", time / 60, time % 60);
        } else {
            return String.format("%d:00", time);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onViewRecycled(ViewHolder.Expedition holder) {
        holder.mExpandableLayout.setExpanded(false, false);
        super.onViewRecycled(holder);
    }
}

