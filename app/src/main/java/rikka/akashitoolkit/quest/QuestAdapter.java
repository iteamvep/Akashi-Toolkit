package rikka.akashitoolkit.quest;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseBookmarkRecyclerAdapter;
import rikka.akashitoolkit.adapter.FilterAdapterHelper;
import rikka.akashitoolkit.model.Quest;
import rikka.akashitoolkit.staticdata.QuestList;

/**
 * Created by Rikka on 2016/3/9.
 */
public class QuestAdapter extends BaseBookmarkRecyclerAdapter<QuestViewHolder, Quest> {

    public static final int FILTER_TYPE = 0;
    public static final int FILTER_PERIOD_FLAG = 1;
    public static final int FILTER_LATEST = 2;

    private FilterAdapterHelper<Quest> mHelper;

    private Context mContext;

    public QuestAdapter(Context context, boolean searching, int type, final int flag, boolean latestOnly, boolean bookmarked) {
        super(bookmarked);

        mHelper = new FilterAdapterHelper<Quest>(this) {
            @Override
            public boolean contains(String key, Quest obj) {
                if (TextUtils.isEmpty(key)) {
                    return false;
                }

                key = key.toLowerCase();
                return (obj.getTitle().get().toLowerCase().contains(key)
                        || obj.getCode().toLowerCase().contains(key)
                        || obj.getContent().get().toLowerCase().contains(key));
            }

            @Override
            public boolean check(int key, int value, Quest obj) {
                if (requireBookmarked() && !obj.isBookmarked()) {
                    return false;
                }

                switch (key) {
                    case FILTER_TYPE:
                        return value == 0 || obj.getType() == value;
                    case FILTER_PERIOD_FLAG:
                        return value == -1 || (value != 0 && (value & 1 << obj.getPeriod()) > 0);
                }
                return true;
            }

            @Override
            public boolean check(int key, boolean value, Quest obj) {
                switch (key) {
                    case FILTER_LATEST:
                        return !value || obj.isNewMission();
                }
                return true;
            }
        };

        mHelper.setSearching(searching);
        mHelper.addKey(FILTER_TYPE, type);
        mHelper.addKey(FILTER_PERIOD_FLAG, flag);
        mHelper.addKey(FILTER_LATEST, latestOnly);
        mContext = context;
        setHasStableIds(true);
        rebuildDataList();
    }

    public int getPositionByIndex(int index) {
        for (int i = 0; i < getItemList().size(); i++) {
            if (getItemList().get(i).getId() == index) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public QuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest_detail_expanded, parent, false);
        }
        return new QuestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuestViewHolder holder, int position) {
        holder.setAdapter(this);

        super.onBindViewHolder(holder, position);
    }

    @Override
    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                clearItemList();

                mHelper.setData(QuestList.get(mContext));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setItemList(mHelper.getFilteredData());
                //notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onViewRecycled(QuestViewHolder holder) {
        super.onViewRecycled(holder);

        holder.setAdapter(null);
        holder.mExpandableLayout.setExpanded(false, false);
    }

    public FilterAdapterHelper<Quest> getHelper() {
        return mHelper;
    }
}
