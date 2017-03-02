package rikka.akashitoolkit.quest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.model.Quest;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.QuestAction;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.staticdata.QuestList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.ShipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.widget.ExpandableLayout;
import rikka.akashitoolkit.utils.LocaleUtils;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/8/7.
 */
public class QuestViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Quest> {

    public ExpandableLayout mExpandableLayout;
    public TextView mName;
    public TextView mDetail;
    public TextView mNote;
    public TextView mRewardText[] = new TextView[5];
    public TextView mType[] = new TextView[2];
    public LinearLayout mQuestContainer;

    private QuestAdapter mAdapter;

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
    }

    public void setAdapter(QuestAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void bind(final Quest data, int position) {
        Context context = itemView.getContext();

        if (!data.isHighlight()) {
            //holder.mExpandableLayout.setExpanded(false, false);
            mName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else {
            setViewDetail(context, data);
            mExpandableLayout.setExpanded(true, false);
            mName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        mName.setText(
                String.format(data.isBookmarked() ? "%s %s ★" : "%s %s",
                        data.getCode(),
                        data.getTitle().get(true)));

        mType[0].setVisibility(View.INVISIBLE);
        mType[1].setVisibility(View.INVISIBLE);

        int viewType = 0;

        if (data.getPeriod() > 0) {
            mType[0].setVisibility(View.VISIBLE);
            mType[0].setText(PERIOD[data.getPeriod()]);
            viewType++;
        }

        if (data.isNewMission()) {
            mType[viewType].setVisibility(View.VISIBLE);
            mType[viewType].setText(context.getString(R.string.quest_new));
        }

        if (!LocaleUtils.isDataLanguageJapanese(context) && data.getNote() != null && data.getNote().length() > 0) {
            mNote.setVisibility(View.VISIBLE);

            Spanned htmlDescription = Html.fromHtml(data.getNote());
            String descriptionWithOutExtraSpace = htmlDescription.toString().trim();
            mNote.setText(descriptionWithOutExtraSpace);
        } else {
            mNote.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandableLayout.isExpanded()) {
                    mExpandableLayout.setExpanded(false);
                    mName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                } else {
                    setViewDetail(v.getContext(), data);
                    mExpandableLayout.setExpanded(true);
                    mName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onLongClick(View v) {
                data.setBookmarked(!data.isBookmarked());

                Settings.instance(v.getContext())
                        .putBoolean(String.format("quest_%d", data.getId()), data.isBookmarked());

                mAdapter.showToast(v.getContext(), data.isBookmarked());

                mAdapter.notifyItemChanged(getAdapterPosition());

                return true;
            }
        });
    }

    private static final String[] PERIOD = {"", "日", "周", "月"};

    private void setViewDetail(Context context, Quest data) {

        mDetail.setText(Html.fromHtml(data.getContent().get()));

        setRewardText(data, 0);
        setRewardText(data, 1);
        setRewardText(data, 2);
        setRewardText(data, 3);

        setRewardText(data, mRewardText[4]);

        mQuestContainer.removeAllViews();

        if (data.getUnlock() != null && data.getUnlock().size() > 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_quest_next_header, mQuestContainer, false);
            ((TextView) view).setText(context.getString(R.string.quest_before));
            mQuestContainer.addView(view);
            addUnlockQuestViews(context, data.getUnlock(), "%s %s");
        }

        if (data.getAfter() != null && data.getAfter().size() > 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_quest_next_header, mQuestContainer, false);
            ((TextView) view).setText(context.getString(R.string.quest_after));
            mQuestContainer.addView(view);
            addUnlockQuestViews(context, data.getAfter(), "%s %s");
        }
    }

    private void addUnlockQuestViews(Context context, List<String> ids, String format) {
        for (String code : ids) {
            if (code.length() == 0) {
                continue;
            }

            final Quest unlockQuest = QuestList.findItemByCode(context, code);
            if (unlockQuest == null) {
                Log.d("QuestAdapter", "Quest not found: " + code);
                continue;
            }

            View view = LayoutInflater.from(context).inflate(R.layout.card_quest_next, mQuestContainer, false);
            ((TextView) view).setText(
                    String.format(format, unlockQuest.getCode(), unlockQuest.getTitle().get(true)));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Quest q : QuestList.get(v.getContext())) {
                        q.setHighlight(false);
                    }

                    unlockQuest.setHighlight(true);
                    BusProvider.instance().post(new QuestAction.JumpToQuest(unlockQuest.getType(), unlockQuest.getId()));
                }
            });

            mQuestContainer.addView(view);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setRewardText(Quest data, int id) {
        int res = data.getReward().getResource().get(id);
        if (res > 0) {
            ((LinearLayout) mRewardText[id].getParent()).setVisibility(View.VISIBLE);
            mRewardText[id].setText(Integer.toString(res));
        } else {
            ((LinearLayout) mRewardText[id].getParent()).setVisibility(View.GONE);
        }
    }

    private void setRewardText(Quest quest, TextView textView) {
        StringBuilder sb = new StringBuilder();

        for (Integer shipId :
                quest.getReward().getShip()) {
            Ship ship = ShipList.findItemById(shipId);
            if (ship != null) {
                sb.append(ShipTypeList.findItemById(ship.getType()).getName().get()).append(" ").append(ship.getName().get()).append("<br>");
            }
        }

        boolean isNumber = false;
        for (Integer equipId :
                quest.getReward().getEquip()) {
            if (!isNumber) {
                Equip equip = EquipList.findItemById(equipId);
                if (equip != null) {
                    sb.append(equip.getName().get());
                }
            } else {
                sb.append(" * ").append(equipId).append("<br>");
            }
            isNumber = !isNumber;
        }

        isNumber = false;
        for (Integer itemId :
                quest.getReward().getItem()) {
            if (!isNumber) {
                Item item = ItemList.findItemById(itemId);
                if (item != null) {
                    sb.append(item.getName().get());
                }
            } else {
                sb.append(" * ").append(itemId).append("<br>");
            }
            isNumber = !isNumber;
        }

        if (quest.getReward().getStr().length() > 0) {
            sb.append(quest.getReward().getStr());
        }

        if (sb.length() >= 4) {
            sb.delete(sb.length() - 4, sb.length());
        }

        if (sb.length() > 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(Html.fromHtml(sb.toString()));
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
