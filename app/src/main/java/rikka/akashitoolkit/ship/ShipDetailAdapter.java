package rikka.akashitoolkit.ship;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.model.Ship;
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

    private Ship mItem;

    private ShipDetailRemodelViewHolder.OnItemClickListener mItemClickListener;

    public void setItem(Ship item) {
        if (mItem == null || item.getId() != mItem.getId()) {
            mItem = item;

            boolean is_enemy = item.getId() >= 500;

            if (!is_enemy) {
                getItemList().set(0, mItem);
                getItemList().set(2, mItem.getEquip());
                getItemList().set(4, mItem);
                getItemList().set(6, mItem);
            } else {
                getItemList().set(0, mItem);
                getItemList().set(2, mItem.getEquip());
                getItemList().set(4, mItem);
            }

            notifyDataSetChanged();
        }
    }

    public ShipDetailAdapter(Context context, Ship item, ShipDetailRemodelViewHolder.OnItemClickListener listener) {
        mItemClickListener = listener;

        boolean is_enemy = item.getId() >= 500;

        addItem(ShipDetailAdapter.TYPE_ATTRIBUTE, null); // 0
        addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.equip_and_load));
        addItem(ShipDetailAdapter.TYPE_EQUIP, null); // 2
        if (!is_enemy) {
            addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.remodel));
            addItem(ShipDetailAdapter.TYPE_REMODEL, null); // 4
        }
        addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.illustration));
        addItem(ShipDetailAdapter.TYPE_ILLUSTRATION, null); // 6
        if (!is_enemy) {
            addItem(ShipDetailAdapter.TYPE_TITLE, context.getString(R.string.voice));
        }
        setItem(item);
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
        }
        return null;
    }
}
