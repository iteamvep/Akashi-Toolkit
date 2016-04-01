package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.staticdata.ItemTypeList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipDisplayActivity extends BaseItemDisplayActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Ship mItem;
    private LinearLayout mItemAttrContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            id = intent.getIntExtra(EXTRA_ITEM_ID, 0);
            mItem = ShipList.findItemById(this, id);
            if (mItem == null) {
                Log.d("QAQ", "No item find? id=" + Integer.toString(id));
                finish();
                return;
            }
        } else {
            finish();
            return;
        }

        setContentView(R.layout.activity_item_display);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        setViews();
    }

    private void setViews() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Utils.isNightMode(getResources())) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);
            mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp_dark);
            mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        }

        if (mItem.getName() != null) {
            getSupportActionBar().setTitle(mItem.getName().getZh_cn());
        }

        ((TextView) findViewById(R.id.text_title)).setText(String.format(
                "No. %d %s",
                mItem.getId_illustrations(),
                ShipList.shipType[mItem.getType()]
                /*formatStars(mItem.getRarity()))*/));

        addAttrView(mItemAttrContainer, "耐久", mItem.getAttr().getHp().get(0), R.drawable.item_attr_hp);
        addAttrView(mItemAttrContainer, "火力", mItem.getAttr().getFire().get(0), R.drawable.item_attr_fire);
        addAttrView(mItemAttrContainer, "对空", mItem.getAttr().getAa().get(0), R.drawable.item_attr_aa);
        addAttrView(mItemAttrContainer, "雷装", mItem.getAttr().getTorpedo().get(0), R.drawable.item_attr_torpedo);
        addAttrView(mItemAttrContainer, "装甲", mItem.getAttr().getArmor().get(0), R.drawable.item_attr_armor);
        addAttrView(mItemAttrContainer, "对潜", mItem.getAttr().getAsw().get(0), R.drawable.item_attr_asw);
        addAttrView(mItemAttrContainer, "回避", mItem.getAttr().getEvasion().get(0), R.drawable.item_attr_dodge);
        addAttrView(mItemAttrContainer, "索敌", mItem.getAttr().getSearch().get(0), R.drawable.item_attr_search);
        addAttrView(mItemAttrContainer, "航速", mItem.getAttr().getSpeed(), R.drawable.item_attr_speed);
        addAttrView(mItemAttrContainer, "射程", mItem.getAttr().getRange(), R.drawable.item_attr_range);
        addAttrView(mItemAttrContainer, "运", mItem.getAttr().getLuck().get(0), R.drawable.item_attr_luck);

        addEquip();
    }

    private void addEquip() {
        ViewGroup parent = addCell(mLinearLayout, "初始装备 & 搭载量");

        List<Integer> equipId = mItem.getEquip().get(0);
        List<Integer> equipSlot = mItem.getEquip().get(1);

        for (int i = 0; i < mItem.getSlot(); i++) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_item, null);

            if (equipId.get(i) > 0) {
                Item item = ItemList.findItemById(this, equipId.get(i));
                ((TextView) view.findViewById(android.R.id.title)).setText(item.getName().getZh_cn());
                ItemTypeList.setIntoImageView((ImageView) view.findViewById(R.id.imageView), item.getIcon());
            } else {
                ((TextView) view.findViewById(android.R.id.title)).setText("未装备");
                view.findViewById(android.R.id.title).setEnabled(false);
            }

            ((TextView) view.findViewById(R.id.textView)).setText(Integer.toString(equipSlot.get(i)));

            parent.addView(view);
        }
    }

    private LinearLayout mCurAttrLinearLayout;
    private int attr = 0;
    private void addAttrView(ViewGroup parent, String title, int value, int icon) {
        if (value == 0) {
            return;
        }

        if (mItemAttrContainer == null) {
            mItemAttrContainer = (LinearLayout) addCell(mLinearLayout, "属性 初期值"/*R.string.attributes*/);
            parent = mItemAttrContainer;
        }

        attr ++;

        if (mCurAttrLinearLayout == null) {
            mCurAttrLinearLayout = new LinearLayout(this);
            mCurAttrLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mCurAttrLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32)));
            mCurAttrLinearLayout.setBaselineAligned(false);
            mCurAttrLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_attr_cell, mCurAttrLinearLayout);
            parent.addView(mCurAttrLinearLayout);
        }

        View cell = mCurAttrLinearLayout
                .findViewById((attr % 2 == 0) ? R.id.item_attr_cell2 : R.id.item_attr_cell);

        cell.setVisibility(View.VISIBLE);

        ((TextView) cell.findViewById(R.id.textView)).setText(title);

        if (icon == R.drawable.item_attr_range) {
            ((TextView) cell.findViewById(R.id.textView2)).setText(getRangeString(value));
        } else if (icon == R.drawable.item_attr_speed) {
            ((TextView) cell.findViewById(R.id.textView2)).setText(getSpeedString(value));
        } else {
            ((TextView) cell.findViewById(R.id.textView2)).setText(String.format("%d", value));
            //((TextView) cell.findViewById(R.id.textView2)).setTextColor(ContextCompat.getColor(this, value > 0 ? R.color.material_green_300 : R.color.material_red_300));
        }

        ((ImageView) cell.findViewById(R.id.imageView)).setImageDrawable(ContextCompat.getDrawable(this, icon));

        if (attr % 2 == 0) {
            mCurAttrLinearLayout = null;
        }
    }

    private String getSpeedString(int value) {
        switch (value) {
            case 10: return "高速";
            case 5: return "低速";
        }
        return "";
    }

    private String getRangeString(int value) {
        switch (value) {
            case 1: return "短";
            case 2: return "中";
            case 3: return "长";
            case 4: return "超长";
        }
        return "";
    }

    private ViewGroup addCell(ViewGroup parent, String title) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.content_item_display_cell, null);
        ((TextView) view.findViewById(android.R.id.title)).setText(title);
        parent.addView(view);
        return view;
    }

    private ViewGroup addCell(ViewGroup parent, int ResId) {
        return addCell(parent, getString(ResId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewGroup getRootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected View[] getAnimFadeViews() {
        return new View[] {
                mAppBarLayout,
                mLinearLayout
        };
    }
}
