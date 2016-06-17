package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Expedition;
import rikka.akashitoolkit.otto.BookmarkItemChanged;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionAdapter extends BaseBookmarkRecyclerAdapter<ViewHolder.Expedition> {
    private Context mContext;

    private List<Expedition> mData;
    private List<Integer> mFilter;

    public ExpeditionAdapter(Context context, boolean bookmarked) {
        super(bookmarked);

        mData = new ArrayList<>();
        mContext = context;

        rebuildDataList();
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    public void setFilter(List<Integer> filter) {
        mFilter = filter;

        rebuildDataList();
    }

    @Override
    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Expedition> data = ExpeditionList.get(mContext);
                mData = new ArrayList<>();

                for (Expedition item :
                        data) {
                    if (!item.isBookmarked() && requireBookmarked()) {
                        continue;
                    }

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

        holder.mTitle.setText(String.format(item.isBookmarked() ? "%d %s ★" : "%d %s", item.getId(), item.getName().get(context)));
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onLongClick(View view) {
                Expedition item = mData.get(holder.getAdapterPosition());

                item.setBookmarked(!item.isBookmarked());

                Settings.instance(view.getContext())
                        .putBoolean(String.format("expedition_%d", item.getId()), item.isBookmarked());

                showToast(view.getContext(), item.isBookmarked());

                BusProvider.instance().post(new BookmarkItemChanged.Expedition());

                notifyItemChanged(holder.getAdapterPosition());
                return true;
            }
        });
    }

    private void setViewDetail(final ViewHolder.Expedition holder, int position) {
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

