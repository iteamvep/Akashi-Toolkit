package rikka.akashitoolkit.equip_improvement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseBookmarkRecyclerAdapter;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementAdapter extends BaseBookmarkRecyclerAdapter<EquipImprovementViewHolder, EquipImprovementAdapter.Data> {

    protected static class Data {
        protected String ship;
        protected EquipImprovement data;

        public Data(String ship, EquipImprovement data) {
            this.ship = ship;
            this.data = data;
        }
    }

    private Activity mActivity;
    private int mType;

    public EquipImprovementAdapter(final Activity activity, int type, boolean bookmarked) {
        super(bookmarked);

        mActivity = activity;
        mType = type;

        setHasStableIds(true);

        rebuildDataList();
    }

    private long generateItemId(EquipImprovement item) {
        return item.getId() + mType * 10000;
    }

    @Override
    public EquipImprovementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equip_improvement, parent, false);
        return new EquipImprovementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EquipImprovementViewHolder holder, int position) {
        holder.mAdapter = this;
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onViewRecycled(EquipImprovementViewHolder holder) {
        holder.mAdapter = null;
        holder.mCheckBox.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
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
                                            upgrade = String.format("%s ★+%d", equip.getName().get(), level);
                                        } else {
                                            upgrade = equip.getName().get();
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
                                            sb.append(s.getName().get());
                                    }
                                }
                            }
                        }
                    }

                    if (add) {
                        item.setBookmarked(Settings.instance(mActivity)
                                .getBoolean(String.format("equip_improve_%d", item.getId()), false));

                        if (!requireBookmarked() || item.isBookmarked()) {
                            addItem(generateItemId(item), 0, new Data(sb.toString(), item));
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }.execute();
    }
}
