package rikka.akashitoolkit.ship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.detail.AttributeViewHolder;
import rikka.akashitoolkit.model.AttributeEntity;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.utils.KCStringFormatter;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/17.
 */
public class ShipDetailAttributeViewHolder extends AttributeViewHolder implements IBindViewHolder<Ship> {

    public ShipDetailAttributeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Ship data, int position) {
        Context context = itemView.getContext();
        Adapter adapter = mAdapter;

        AttributeEntity attr = data.getAttr();
        AttributeEntity attr_max = attr.plus(data.getAttrMax()).plus(data.getAttr99());

        if (!data.isEnemy()) {
            adapter.add(R.string.attr_hp, attr.getHP()/*, attr_max.getHP()*/);

            add(adapter, R.string.attr_firepower, attr.getFirepower(), attr_max.getFirepower());
            add(adapter, R.string.attr_aa, attr.getAA(), attr_max.getAA());
            add(adapter, R.string.attr_torpedo, attr.getTorpedo(), attr_max.getTorpedo());
            add(adapter, R.string.attr_armor, attr.getArmor(), attr_max.getArmor());

            add(adapter, R.string.attr_asw, attr.getASW(), attr_max.getASW());
            add(adapter, R.string.attr_evasion, attr.getEvasion(), attr_max.getEvasion());
            add(adapter, R.string.attr_los, attr.getLOS(), attr_max.getLOS());

            adapter.add(R.string.attr_speed, KCStringFormatter.getSpeed(context, attr.getSpeed()));
            adapter.add(R.string.attr_range, KCStringFormatter.getRange(context, attr.getRange()));

            add(adapter, R.string.attr_luck, attr.getLuck(), attr_max.getLuck());
        } else {
            adapter.add(R.string.attr_hp, attr.getHP());
            adapter.add(R.string.attr_firepower, attr.getFirepower());
            adapter.add(R.string.attr_aa, attr.getAA());
            adapter.add(R.string.attr_torpedo, attr.getTorpedo());
            adapter.add(R.string.attr_armor, attr.getArmor());
            adapter.add(R.string.attr_speed, KCStringFormatter.getSpeed(context, attr.getSpeed()));
            adapter.add(R.string.attr_range, KCStringFormatter.getRange(context, attr.getRange()));
        }
    }

    @SuppressLint("DefaultLocale")
    private void add(Adapter adapter, @StringRes int title, int min, int max) {
        if (min == max) {
            adapter.add(title, String.format("%d", min));
        } else {
            adapter.add(title, String.format("%d → %d", min, max));
        }
    }
}
