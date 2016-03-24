package rikka.akashitoolkit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.ui.ItemDisplayActivity;

/**
 * Created by Rikka on 2016/3/23.
 */
public class ItemAdapter extends RecyclerView.Adapter<ViewHolder.Item> {
    private List<Item> mData;
    private Activity mActivity;

    public ItemAdapter(Activity activity) {
        mActivity = activity;
        mData = ItemList.get(activity);
    }

    @Override
    public ViewHolder.Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder.Item(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.Item holder, final int position) {
        holder.mName.setText(mData.get(position).getName());

        Resources resources = holder.itemView.getContext().getResources();
        int resourceId = resources.getIdentifier(String.format("item_type_%d", mData.get(position).getIcon()), "drawable",
                holder.itemView.getContext().getPackageName());
        holder.mImageView.setImageResource(resourceId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDisplayActivity.class);
                intent.putExtra(ItemDisplayActivity.EXTRA_ITEM_ID, mData.get(position).getId());

                int[] loaction = new int[2];
                holder.itemView.getLocationOnScreen(loaction);
                intent.putExtra(ItemDisplayActivity.EXTRA_START_Y, loaction[1]);
                intent.putExtra(ItemDisplayActivity.EXTRA_START_HEIGHT, holder.itemView.getHeight());
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    v.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                } else {
                    v.getContext().startActivity(intent);
                }*/
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
