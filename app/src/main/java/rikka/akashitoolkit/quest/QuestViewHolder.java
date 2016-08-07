package rikka.akashitoolkit.quest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.widget.ExpandableLayout;

/**
 * Created by Rikka on 2016/8/7.
 */
public class QuestViewHolder extends RecyclerView.ViewHolder {

    public ExpandableLayout mExpandableLayout;
    public TextView mName;
    public TextView mDetail;
    public TextView mNote;
    public TextView mRewardText[] = new TextView[5];
    public TextView mType[] = new TextView[2];
    public LinearLayout mQuestContainer;


    public QuestViewHolder(View itemView) {
        super(itemView);

        mExpandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);

        mType[0] = (TextView) itemView.findViewById(R.id.text_quest_type);
        mType[1] = (TextView) itemView.findViewById(R.id.text_quest_type2);
        mName = (TextView) itemView.findViewById(R.id.text_card_title);

        mDetail = (TextView) itemView.findViewById(R.id.text_quest_detail);
        mNote = (TextView) itemView.findViewById(R.id.text_quest_note);
        mRewardText[0] = (TextView) itemView.findViewById(R.id.text_number_0);
        mRewardText[1] = (TextView) itemView.findViewById(R.id.text_number_1);
        mRewardText[2] = (TextView) itemView.findViewById(R.id.text_number_2);
        mRewardText[3] = (TextView) itemView.findViewById(R.id.text_number_3);
        mRewardText[4] = (TextView) itemView.findViewById(R.id.text_quest_reward_4);
        mQuestContainer = (LinearLayout) itemView.findViewById(R.id.quest_container);

            /*for (TextView textView :
                    mType) {
                if (textView == null) {
                    continue;
                }

                textView.getBackground().setColorFilter(
                        ContextCompat.getColor(itemView.getContext(), R.color.colorAccent),
                        PorterDuff.Mode.SRC_ATOP);
            }*/
    }
}
