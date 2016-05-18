package rikka.akashitoolkit.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
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

/**
 * Created by Rikka on 2016/3/9.
 */
public class QuestAdapter extends BaseRecyclerAdapter<ViewHolder.Quest> {
    private List<Quest> mData;
    private int mType;
    private int mFilterFlag;
    private String mKeyword;
    private Context mContext;
    private boolean mIsSearching;
    private boolean mLatestOnly;

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    public QuestAdapter(Context context, int type, int flag, boolean isSearching, boolean latestOnly) {
        mType = type;
        mData = new ArrayList<>();
        mFilterFlag = flag;
        mContext = context;
        mIsSearching = isSearching;
        mLatestOnly = latestOnly;
        setHasStableIds(true);
        rebuildDataList();
    }

    public int getPositionByIndex(int index) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == index) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Quest quest = mData.get(position);
        int type = 0;
        if (quest.getPeriod() > 0) {
            type ++;
        }
        if (quest.isNewMission()) {
            type ++;
        }

        return type;
    }

    private boolean check(Quest item) {
        if (mLatestOnly && !item.isNewMission()) {
            return false;
        }

        if (mType == 0 || item.getType() == mType) {
            if (mFilterFlag == 0) {
                return false;
            }

            if (mFilterFlag == -1 || (mFilterFlag & 1 << item.getPeriod()) > 0) {
                if (!mIsSearching) {
                    return true;
                }

                if (mKeyword == null ||mKeyword.length() == 0) {
                    return false;
                }

                if (item.getTitle().get(mContext).contains(mKeyword) ||
                        item.getCode().contains(mKeyword.toUpperCase()) ||
                        item.getContent().get(mContext).contains(mKeyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSearching() {
        return mIsSearching;
    }

    public void setSearching(boolean searching) {
        mIsSearching = searching;
    }

    public void setFilterFlag(int filterFlag) {
        if (mFilterFlag == filterFlag) {
            return;
        }

        mFilterFlag = filterFlag;
        rebuildDataList();
    }

    public void setKeyword(String keyword) {
        //Log.d(getClass().getSimpleName(), keyword == null ? "null" : keyword);

        if (mKeyword != null && mKeyword.equals(keyword)) {
            return;
        }

        mKeyword = keyword;
        rebuildDataList();
    }

    private void setRewardText(ViewHolder.Quest holder, int position, int id) {
        int res = mData.get(position).getReward().getResource().get(id);
        if (res > 0) {
            ((LinearLayout)holder.mRewardText[id].getParent()).setVisibility(View.VISIBLE);
            holder.mRewardText[id].setText(Integer.toString(res));
        } else {
            ((LinearLayout)holder.mRewardText[id].getParent()).setVisibility(View.GONE);
        }
    }

    private void setRewardText(Quest quest, TextView textView) {
        StringBuilder sb = new StringBuilder();

        for (Integer shipId :
                quest.getReward().getShip()) {
            Ship ship = ShipList.findItemById(mContext, shipId);
            if (ship != null) {
                sb.append(ShipList.shipType[ship.getType()]).append(" ").append(ship.getName().get(mContext)).append("<br>");
            }
        }

        boolean isNumber = false;
        for (Integer equipId :
                quest.getReward().getEquip()) {
            if (!isNumber) {
                Equip equip = EquipList.findItemById(mContext, equipId);
                if (equip != null) {
                    sb.append(equip.getName().get(mContext));
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
                Item item = ItemList.findItemById(mContext, itemId);
                if (item != null) {
                    sb.append(item.getName().get(mContext));
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

    @Override
    public ViewHolder.Quest onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            /*case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest_detail, parent, false);
                break;
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest_detail_one_type, parent, false);
                break;
            case 2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest_detail_expanded, parent, false);
                break;*/
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quest_detail_expanded, parent, false);
        }
        return new ViewHolder.Quest(itemView);
    }

    private static final String[] PERIOD = {"", "日","周","月"};

    @Override
    public void onBindViewHolder(final ViewHolder.Quest holder, int position) {
        Quest quest = mData.get(position);

        if (!quest.isHighlight()) {
            holder.mExpandableLayout.setExpanded(false, false);
            holder.mName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else {
            setViewDetail(holder, position);
            holder.mExpandableLayout.setExpanded(true, false);
            holder.mName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        holder.mName.setText(
                String.format("%s %s",
                        quest.getCode(),
                        quest.getTitle().get(mContext, true)));

        holder.mType[0].setVisibility(View.INVISIBLE);
        holder.mType[1].setVisibility(View.INVISIBLE);

        int viewType = 0;

        if (quest.getPeriod() > 0) {
            holder.mType[0].setVisibility(View.VISIBLE);
            holder.mType[0].setText(PERIOD[quest.getPeriod()]);
            viewType ++;
        }

        if (quest.isNewMission()) {
            holder.mType[viewType].setVisibility(View.VISIBLE);
            holder.mType[viewType].setText("新");
        }

        if (quest.getNote() != null && quest.getNote().length() > 0) {
            holder.mNote.setVisibility(View.VISIBLE);

            Spanned htmlDescription = Html.fromHtml(quest.getNote());
            String descriptionWithOutExtraSpace = htmlDescription.toString().trim();
            holder.mNote.setText(descriptionWithOutExtraSpace);
        } else {
            holder.mNote.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mExpandableLayout.isExpanded()) {
                    holder.mExpandableLayout.setExpanded(false);
                    holder.mName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                } else {
                    setViewDetail(holder, holder.getAdapterPosition());
                    holder.mExpandableLayout.setExpanded(true);
                    holder.mName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }
        });
    }

    private void setViewDetail(ViewHolder.Quest holder, int position) {
        Quest quest = mData.get(position);

        holder.mDetail.setText(Html.fromHtml(quest.getContent().get(holder.itemView.getContext())));

        setRewardText(holder, position, 0);
        setRewardText(holder, position, 1);
        setRewardText(holder, position, 2);
        setRewardText(holder, position, 3);

        setRewardText(quest, holder.mRewardText[4]);

        holder.mQuestContainer.removeAllViews();
        if (mData.get(position).getUnlock().size() > 0) {
            for (String code : mData.get(position).getUnlock()) {
                if (code.length() == 0) {
                    continue;
                }

                final Quest unlockQuest = QuestList.findItemByCode(mContext, code);
                if (unlockQuest == null) {
                    Log.d("QuestAdapter", "Quest not found: " + code);
                    continue;
                }

                View view = LayoutInflater.from(mContext).inflate(R.layout.card_quest_next, holder.mQuestContainer, false);
                ((TextView) view).setText(
                        String.format("前置任务: %s %s", unlockQuest.getCode(), unlockQuest.getTitle().get(mContext, true)));

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

                holder.mQuestContainer.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Quest> data = QuestList.get(mContext);
                mData.clear();

                for (Quest item :
                        data) {
                    if (check(item)) {
                        mData.add(item);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("QAQ",
                        Integer.toString(mData.size()) + " " +
                                Integer.toString(mType) + " " +
                                (mKeyword == null ? "null" : mKeyword) + " " +
                                (mIsSearching ? "true" : "false") + " " +
                                (mLatestOnly ? "true" : "false") + " "
                );

                notifyDataSetChanged();
            }
        }.execute();
    }
}
