package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipType;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataListRebuiltFinished;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.EquipDisplayActivity;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipAdapter extends BaseBookmarkRecyclerAdapter<ViewHolder.Item> {
    private static class Data {
        public Object data;
        public int type;
        public int id;

        public Data(Object data, int type, int id) {
            this.data = data;
            this.type = type;
            this.id = id;
        }
    }

    private List<Data> mData;
    private Activity mActivity;
    private int mType;

    private Toast mToast;

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }

    public EquipAdapter(Activity activity, int type, boolean bookmarked) {
        setHasStableIds(true);

        mActivity = activity;
        mType = type;
        mData = new ArrayList<>();

        setBookmarked(bookmarked);

        rebuildDataList();
    }

    public void setType(int type) {
        mType = type;
    }

    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            List<Data> newList = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... params) {
                newList.clear();


                for (Equip equip :
                        EquipList.get(mActivity)) {
                    if (requireBookmarked() && !equip.isBookmarked()) {
                        continue;
                    }

                    if (mType == 0 || (EquipTypeList.getsParentList(mActivity).get(
                            EquipTypeList.findItemById(mActivity, equip.getSubType()).getParent()) == mType)) {

                        int lastType = -1;
                        if (newList.size() > 0) {
                            Data last = newList.get(newList.size() - 1);
                            if (last.data != null && last.data instanceof Equip) {
                                lastType = EquipTypeList.findItemById(mActivity, ((Equip) last.data).getSubType()).getId();
                            }
                        }

                        if (newList.size() == 0 || lastType != -1 && lastType != EquipTypeList.findItemById(mActivity, equip.getSubType()).getId()) {
                            EquipType equipType = EquipTypeList.findItemById(mActivity, equip.getSubType());
                            newList.add(new Data(equipType.getName(), 1, equipType.getId() * 10000));
                        }

                        newList.add(new Data(equip, 0, equip.getId()));
                    }
                }

                if (mType == 0) {
                    onDataListRebuilt(newList);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mData.clear();
                mData.addAll(newList);
                notifyDataSetChanged();
                BusProvider.instance().post(new DataListRebuiltFinished());
            }
        }.execute();
    }

    @Override
    public ViewHolder.Item onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            return new ViewHolder.Item(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_header, parent, false);
            return new ViewHolder.Item(itemView);
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder.Item holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                if (position >= (getItemCount() - 1) || getItemViewType(position + 1) == 1) {
                    holder.mDivider.setVisibility(View.GONE);
                } else {
                    holder.mDivider.setVisibility(View.VISIBLE);
                }

                Equip item = (Equip) mData.get(position).data;

                holder.mName.setText(String.format(item.isBookmarked() ? "%s ★" : "%s",
                        item.getName().get(holder.mName.getContext())));

                EquipTypeList.setIntoImageView(holder.mImageView, item.getIcon());

                holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), EquipDisplayActivity.class);
                        intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID,
                                ((Equip) mData.get(holder.getAdapterPosition()).data).getId());

                        int[] location = new int[2];
                        holder.mLinearLayout.getLocationOnScreen(location);
                        intent.putExtra(EquipDisplayActivity.EXTRA_START_Y, location[1]);
                        intent.putExtra(EquipDisplayActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());

                        v.getContext().startActivity(intent);
                        mActivity.overridePendingTransition(0, 0);
                    }
                });

                holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public boolean onLongClick(View v) {
                        Equip item = (Equip) mData.get(holder.getAdapterPosition()).data;

                        item.setBookmarked(!item.isBookmarked());

                        Settings.instance2(mActivity)
                                .putBoolean(String.format("equip_%d", item.getId()), item.isBookmarked());

                        if (mToast != null) {
                            mToast.cancel();
                        }

                        mToast = Toast.makeText(mActivity, item.isBookmarked() ? mActivity.getString(R.string.bookmark_add) : mActivity.getString(R.string.bookmark_remove), Toast.LENGTH_SHORT);
                        mToast.show();

                        notifyItemChanged(holder.getAdapterPosition());

                        return true;
                    }
                });
                break;
            case 1:
                holder.mTitle.setText((String) mData.get(position).data);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onViewRecycled(ViewHolder.Item holder) {
        super.onViewRecycled(holder);

        if (holder.getItemViewType() == 0) {
            holder.mImageView.setTag(null);
        }
    }
}
