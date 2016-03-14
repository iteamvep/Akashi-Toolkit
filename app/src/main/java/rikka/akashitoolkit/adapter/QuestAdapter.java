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
    private int count;
    private boolean[] mExpaned;

    public QuestAdapter(Context context, int type) {
        mType = type;
        List<QuestList.Quest> data = QuestList.get(context);
        mData = new ArrayList<>();

        for (QuestList.Quest item :
                data) {
            if (item.getType() == type) {
                mData.add(item);
            }
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
        holder.mName.setText(String.format("%s %s", mData.get(position).getCode(), mData.get(position).getTitle()));
        holder.mDetail.setText(mData.get(position).getContent());
        holder.mRequire.setText(mData.get(position).getRequire());

        setRewardText(holder, position, 0);
        setRewardText(holder, position, 1);
        setRewardText(holder, position, 2);
        setRewardText(holder, position, 3);

        holder.mRewardText[4].setText(mData.get(position).getAwardOther());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
    public int getItemCount() {
        return mData.size();
    }
}
