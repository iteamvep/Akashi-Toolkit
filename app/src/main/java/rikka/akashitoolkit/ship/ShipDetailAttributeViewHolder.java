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
        if (data.getAttribute() == null) {
            return;
        }

        Context context = itemView.getContext();
        Adapter adapter = mAdapter;

        AttributeEntity attr = data.getAttribute();
        AttributeEntity attr_max = attr.plus(data.getAttributeMax()).plus(data.getAttribute99());

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
            addEnemy(adapter, R.string.attr_firepower, attr.getFirepower(), attr_max.getFirepower());
            addEnemy(adapter, R.string.attr_aa, attr.getAA(), attr_max.getAA());
            addEnemy(adapter, R.string.attr_torpedo, attr.getTorpedo(), attr_max.getTorpedo());
            addEnemy(adapter, R.string.attr_armor, attr.getArmor(), attr_max.getArmor());
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

    /**
     * 使用来自 https://zh.kcwiki.moe/wiki/深海栖舰列表 的数据
     * 原文 "/"前后数值表示："面板火力或雷装/计入装备后火力或雷装"
     *
     * @param adapter Adapter
     * @param title   标题
     * @param min     不带有装备的数值
     * @param max     带有装备的数值
     */
    @SuppressLint("DefaultLocale")
    private void addEnemy(Adapter adapter, @StringRes int title, int min, int max) {
        if (max == 0 || min == max) {
            adapter.add(title, String.format("%d", min));
        } else {
            adapter.add(title, String.format("%d / %d", min, max));
        }
    }
}
