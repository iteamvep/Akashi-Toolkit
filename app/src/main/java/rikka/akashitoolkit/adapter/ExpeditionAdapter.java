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
import rikka.akashitoolkit.model.MapType;
import rikka.akashitoolkit.otto.BookmarkItemChanged;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.staticdata.MapTypeList;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionAdapter extends BaseBookmarkRecyclerAdapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private List<Integer> mFilter;

    public ExpeditionAdapter(Context context, boolean bookmarked) {
        super(bookmarked);

        mContext = context;

        rebuildDataList();
        setHasStableIds(true);
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
                ExpeditionList.get(mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                clearItemList();

                List<Expedition> data = ExpeditionList.get(mContext);

                int type = -1;

                for (Expedition item :
                        data) {
                    if (!item.isBookmarked() && requireBookmarked()) {
                        continue;
                    }

                    if (mFilter == null || mFilter.size() == 0 || mFilter.indexOf(item.getId()) != -1) {
                        if (type != item.getType()) {
                            type = item.getType();
                            addItem(RecyclerView.NO_ID, 1, MapTypeList.get(mContext).get(type).getName(mContext));
                        }

                        addItem(item.getId(), 0, item);
                    }
                }

                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder.Expedition(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expedition, parent, false));
            case 1:
                return new ViewHolder.Subtitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtitle, parent, false));
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                bindViewHolder((ViewHolder.Expedition) holder, position);
                break;
            case 1:
                bindViewHolder((ViewHolder.Subtitle) holder, position);
                break;
        }
    }

    private void bindViewHolder(final ViewHolder.Expedition holder, int position) {
        Context context = holder.itemView.getContext();
        Expedition item = (Expedition) getItemData(position);

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
                Expedition item = (Expedition) getItemData(holder.getAdapterPosition());

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

    private void bindViewHolder(ViewHolder.Subtitle holder, int position) {
        holder.mDivider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.mTitle.setText((String) getItemData(position));
    }

    private void setViewDetail(final ViewHolder.Expedition holder, int position) {
        Expedition item = (Expedition) getItemData(position);

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
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolder.Expedition) {
            ((ViewHolder.Expedition) holder).mExpandableLayout.setExpanded(false, false);
        }
        super.onViewRecycled(holder);
    }
}

