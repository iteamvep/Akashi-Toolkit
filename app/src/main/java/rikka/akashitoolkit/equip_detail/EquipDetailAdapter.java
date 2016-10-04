package rikka.akashitoolkit.equip_detail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;

/**
 * Created by Rikka on 2016/9/21.
 */

public class EquipDetailAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, Object> {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ATTRIBUTE = 1;
    public static final int TYPE_EQUIP = 2;
    public static final int TYPE_REMODEL = 3;
    public static final int TYPE_ILLUSTRATION = 4;
    public static final int TYPE_VOICE = 5;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
