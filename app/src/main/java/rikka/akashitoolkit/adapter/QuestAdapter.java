package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/3/9.
 */
public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.ViewHolder>{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest, parent, false);
        return new ViewHolder(itemView);
}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText("A1 はじめての「編成」！");
        holder.mDetail.setText("2隻以上の艦で編成される「艦隊」を編成せよ！");
        holder.mReward[0].setText("0");
        holder.mReward[1].setText("0");
        holder.mReward[2].setText("0");
        holder.mReward[3].setText("0");
        holder.mReward[4].setText("并没有其他的");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mName;
        protected TextView mDetail;
        protected TextView mReward[];

        public ViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.text_quest_name);
            mDetail = (TextView) itemView.findViewById(R.id.text_quest_detail);
            mReward = new TextView[5];
            mReward[0] = (TextView) itemView.findViewById(R.id.text_quest_reward_0);
            mReward[1] = (TextView) itemView.findViewById(R.id.text_quest_reward_1);
            mReward[2] = (TextView) itemView.findViewById(R.id.text_quest_reward_2);
            mReward[3] = (TextView) itemView.findViewById(R.id.text_quest_reward_3);
            mReward[4] = (TextView) itemView.findViewById(R.id.text_quest_reward_4);
        }
    }
}
