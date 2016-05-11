package rikka.akashitoolkit.adapter;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.widget.ExpandableLayout;

/**
 * Created by Rikka on 2016/3/12.
 */
public class ViewHolder {
    public static class Quest extends RecyclerView.ViewHolder {
        protected ExpandableLayout mExpandableLayout;
        protected TextView mName;
        protected TextView mDetail;
        protected TextView mNote;
        protected TextView mRequire;
        protected TextView mRewardText[] = new TextView[5];
        protected TextView mType[] = new TextView[2];
        protected LinearLayout mQuestContainer;


        public Quest(View itemView) {
            super(itemView);

            mExpandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);

            mType[0] = (TextView) itemView.findViewById(R.id.text_quest_type);
            mType[1] = (TextView) itemView.findViewById(R.id.text_quest_type2);
            mName = (TextView) itemView.findViewById(R.id.text_card_title);

            mDetail = (TextView) itemView.findViewById(R.id.text_quest_detail);
            mNote = (TextView) itemView.findViewById(R.id.text_quest_note);
            mRequire = (TextView) itemView.findViewById(R.id.text_quest_require);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mExpandableLayout.isExpanded()) {
                        mExpandableLayout.setExpanded(false);
                        mName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    } else {
                        mExpandableLayout.setExpanded(true);
                        mName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                }
            });
        }
    }

    public static class Expedition extends RecyclerView.ViewHolder {
        protected CardView mCardView;
        protected TextView mName;
        protected TextView mRequire;
        protected TextView mConsumeText[];
        protected TextView mRewardText[];

        public Expedition(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            mName = (TextView) itemView.findViewById(R.id.text_card_title);
            mRequire = (TextView) itemView.findViewById(R.id.text_quest_require);
            mRewardText = new TextView[5];
            mRewardText[0] = (TextView) itemView.findViewById(R.id.include_expedition_reward).findViewById(R.id.text_number_0);
            mRewardText[1] = (TextView) itemView.findViewById(R.id.include_expedition_reward).findViewById(R.id.text_number_1);
            mRewardText[2] = (TextView) itemView.findViewById(R.id.include_expedition_reward).findViewById(R.id.text_number_2);
            mRewardText[3] = (TextView) itemView.findViewById(R.id.include_expedition_reward).findViewById(R.id.text_number_3);
            mRewardText[4] = (TextView) itemView.findViewById(R.id.include_expedition_reward).findViewById(R.id.text_quest_reward_4);
            mConsumeText = new TextView[5];
            mConsumeText[0] = (TextView) itemView.findViewById(R.id.include_expedition_consume).findViewById(R.id.text_number_0);
            mConsumeText[1] = (TextView) itemView.findViewById(R.id.include_expedition_consume).findViewById(R.id.text_number_1);
            mConsumeText[2] = (TextView) itemView.findViewById(R.id.include_expedition_consume).findViewById(R.id.text_number_2);
            mConsumeText[3] = (TextView) itemView.findViewById(R.id.include_expedition_consume).findViewById(R.id.text_number_3);
            mConsumeText[4] = (TextView) itemView.findViewById(R.id.include_expedition_consume).findViewById(R.id.text_quest_reward_4);
        }
    }

    public static class ItemImprovement extends RecyclerView.ViewHolder {
        //protected CardView mCardView;
        protected TextView mName;
        protected TextView mType;
        protected TextView mShip;
        protected ImageView mImageView;

        public ItemImprovement(View itemView) {
            super(itemView);

            //mCardView = (CardView) itemView.findViewById(R.id.cardView);
            mName = (TextView) itemView.findViewById(R.id.text_card_title);
            mType = (TextView) itemView.findViewById(R.id.text_card_item_improve_type);
            mShip = (TextView) itemView.findViewById(R.id.text_card_item_improve_ship);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public static class Item extends RecyclerView.ViewHolder {
        protected LinearLayout mLinearLayout;
        protected View mDivider;
        protected View mDummyView;
        protected View mDummyView2;
        protected TextView mName;
        protected TextView mTitle;
        protected ImageView mImageView;

        public Item(View itemView) {
            super(itemView);

            mDivider = itemView.findViewById(R.id.divider);
            mDummyView = itemView.findViewById(R.id.dummy_view);
            mDummyView2 = itemView.findViewById(R.id.dummy_view2);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mName = (TextView) itemView.findViewById(R.id.textView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public static class Ship extends RecyclerView.ViewHolder {
        protected LinearLayout mLinearLayout;
        protected View mDivider;
        protected View mDummyView;
        protected View mDummyView2;
        protected TextView mName;
        protected TextView mName2;
        protected TextView mTitle;

        public Ship(View itemView) {
            super(itemView);

            mDivider = itemView.findViewById(R.id.divider);
            mDummyView = itemView.findViewById(R.id.dummy_view);
            mDummyView2 = itemView.findViewById(R.id.dummy_view2);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mName = (TextView) itemView.findViewById(R.id.textView);
            mName2 = (TextView) itemView.findViewById(R.id.textView2);
        }
    }

    public static class Map extends RecyclerView.ViewHolder {
        //protected ExpandableLinearLayout mLinearLayout;
        protected ExpandableLayout mDetailContainer;
        protected TextView mTitle;
        protected TextView mTextView;
        protected ImageView mImageView;

        public Map(View itemView) {
            super(itemView);

            //mLinearLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandableLinearLayout);
            mDetailContainer = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);

            /*mDetailContainer.post(new Runnable() {
                @Override
                public void run() {
                    //mDetailContainer.setExpanded(false);
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*mTitle.setTypeface(
                            mDetailContainer.getVisibility() == View.GONE ?
                                    Typeface.defaultFromStyle(Typeface.BOLD) :
                                    Typeface.defaultFromStyle(Typeface.NORMAL));*/

                    if (mDetailContainer.isExpanded()) {
                        mDetailContainer.setExpanded(false);
                        mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        //mDetailContainer.moveChild(0);
                    } else {
                        mDetailContainer.setExpanded(true);
                        mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        //mDetailContainer.moveChild(1);
                    }

                    //mDetailContainer.toggle();
                    //mDetailContainer.setVisibility(
                    //       mDetailContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}
