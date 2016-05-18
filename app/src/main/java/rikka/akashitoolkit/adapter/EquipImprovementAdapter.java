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

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.EquipDisplayActivity;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementAdapter extends BaseBookmarkRecyclerAdapter<ViewHolder.ItemImprovement> {
    private List<EquipImprovement> mData;
    private List<String> mDataShip;
    private Activity mActivity;
    private int mType;

    public EquipImprovementAdapter(final Activity activity, int type, boolean bookmarked) {
        mActivity = activity;
        mData = new ArrayList<>();
        mDataShip = new ArrayList<>();
        mType = type;

        setBookmarked(bookmarked);
        setHasStableIds(true);

        rebuildDataList();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId() + mType * 10000;
    }

    @Override
    public ViewHolder.ItemImprovement onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_improvement, parent, false);
        return new ViewHolder.ItemImprovement(itemView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final ViewHolder.ItemImprovement holder, int position) {
        Equip equip = EquipList.findItemById(mActivity, mData.get(position).getId());

        if (equip != null) {
            EquipImprovement item = mData.get(position);

            if (!item.isBookmarked()) {
                holder.mName.setText(equip.getName().get(mActivity));
            } else {
                holder.mName.setText(equip.getName().get(mActivity) + " ★");
            }
        }

        holder.mShip.setText("二号舰娘: " + mDataShip.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EquipDisplayActivity.class);
                intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, mData.get(holder.getAdapterPosition()).getId());

                int[] location = new int[2];
                holder.itemView.getLocationOnScreen(location);
                intent.putExtra(EquipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(EquipDisplayActivity.EXTRA_START_HEIGHT, holder.itemView.getHeight());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onLongClick(View v) {
                EquipImprovement item = mData.get(holder.getAdapterPosition());

                item.setBookmarked(!item.isBookmarked());

                Settings.instance(mActivity)
                        .putBoolean(String.format("equip_improve_%d", item.getId()), item.isBookmarked());

                showToast(mActivity, item.isBookmarked());

                notifyItemChanged(holder.getAdapterPosition());

                return true;
            }
        });

        EquipTypeList.setIntoImageView(holder.mImageView, mData.get(position).getIcon());
    }

    @Override
    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("DefaultLocale")
            @Override
            protected Void doInBackground(Void... params) {
                mData.clear();
                mDataShip.clear();

                for (EquipImprovement item :
                        EquipImprovementList.get(mActivity)) {

                    boolean add = false;
                    StringBuilder sb = new StringBuilder();
                    for (EquipImprovement.SecretaryEntity entity : item.getSecretary()) {
                        if (entity.getDay().get(mType)) {
                            add = true;
                            sb.append(sb.length() > 0 ? " / " : "" );
                            sb.append(entity.getName());
                        }
                    }

                    if (add) {
                        item.setBookmarked(Settings.instance(mActivity)
                                .getBoolean(String.format("equip_improve_%d", item.getId()), false));

                        if (!requireBookmarked() || item.isBookmarked()) {
                            mData.add(item);
                            mDataShip.add(sb.toString());
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
