package rikka.akashitoolkit.ship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Locale;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.viewholder.SimpleStringHolder;
import rikka.akashitoolkit.viewholder.SimpleTitleViewHolder;

/**
 * 用在舰娘详情
 */
public class ShipDetailAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, Object> {

    private static final String TAG = "ShipDetailAdapter";

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ATTRIBUTE = 1;
    public static final int TYPE_EQUIP = 2;
    public static final int TYPE_REMODEL = 3;
    public static final int TYPE_ILLUSTRATION = 4;
    public static final int TYPE_VOICE = 5;
    public static final int TYPE_CONSUME = 6;
    public static final int TYPE_BROKEN = 7;
    public static final int TYPE_POW_UP = 8;
    public static final int TYPE_GET = 9;
    public static final int TYPE_PAINTER_CV = 10;

    private Ship mItem;

    private ShipDetailRemodelViewHolder.OnItemClickListener mItemClickListener;

    @SuppressLint("DefaultLocale")
    public void setItem(Context context, Ship item) {
        if (mItem == null || item.getId() != mItem.getId()) {
            mItem = item;

            clearItemList();

            boolean is_enemy = item.isEnemy();

            addItem(ShipDetailAdapter.TYPE_ATTRIBUTE, item);
            addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.equip_and_load));
            addItem(ShipDetailAdapter.TYPE_EQUIP, item.getEquip());
            if (!is_enemy) {
                addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.remodel));
                addItem(ShipDetailAdapter.TYPE_REMODEL, item);

                addItem(ShipDetailAdapter.TYPE_TITLE, "消耗 & 解体 & 合成");
                addItem(ShipDetailAdapter.TYPE_CONSUME, null);
                addItem(ShipDetailAdapter.TYPE_TITLE, "入手方式");
                addItem(ShipDetailAdapter.TYPE_GET, null);
                addItem(ShipDetailAdapter.TYPE_TITLE, "画师 & 声优");
                addItem(ShipDetailAdapter.TYPE_PAINTER_CV, null);
            }
            addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.illustration));
            addItem(ShipDetailAdapter.TYPE_ILLUSTRATION, item);
            if (!is_enemy) {
                addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.voice));
            }

            for (int i = 0; i < getItemList().size(); i++) {
                StringBuilder sb;
                switch (getItemViewType(i)) {
                    case TYPE_CONSUME:
                        sb = new StringBuilder();
                        sb.append(String.format(
                                "消耗   %s %d %s %d",
                                context.getString(R.string.res_fuel),
                                item.getResourceConsume()[0],
                                context.getString(R.string.res_ammo),
                                item.getResourceConsume()[1]));
                        sb.append("\n");

                        sb.append(String.format(
                                "解体   %s %d %s %d %s %d %s %d",
                                context.getString(R.string.res_fuel),
                                item.getBrokenResources()[0],
                                context.getString(R.string.res_ammo),
                                item.getBrokenResources()[1],
                                context.getString(R.string.res_steel),
                                item.getBrokenResources()[2],
                                context.getString(R.string.res_bauxite),
                                item.getBrokenResources()[3]));
                        sb.append("\n");

                        sb.append(String.format(
                                "合成   %s +%d %s +%d %s +%d %s +%d",
                                context.getString(R.string.attr_firepower),
                                item.getModernizationBonus()[0],
                                context.getString(R.string.attr_torpedo),
                                item.getModernizationBonus()[1],
                                context.getString(R.string.attr_aa),
                                item.getModernizationBonus()[2],
                                context.getString(R.string.attr_armor),
                                item.getModernizationBonus()[3]));

                        getItemList().set(i, sb.toString());
                        break;
                    case TYPE_GET:
                        sb = new StringBuilder();
                        if (item.getGet().getBuild() == 1) {
                            sb.append("建造 / ");
                            sb.append(String.format("%02d", item.getGet().getBuildTime() / 60));
                            sb.append(":");
                            sb.append(String.format("%02d", item.getGet().getBuildTime() % 60));
                            sb.append(":");
                            sb.append(String.format("%02d", 0));
                            sb.append("\n");
                        }
                        if (item.getGet().getDrop() == 1) {
                            sb.append("掉落 / 可通过打捞获得\n");
                        }
                        String str = sb.toString().trim();
                        if (TextUtils.isEmpty(str)) {
                            removeItem(i);
                            removeItem(i - 1);
                        } else {
                            getItemList().set(i, sb.toString().trim());
                        }

                        break;
                    case TYPE_PAINTER_CV:
                        getItemList().set(i, String.format(
                                "%s / %s",
                                item.getPainter(), item.getCV()));
                        break;
                }
            }

            notifyDataSetChanged();
        }
    }

    public ShipDetailAdapter(Context context, Ship item, ShipDetailRemodelViewHolder.OnItemClickListener listener) {
        mItemClickListener = listener;

        setItem(context, item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return new SimpleTitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_subtitle, parent, false));
            case TYPE_ATTRIBUTE:
                return new ShipDetailAttributeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_recycler_view, parent, false));
            case TYPE_EQUIP:
                return new ShipDetailEquipsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_recycler_view, parent, false));
            case TYPE_REMODEL:
                ShipDetailRemodelViewHolder viewHolder = new ShipDetailRemodelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_recycler_view, parent, false));
                viewHolder.setOnItemClickListener(mItemClickListener);
                return viewHolder;
            case TYPE_ILLUSTRATION:
                return new ShipDetailIllustrationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_recycler_view, parent, false));
            case TYPE_VOICE:
                return new ShipDetailVoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_item_voice, parent, false));

            case TYPE_CONSUME:
            /*case TYPE_BROKEN:
            case TYPE_POW_UP:*/
            case TYPE_GET:
            case TYPE_PAINTER_CV:
                return new SimpleStringHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_text, parent, false), android.R.id.text1);
        }
        return null;
    }
}
