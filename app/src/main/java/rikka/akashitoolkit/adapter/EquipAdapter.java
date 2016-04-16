package rikka.akashitoolkit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.ui.EquipDisplayActivity;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipAdapter extends BaseRecyclerAdapter<ViewHolder.Item> {
    private List<Equip> mData;
    private Activity mActivity;
    private int mType;

    public EquipAdapter(Activity activity, int type) {
        mActivity = activity;
        mType = type;
        mData = new ArrayList<>();

        rebuildDataList();
    }

    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mData.clear();

                for (Equip equip :
                        EquipList.get(mActivity)) {

                    if ((EquipTypeList.getsParentList(mActivity).get(EquipTypeList.findItemById(mActivity, equip.getSubType()).getParent()) == mType)) {
                        mData.add(equip);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
            }
        }.execute();
        /*mData.clear();

        for (Equip item :
                EquipList.get(context)) {

            if ((EquipTypeList.getsParentList(context).get(EquipTypeList.findItemById(context, item.getSubType()).getParent()) == mType)) {
                mData.add(item);
            }
        }

        notifyDataSetChanged();*/
    }

    @Override
    public ViewHolder.Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder.Item(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.Item holder, final int position) {
        String curType = EquipTypeList.findItemById(holder.mTitle.getContext(), mData.get(position).getSubType()).getName();
        boolean showTitle = position <= 0 || !curType.equals(EquipTypeList.findItemById(holder.mTitle.getContext(), mData.get(position - 1).getSubType()).getName());

        if (showTitle) {
            holder.mTitle.setText(curType);
            holder.mTitle.setVisibility(View.VISIBLE);
        } else {
            holder.mTitle.setVisibility(View.GONE);
        }

        boolean showDivider = position < mData.size() - 1
                && curType.equals(EquipTypeList.findItemById(holder.mTitle.getContext(), mData.get(position + 1).getSubType()).getName());

        holder.mDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView.setVisibility(!showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView2.setVisibility(showTitle && position != 0 ? View.VISIBLE : View.GONE);

        holder.mName.setText(mData.get(position).getName().get(holder.mName.getContext()));

        EquipTypeList.setIntoImageView(holder.mImageView, mData.get(position).getIcon());
        //holder.mImageView.setImageResource(EquipTypeList.getResourceId(holder.itemView.getContext(), mData.get(position).getIcon()));
        //holder.mImageView.setColorFilter();
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EquipDisplayActivity.class);
                intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, mData.get(position).getId());

                int[] location = new int[2];
                holder.mLinearLayout.getLocationOnScreen(location);
                intent.putExtra(EquipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(EquipDisplayActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    v.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                } else {
                    v.getContext().startActivity(intent);
                }*/
                // API 16+.....
                //v.getContext().startActivity(intent, ActivityOptions.makeCustomAnimation(v.getContext(), 0, 0).toBundle());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
