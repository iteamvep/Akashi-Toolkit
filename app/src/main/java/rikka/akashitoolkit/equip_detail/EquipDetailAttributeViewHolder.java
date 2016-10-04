package rikka.akashitoolkit.equip_detail;

import android.view.View;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.detail.AttributeViewHolder;
import rikka.akashitoolkit.model.AttributeEntity;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/21.
 * 装备详情里的属性
 */

public class EquipDetailAttributeViewHolder extends AttributeViewHolder implements IBindViewHolder<Equip> {

    public EquipDetailAttributeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Equip data, int position) {
        Adapter adapter = mAdapter;

        AttributeEntity attr = data.getAttr();

        adapter.add(R.string.attr_firepower, attr.getFirepower());
        adapter.add(R.string.attr_aa, attr.getAA());
        adapter.add(R.string.attr_accuracy, attr.getAccuracy());
        adapter.add(R.string.attr_torpedo, attr.getTorpedo());
        adapter.add(R.string.attr_boom, attr.getBombing());
        adapter.add(R.string.attr_asw, attr.getASW());
        adapter.add(R.string.attr_evasion, attr.getEvasion());
        adapter.add(R.string.attr_los, attr.getLOS());
        adapter.add(R.string.attr_armor, attr.getArmor());
        if (true/*data.getParentType() == 3*/) {
            adapter.add(R.string.attr_range_aircraft, attr.getRange());
        } else {
            adapter.add(R.string.attr_range, attr.getRange());
        }
    }
}
