package rikka.akashitoolkit.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.ItemImprovement;
import rikka.akashitoolkit.staticdata.ItemImprovementList;
import rikka.akashitoolkit.staticdata.ItemTypeList;
import rikka.akashitoolkit.staticdata.QuestList;
import rikka.akashitoolkit.ui.ItemDisplayActivity;

/**
 * Created by Rikka on 2016/3/17.
 */
public class ItemImprovementAdapter extends RecyclerView.Adapter<ViewHolder.ItemImprovement> {
    private List<ItemImprovement> mData;
    private List<String> mDataShip;
    private Activity mActivity;

    public ItemImprovementAdapter(Activity activity, int type) {
        mActivity = activity;
        mData = new ArrayList<>();
        mDataShip = new ArrayList<>();

        for (ItemImprovement item :
                ItemImprovementList.get(activity)) {

            boolean add = false;
            StringBuilder sb = new StringBuilder();
            for (ItemImprovement.SecretaryEntity entity : item.getSecretary()) {
                if (entity.getDay().get(type)) {
                    add = true;
                    sb.append(sb.length() > 0 ? " / " : "" );
                    sb.append(entity.getName());
                }
            }

            if (add) {
                mData.add(item);
                mDataShip.add(sb.toString());
            }

        }
    }

    @Override
    public ViewHolder.ItemImprovement onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_improvement, parent, false);
        return new ViewHolder.ItemImprovement(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.ItemImprovement holder, final int position) {
        holder.mName.setText(mData.get(position).getName());
        holder.mType.setText(mData.get(position).getType());
        holder.mShip.setText("二号舰娘: " + mDataShip.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDisplayActivity.class);
                intent.putExtra(ItemDisplayActivity.EXTRA_ITEM_ID, mData.get(position).getId());

                int[] location = new int[2];
                holder.itemView.getLocationOnScreen(location);
                intent.putExtra(ItemDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(ItemDisplayActivity.EXTRA_START_HEIGHT, holder.itemView.getHeight());
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    v.getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                } else {
                    v.getContext().startActivity(intent);
                }*/
                //v.getContext().startActivity(intent, ActivityOptions.makeCustomAnimation(v.getContext(), 0, 0).toBundle());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        holder.mImageView.setImageResource(ItemTypeList.getResourceId(holder.itemView.getContext(), mData.get(position).getIcon()));
        /*Resources resources = holder.mType.getContext().getResources();
        int resourceId = resources.getIdentifier(String.format("item_type_%d", mData.get(position).getIcon()), "drawable",
                holder.mType.getContext().getPackageName());*/
        //holder.mType.setCompoundDrawables(holder.mType.getContext().getResources().getDrawable(R.drawable.item_type_1, null), null, null, null);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
