package rikka.akashitoolkit.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.staticdata.ItemTypeList;
import rikka.akashitoolkit.ui.ItemDisplayActivity;

/**
 * Created by Rikka on 2016/3/23.
 */
public class ItemAdapter extends RecyclerView.Adapter<ViewHolder.Item> {
    private List<Item> mData;
    private Activity mActivity;
    private int mType;

    public ItemAdapter(Activity activity, int type) {
        mActivity = activity;
        mType = type;
        mData = new ArrayList<>();

        rebuildDataList(activity);
    }

    public void rebuildDataList(Context context) {
        mData.clear();

        for (Item item :
                ItemList.get(context)) {

            if ((ItemTypeList.getsParentList(context).get(ItemTypeList.findItemById(context, item.getSubType()).getParent()) == mType)) {
                mData.add(item);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder.Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder.Item(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.Item holder, final int position) {
        String curType = ItemTypeList.findItemById(holder.mTitle.getContext(), mData.get(position).getSubType()).getName();
        boolean showTitle = position <= 0 || !curType.equals(ItemTypeList.findItemById(holder.mTitle.getContext(), mData.get(position - 1).getSubType()).getName());

        if (showTitle) {
            holder.mTitle.setText(curType);
            holder.mTitle.setVisibility(View.VISIBLE);
        } else {
            holder.mTitle.setVisibility(View.GONE);
        }

        boolean showDivider = position < mData.size() - 1
                && curType.equals(ItemTypeList.findItemById(holder.mTitle.getContext(), mData.get(position + 1).getSubType()).getName());

        holder.mDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView.setVisibility(!showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView2.setVisibility(showTitle && position != 0 ? View.VISIBLE : View.GONE);

        holder.mName.setText(mData.get(position).getName().getZh_cn());

        ItemTypeList.setIntoImageView(holder.mImageView, mData.get(position).getIcon());
        //holder.mImageView.setImageResource(ItemTypeList.getResourceId(holder.itemView.getContext(), mData.get(position).getIcon()));
        //holder.mImageView.setColorFilter();
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDisplayActivity.class);
                intent.putExtra(ItemDisplayActivity.EXTRA_ITEM_ID, mData.get(position).getId());

                int[] location = new int[2];
                holder.mLinearLayout.getLocationOnScreen(location);
                intent.putExtra(ItemDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(ItemDisplayActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());
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
