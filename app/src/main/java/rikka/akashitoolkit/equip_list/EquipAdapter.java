package rikka.akashitoolkit.equip_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseBookmarkRecyclerAdapter;
import rikka.akashitoolkit.equip_detail.EquipDetailActivity;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipType;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataListRebuiltFinished;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.ApiConstParam;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.SimpleTitleDividerViewHolder;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipAdapter extends BaseBookmarkRecyclerAdapter<RecyclerView.ViewHolder, Object> {

    public static final String ARG_TYPE_IDS = "TYPE";
    public static final String ARG_BOOKMARKED = "BOOKMARKED";
    public static final String ARG_SELECT_MODE = "SELECT_MODE";
    public static final String ARG_ENEMY_MODE = "ENEMY";

    private boolean mEnemyMode;
    private boolean mSelectMode;
    private List<Integer> mTypeList;

    public EquipAdapter(Bundle args) {
        this(args.getIntegerArrayList(ARG_TYPE_IDS),
                args.getBoolean(ARG_BOOKMARKED),
                args.getBoolean(ARG_ENEMY_MODE),
                args.getBoolean(ARG_SELECT_MODE));
    }

    public EquipAdapter(List<Integer> type, boolean bookmarked, boolean enemy_mode, boolean select_mode) {
        super(bookmarked);

        setHasStableIds(true);

        mTypeList = type;
        mEnemyMode = enemy_mode;
        mSelectMode = select_mode;

        rebuildDataList();
    }

    public void rebuildDataList() {
        clearItemList();
        int lastType = -1;

        for (Equip equip : EquipList.get()) {
            if (!check(equip))
                continue;

            if (getItemCount() == 0 || lastType != equip.getType()) {
                lastType = equip.getType();
                EquipType equipType = equip.getEquipType();
                if (equipType != null)
                    addItem(equipType.getId() * 10000, 1, equipType.getName().get());
            }
            addItem(equip.getId(), 0, equip);
        }

        notifyDataSetChanged();
        BusProvider.instance().post(new DataListRebuiltFinished());

    }

    private boolean check(Equip equip) {
        if (mEnemyMode && !equip.isEnemy()) {
            return false;
        }

        if (!mEnemyMode && equip.isEnemy()) {
            return false;
        }

        if (requireBookmarked() && !equip.isBookmarked()) {
            return false;
        }

        if (mTypeList != null && !mTypeList.contains(equip.getType())) {
            return false;
        }

        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equip, parent, false);
            return new EquipViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtitle, parent, false);
            return new SimpleTitleDividerViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                bindViewHolder((EquipViewHolder) holder, position);
                break;
            case 1:
                bindViewHolder((SimpleTitleDividerViewHolder) holder, position);
                break;
        }
    }

    private void bindViewHolder(final EquipViewHolder holder, int position) {
        Equip item = (Equip) getItem(position);

        holder.mName.setText(item.getName().get(ApiConstParam.Language.JA));
        if (Utils.isJapanese(holder.itemView.getContext())) {
            holder.mNameTranslate.setVisibility(View.GONE);
        } else {
            holder.mNameTranslate.setText(item.getName().get());
        }

        EquipTypeList.setIntoImageView(holder.mIcon, item.getIcon());

        holder.mCheckBox.setChecked(item.isBookmarked());

        if (mEnemyMode) {
            holder.mCheckBox.setVisibility(View.GONE);
        }

        if (!mSelectMode) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    Intent intent = new Intent(context, EquipDetailActivity.class);
                    intent.putExtra(EquipDetailActivity.EXTRA_ITEM_ID,
                            ((Equip) getItem(holder.getAdapterPosition())).getId());

                    int[] location = new int[2];
                    holder.itemView.getLocationOnScreen(location);
                    intent.putExtra(EquipDetailActivity.EXTRA_START_Y, location[1]);
                    intent.putExtra(EquipDetailActivity.EXTRA_START_HEIGHT, holder.itemView.getHeight());

                    BaseItemDisplayActivity.start(v.getContext(), intent);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Equip item = (Equip) getItem(holder.getAdapterPosition());
                    if (item != null) {
                        BusProvider.instance().post(new ItemSelectAction.Finish(item.getId()));
                    }
                }
            });
            holder.mCheckBox.setVisibility(View.GONE);
        }

        if (!mEnemyMode && !mSelectMode) {
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                    Equip item = (Equip) getItem(holder.getAdapterPosition());

                    item.setBookmarked(isChecked);

                    Settings.instance(v.getContext())
                            .putBoolean(String.format("equip_%d", item.getId()), item.isBookmarked());

                    showToast(v.getContext(), item.isBookmarked());

                    //notifyItemChanged(holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onLongClick(View v) {
                    Equip item = (Equip) getItem(holder.getAdapterPosition());

                    item.setBookmarked(!item.isBookmarked());

                    Settings.instance(v.getContext())
                            .putBoolean(String.format("equip_%d", item.getId()), item.isBookmarked());

                    showToast(v.getContext(), item.isBookmarked());

                    holder.mCheckBox.setChecked(item.isBookmarked());

                    //notifyItemChanged(holder.getAdapterPosition());

                    return true;
                }
            });
        }
    }

    private void bindViewHolder(final SimpleTitleDividerViewHolder holder, int position) {
        boolean showDivider = position != 0 && (getItemViewType(position - 1) != getItemViewType(position));

        holder.mDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        holder.mTitle.setText((String) getItem(position));
        holder.itemView.setSelected(true);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof EquipViewHolder) {
            ((EquipViewHolder) holder).mIcon.setTag(null);
            ((EquipViewHolder) holder).mCheckBox.setOnCheckedChangeListener(null);
        }
    }
}
