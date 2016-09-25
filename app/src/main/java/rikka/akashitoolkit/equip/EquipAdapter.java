package rikka.akashitoolkit.equip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseBookmarkRecyclerAdapter;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipType;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataListRebuiltFinished;
import rikka.akashitoolkit.otto.ItemSelectAction;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.viewholder.SimpleTitleDividerViewHolder;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipAdapter extends BaseBookmarkRecyclerAdapter<RecyclerView.ViewHolder, Object> {

    private Activity mActivity;
    private int mType;
    private boolean mEnemy;
    private boolean mSelectMode;
    private Ship mShip;

    public EquipAdapter(Activity activity, int type, boolean bookmarked, boolean enemy, boolean select_mode, Ship ship) {
        super(bookmarked);

        setHasStableIds(true);

        mActivity = activity;
        mType = type;
        mEnemy = enemy;
        mSelectMode = select_mode;
        mShip = ship;

        rebuildDataList();
    }

    public void setType(int type) {
        mType = type;
    }

    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                EquipList.get(mActivity);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                clearItemList();
                int lastType = -1;

                for (Equip equip : EquipList.get(mActivity)) {
                    if (!check(equip))
                        continue;

                    if (getItemCount() == 0 || lastType != equip.getType()) {
                        lastType = equip.getType();
                        EquipType equipType = EquipTypeList.findItemById(mActivity, equip.getType());
                        if (equipType != null)
                            addItem(equipType.getId() * 10000, 1, equipType.getName(mActivity));
                    }
                    addItem(equip.getId(), 0, equip);
                }

                if (mType == 0) {
                    onDataListRebuilt(getItemList());
                }

                notifyDataSetChanged();
                BusProvider.instance().post(new DataListRebuiltFinished());
            }
        }.execute();
    }

    private boolean check(Equip equip) {
        if (mEnemy && equip.getId() < 500) {
            return false;
        }

        if (!mEnemy && equip.getId() > 500) {
            return false;
        }

        if (requireBookmarked() && !equip.isBookmarked()) {
            return false;
        }

        if (equip.getParentType() != mType && mType != 0) {
            return false;
        }

        if (mShip != null
                && (mShip.getShipType() != null && !mShip.getShipType().canEquip(equip))
                && (mShip.getExtraEquipType() == null || !mShip.getExtraEquipType().contains(equip.getTypes()[2]))) {
            return false;
        }

        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
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

        holder.mName.setText(String.format(item.isBookmarked() ? "%s ★" : "%s",
                item.getName().get(holder.mName.getContext())));

        EquipTypeList.setIntoImageView(holder.mImageView, item.getIcon());

        if (!mSelectMode) {
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    Intent intent = new Intent(context, EquipDetailActivity.class);
                    intent.putExtra(EquipDetailActivity.EXTRA_ITEM_ID,
                            ((Equip) getItem(holder.getAdapterPosition())).getId());

                    int[] location = new int[2];
                    holder.mLinearLayout.getLocationOnScreen(location);
                    intent.putExtra(EquipDetailActivity.EXTRA_START_Y, location[1]);
                    intent.putExtra(EquipDetailActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());

                    BaseItemDisplayActivity.start(mActivity, intent);
                }
            });
        } else {
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Equip item = (Equip) getItem(holder.getAdapterPosition());
                    if (item != null) {
                        BusProvider.instance().post(new ItemSelectAction.Finish(item.getId()));
                    }
                }
            });
        }

        if (!mEnemy && !mSelectMode) {
            holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onLongClick(View v) {
                    Equip item = (Equip) getItem(holder.getAdapterPosition());

                    item.setBookmarked(!item.isBookmarked());

                    Settings.instance(mActivity)
                            .putBoolean(String.format("equip_%d", item.getId()), item.isBookmarked());

                    showToast(mActivity, item.isBookmarked());

                    notifyItemChanged(holder.getAdapterPosition());

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
            ((EquipViewHolder) holder).mImageView.setTag(null);
        }
    }
}
