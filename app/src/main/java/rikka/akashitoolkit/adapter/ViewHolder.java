package rikka.akashitoolkit.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/3/12.
 */
public class ViewHolder {
    public static class Quest extends RecyclerView.ViewHolder {
        protected CardView mCardView;
        protected TextView mName;
        protected TextView mDetail;
        protected TextView mRequire;
        protected TextView mRewardText[];

        public Quest(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            mName = (TextView) itemView.findViewById(R.id.text_quest_name);
            mDetail = (TextView) itemView.findViewById(R.id.text_quest_detail);
            mRequire = (TextView) itemView.findViewById(R.id.text_quest_require);
            mRewardText = new TextView[5];
            mRewardText[0] = (TextView) itemView.findViewById(R.id.text_quest_reward_0);
            mRewardText[1] = (TextView) itemView.findViewById(R.id.text_quest_reward_1);
            mRewardText[2] = (TextView) itemView.findViewById(R.id.text_quest_reward_2);
            mRewardText[3] = (TextView) itemView.findViewById(R.id.text_quest_reward_3);
            mRewardText[4] = (TextView) itemView.findViewById(R.id.text_quest_reward_4);
        }
    }
}
