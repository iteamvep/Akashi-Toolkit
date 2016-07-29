package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.otto.BookmarkItemChanged;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.ui.EquipDisplayActivity;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementAdapter extends BaseBookmarkRecyclerAdapter<ViewHolder.ItemImprovement, EquipImprovement> {

    private List<String> mDataShip;
    private Activity mActivity;
    private int mType;

    public EquipImprovementAdapter(final Activity activity, int type, boolean bookmarked) {
        super(bookmarked);

        mActivity = activity;
        mDataShip = new ArrayList<>();
        mType = type;

        setHasStableIds(true);

        rebuildDataList();
    }

    private long generateItemId(EquipImprovement item) {
        return item.getId() + mType * 10000;
    }

    @Override
    public ViewHolder.ItemImprovement onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equip_improvement, parent, false);
        return new ViewHolder.ItemImprovement(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.ItemImprovement holder, int position) {
        Context context = holder.itemView.getContext();
        EquipImprovement item = getItem(position);
        Equip equip = EquipList.findItemById(mActivity, item.getId());

        if (equip == null) {
            holder.mName.setText(String.format(context.getString(R.string.equip_not_found), getItem(position).getId()));
            return;
        }
        if (!item.isBookmarked()) {
            holder.mName.setText(equip.getName().get(mActivity));
        } else {
            holder.mName.setText(String.format("%s ★", equip.getName().get(mActivity)));
        }

        holder.mShip.setText(mDataShip.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EquipImprovement item = getItem(holder.getAdapterPosition());

                Intent intent = new Intent(v.getContext(), EquipDisplayActivity.class);
                intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, getItem(holder.getAdapterPosition()).getId());

                int[] location = new int[2];
                holder.itemView.getLocationOnScreen(location);
                intent.putExtra(EquipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(EquipDisplayActivity.EXTRA_START_HEIGHT, holder.itemView.getHeight());
                intent.putExtra(EquipDisplayActivity.EXTRA_EQUIP_IMPROVE_ID, item.getId());

                BaseItemDisplayActivity.start(mActivity, intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onLongClick(View v) {
                EquipImprovement item = getItem(holder.getAdapterPosition());

                item.setBookmarked(!item.isBookmarked());

                Settings.instance(mActivity)
                        .putBoolean(String.format("equip_improve_%d", item.getId()), item.isBookmarked());

                showToast(mActivity, item.isBookmarked());

                notifyItemChanged(holder.getAdapterPosition());

                BusProvider.instance().post(new BookmarkItemChanged.ItemImprovement());

                return true;
            }
        });

        EquipTypeList.setIntoImageView(holder.mImageView, equip.getIcon());
    }

    @Override
    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                EquipImprovementList.get(mActivity);
                return null;
            }

            @SuppressLint("DefaultLocale")
            @Override
            protected void onPostExecute(Void aVoid) {
                clearItemList();
                mDataShip.clear();

                for (EquipImprovement item :
                        EquipImprovementList.get(mActivity)) {

                    boolean add = false;
                    StringBuilder sb = new StringBuilder();
                    List<Integer> ids = new ArrayList<>();
                    for (Map.Entry<Integer, List<Integer>> entry : item.getData().entrySet()) {
                        int flag = entry.getKey();
                        List<Integer> ship = entry.getValue();

                        if ((flag & 1 << mType) > 0) {

                            if (!add) {
                                String upgrade = null;
                                if (item.getUpgradeTo() != null) {
                                    int level = item.getUpgradeTo().get(1);
                                    int id = item.getUpgradeTo().get(0);

                                    Equip equip = EquipList.findItemById(mActivity, id);
                                    if (equip != null) {
                                        if (level > 0) {
                                            upgrade = String.format("%s ★+%d", equip.getName().get(mActivity), level);
                                        } else {
                                            upgrade = equip.getName().get(mActivity);
                                        }
                                    }

                                    if (upgrade != null) {
                                        sb.append(mActivity.getString(R.string.improvement_upgrade_to))
                                                .append(" ")
                                                .append(upgrade)
                                                .append("\n");
                                    }
                                }
                            }

                            add = true;

                            for (int id : ship) {
                                if (ids.size() > 0) {
                                    sb.append(" / ");
                                }

                                if (ids.indexOf(id) == -1) {
                                    ids.add(id);

                                    if (id == 0) {
                                        sb.append(mActivity.getString(R.string.improvement_any));
                                    } else {
                                        Ship s = ShipList.findItemById(mActivity, id);
                                        if (s != null)
                                            sb.append(s.getName().get(mActivity));
                                    }
                                }
                            }
                        }
                    }

                    if (add) {
                        item.setBookmarked(Settings.instance(mActivity)
                                .getBoolean(String.format("equip_improve_%d", item.getId()), false));

                        if (!requireBookmarked() || item.isBookmarked()) {
                            addItem(generateItemId(item), 0, item);
                            mDataShip.add(sb.toString());
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }.execute();
    }
}
