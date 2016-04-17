package rikka.akashitoolkit.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.QuestAction;
import rikka.akashitoolkit.staticdata.QuestList;

/**
 * Created by Rikka on 2016/3/9.
 */
public class QuestAdapter extends RecyclerView.Adapter<ViewHolder.Quest> {
    private static final int[] CARD_BACKGROUND = {
            R.color.questCardBackground0,
            R.color.questCardBackground1,
            R.color.questCardBackground2,
            R.color.questCardBackground3,
            R.color.questCardBackground4,
            R.color.questCardBackground5,
            R.color.questCardBackground6
    };

    private List<QuestList.Quest> mData;
    private int mType;
    private int mFilterFlag;
    private int count;
    private String mKeyword;
    private boolean[] mExpaned;

    public QuestAdapter(Context context, int type) {
        this(context, type, -1);
    }

    public QuestAdapter(Context context, int type, int flag) {
        mType = type;
        mData = new ArrayList<>();
        mFilterFlag = flag;
    }

    public int getPositionByIndex(int index) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getIndex() == index) {
                return i;
            }
        }
        return 0;
    }

    public void rebuildDataList(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<QuestList.Quest> data = QuestList.get(context);
                mData.clear();

                for (QuestList.Quest item :
                        data) {
                    if (check(item)) {
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

    private boolean check(QuestList.Quest item) {
        if (mType == 0 || item.getType() == mType) {
            if (mFilterFlag == 0) {
                return false;
            }

            if (mFilterFlag == -1 || (mFilterFlag & 1 << (item.getPeriod() - 1)) > 0) {
                if (mKeyword == null) {
                    return true;
                }

                if (mKeyword.length() == 0) {
                    return false;
                }

                if (item.getTitle().contains(mKeyword) ||
                        item.getCode().contains(mKeyword.toUpperCase()) ||
                        item.getContent().contains(mKeyword) ||
                        item.getRequire().contains(mKeyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setFilterFlag(Context context, int filterFlag) {
        if (mFilterFlag == filterFlag) {
            return;
        }

        mFilterFlag = filterFlag;
        rebuildDataList(context);
    }

    public void setKeyword(Context context, String keyword) {
        if (mKeyword != null && mKeyword.equals(keyword)) {
            return;
        }

        mKeyword = keyword;
        rebuildDataList(context);
    }

    private void setRewardText(ViewHolder.Quest holder, int position, int id) {
        String text = mData.get(position).getAward(id);
        if (text != null &&text.length() > 0) {
            ((LinearLayout)holder.mRewardText[id].getParent()).setVisibility(View.VISIBLE);
            holder.mRewardText[id].setText(text);
        } else {
            ((LinearLayout)holder.mRewardText[id].getParent()).setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder.Quest onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest, parent, false);
        return new ViewHolder.Quest(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder.Quest holder, int position) {
        //holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(holder.mCardView.getContext(), CARD_BACKGROUND[mType - 1]));
        holder.mName.setText(mData.get(position).getTitle());
        holder.mCode.setText(mData.get(position).getCode());
        holder.mDetail.setText(mData.get(position).getContent());
        //holder.mRequire.setText(mData.get(position).getRequire());

        setRewardText(holder, position, 0);
        setRewardText(holder, position, 1);
        setRewardText(holder, position, 2);
        setRewardText(holder, position, 3);

        holder.mRewardText[4].setText(mData.get(position).getAwardOther());

        if (mData.get(position).getUnlockIndex() > 0) {
            holder.mName2.setVisibility(View.VISIBLE);

            final QuestList.Quest item = QuestList.findItemById(holder.mName2.getContext(), mData.get(position).getUnlockIndex());
            if (item != null) {
                holder.mName2.setText(String.format("前置任务: %s %s", item.getCode(), item.getTitle()));
                /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BusProvider.instance().post(new QuestAction.JumpToQuest(item.getType(), item.getIndex()));
                    }
                });*/
            }
        } else {
            holder.mName2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
