package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
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
import rikka.akashitoolkit.model.ItemImprovement;
import rikka.akashitoolkit.model.ShipType;
import rikka.akashitoolkit.staticdata.ItemImprovementList;
import rikka.akashitoolkit.staticdata.ItemTypeList;
import rikka.akashitoolkit.staticdata.ItemList;
import rikka.akashitoolkit.staticdata.QuestList;
import rikka.akashitoolkit.staticdata.ShipTypeList;
import rikka.akashitoolkit.utils.KCStringFormatter;
import rikka.akashitoolkit.utils.Utils;

public class ItemDisplayActivity extends BaseItemDisplayActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Item mItem;
    private LinearLayout mItemAttrContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = -1;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            id = intent.getIntExtra(EXTRA_ITEM_ID, 0);
        }

        if (intent.getData() != null) {
            try {
                id = Integer.parseInt(intent.getData().toString().split("/")[3]);
            } catch (Exception ignored) {
            }
        }

        mItem = ItemList.findItemById(this, id);
        if (mItem == null) {
            Log.d("QAQ", "No item find? id=" + Integer.toString(id));
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

    @Override
    protected String getTaskDescriptionLabel() {
        return mItem.getName().get(this);
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

    private void setViews() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Utils.isNightMode(getResources())) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);
            mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            mToolbar.setSubtitleTextColor(Color.parseColor("#ffe0e0e0"));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp_dark);
            mToolbar.setTitleTextColor(Color.parseColor("#000000"));
            mToolbar.setSubtitleTextColor(Color.parseColor("#ff757575"));
        }

        if (mItem.getName() != null) {
            getSupportActionBar().setTitle(mItem.getName().get(this));
        }

        /*((TextView) findViewById(R.id.text_title)).setText(*/
        getSupportActionBar().setSubtitle(String.format(
                "No. %d %s %s",
                mItem.getId(),
                ItemTypeList.findItemById(this, mItem.getSubType()).getName(),
                KCStringFormatter.getStars(mItem.getRarity())));

        addAttrView(mLinearLayout, "火力", mItem.getAttr().getFire(), R.drawable.item_attr_fire);
        addAttrView(mLinearLayout, "对空", mItem.getAttr().getAa(), R.drawable.item_attr_aa);
        addAttrView(mLinearLayout, "命中", mItem.getAttr().getAcc(), R.drawable.item_attr_acc);
        addAttrView(mLinearLayout, "雷装", mItem.getAttr().getTorpedo(), R.drawable.item_attr_torpedo);
        addAttrView(mLinearLayout, "爆装", mItem.getAttr().getBomb(), R.drawable.item_attr_bomb);
        addAttrView(mLinearLayout, "对潜", mItem.getAttr().getAsw(), R.drawable.item_attr_asw);
        addAttrView(mLinearLayout, "回避", mItem.getAttr().getEvasion(), R.drawable.item_attr_dodge);
        addAttrView(mLinearLayout, "索敌", mItem.getAttr().getSearch(), R.drawable.item_attr_search);
        addAttrView(mLinearLayout, "装甲", mItem.getAttr().getArmor(), R.drawable.item_attr_armor);
        addAttrView(mLinearLayout, "射程", mItem.getAttr().getRange(), R.drawable.item_attr_range);

        addShipType();

        addItemImprovementView();

        addOther(mLinearLayout);
    }

    private void addShipType() {
        ViewGroup parent = addCell(mLinearLayout, R.string.ship_can_equip);

        List<ShipType> list = ShipTypeList.get(this);
        LinearLayout linearLayout = null;

        int i = 0;
        for (Integer shipType :
                mItem.getShipLimit()) {
            if (i % 3 == 0) {
                linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setWeightSum(3);

                parent.addView(linearLayout);

                i = 0;
            }

            TextView textView = new TextView(this);
            textView.setText(list.get(shipType).getName().get(this));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, Utils.dpToPx(36), 1));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setSingleLine(true);
            //textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(i == 0 ? Utils.dpToPx(16) : Utils.dpToPx(4), Utils.dpToPx(2), i == 2 ? Utils.dpToPx(16) : Utils.dpToPx(4), Utils.dpToPx(2));
            linearLayout.addView(textView);

            i ++;
        }

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

    private void addOther(ViewGroup parent) {
        if (mItem.getRemark() != null && mItem.getRemark().length() > 0) {
            ViewGroup cell = addCell(parent, R.string.remark);
            addTextView(cell, Html.fromHtml(mItem.getRemark())).setPadding(Utils.dpToPx(16), Utils.dpToPx(4), Utils.dpToPx(16), 0);
        }

        if (mItem.getGet().getQuest() != null ||
                mItem.getGet().getRank() != null ||
                mItem.getGet().getEvent() != null) {
            ViewGroup cell = addCell(parent, R.string.item_get);

            if (mItem.getGet().getQuest() != null) {
                StringBuilder sb = new StringBuilder();
                //sb.append(getString(R.string.quest)).append(":\n");

                for (String item:
                    mItem.getGet().getQuest().split(",")) {
                    int id = (int) Float.parseFloat(item);
                    QuestList.Quest quest = QuestList.findItemById(this, id);
                    if (quest != null && quest.getCode() != null) {
                        sb.append(quest.getCode()).append(' ').append(quest.getTitle()).append('\n');
                    } else {
                        sb.append(item);
                    }
                }

                addTextView(cell, sb.toString()).setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
            }


            if (mItem.getGet().getEvent() != null) {
                StringBuilder sb = new StringBuilder();

                //sb.append(getString(R.string.quest)).append(":\n");

                for (String item:
                        mItem.getGet().getEvent().split(",")) {
                    sb.append(item).append('\n');
                }

                addTextView(cell, sb.toString()).setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
            }


            if (mItem.getGet().getRank() != null) {
                //sb.append(mItem.getGet().getRank());
                StringBuilder sb = new StringBuilder();
                for (String item:
                        mItem.getGet().getRank().split(",")) {
                    sb.append(item).append('\n');
                }

                addTextView(cell, sb.toString()).setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
            }
        }

        if (mItem.getIntroduction() != null
                && mItem.getIntroduction().get(this) != null
                && mItem.getIntroduction().get(this).length() > 0) {
            ViewGroup cell = addCell(parent, R.string.introduction);
            addTextView(cell, Html.fromHtml(mItem.getIntroduction().get(this))).setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
        }
    }

    private ViewGroup addCell(ViewGroup parent, int ResId) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.content_item_display_cell, null);
        ((TextView) view.findViewById(android.R.id.title)).setText(ResId);
        parent.addView(view);
        return view;
    }

    private void addItemImprovementView() {
        ItemImprovement itemImprovement = ItemImprovementList.findItemById(this, mItem.getId());
        if (itemImprovement == null || itemImprovement.getSecretary() == null) {
            return;
        }

        ViewGroup parent = addCell(mLinearLayout, R.string.item_improvement_data);

        for (ItemImprovement.SecretaryEntity entry:
                itemImprovement.getSecretary()) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(Utils.dpToPx(16), Utils.dpToPx(4), Utils.dpToPx(16), Utils.dpToPx(4));

            for (int i = 0; i < entry.getDay().size(); i++) {
                TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.day_cricle, null);
                view.setText(DAY[i]);
                view.setEnabled(entry.getDay().get(i));
                linearLayout.addView(view);
            }

            TextView view = new TextView(this);
            view.setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
            view.setText(entry.getName());
            view.setTextSize(16);
            linearLayout.addView(view);

            parent.addView(linearLayout);
        }


        if (mItem.getImprovement() == null) {
            return;
        }

        getItemImprovementInsideView(parent, 0, "必要资源", mItem.getImprovement().getResource().getBase());

        for (int i = 0; i < 3; i++) {
            List<Integer> list = mItem.getImprovement().getResource().getItem().get(i);

            getItemImprovementInsideView(parent, 1, i == 0 ? "~ ★+6" : i == 1 ? "~ MAX" : "升级", list);
        }

        getItemImprovementInsideView(parent, "升级为", mItem.getImprovement().getLevelup());
    }

    private void getItemImprovementInsideView(ViewGroup parent, String title, int id) {
        if (id <= 0) {
            return;
        }
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_improvement_levelup, null);
        ((TextView) linearLayout.findViewById(android.R.id.title)).setText(title);

        final Item item = ItemList.findItemById(this, id);
        ((TextView) linearLayout.findViewById(R.id.text_number_0)).setText(item.getName().get(this));
        /*((ImageView) linearLayout.findViewById(R.id.imageView))
                .setImageResource(ItemTypeList.getResourceId(this, item.getIcon()));*/

        ((View) linearLayout.findViewById(R.id.text_number_0).getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDisplayActivity.this, ItemDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                startActivity(intent);
            }
        });

        ItemTypeList.setIntoImageView((ImageView) linearLayout.findViewById(R.id.imageView), item.getIcon());

        parent.addView(linearLayout);
    }

    private void getItemImprovementInsideView(ViewGroup parent, int type, String title, List<Integer> res) {
        if (type == 0) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_improvement_cost_base, null);
            ((TextView) linearLayout.findViewById(android.R.id.title)).setText(title);
            ((TextView) linearLayout.findViewById(R.id.text_number_0)).setText(String.format("%d", res.get(0)));
            ((TextView) linearLayout.findViewById(R.id.text_number_1)).setText(String.format("%d", res.get(1)));
            ((TextView) linearLayout.findViewById(R.id.text_number_2)).setText(String.format("%d", res.get(2)));
            ((TextView) linearLayout.findViewById(R.id.text_number_3)).setText(String.format("%d", res.get(3)));
            parent.addView(linearLayout);
        } else {
            if (res.get(0) == 0) {
                return;
            }
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_improvement_cost_item, null);
            ((TextView) linearLayout.findViewById(android.R.id.title)).setText(title);
            ((TextView) linearLayout.findViewById(R.id.text_number_0)).setText(String.format("%d(%d)", res.get(0), res.get(1)));
            ((TextView) linearLayout.findViewById(R.id.text_number_1)).setText(String.format("%d(%d)", res.get(2), res.get(3)));

            if (res.get(5) > 0) {
                final Item item = ItemList.findItemById(this, res.get(4));
                ((TextView) linearLayout.findViewById(R.id.text_number_2)).setText(
                        String.format("%s ×%d",
                                item.getName().get(this),
                                res.get(5)));

                ((View) linearLayout.findViewById(R.id.text_number_2).getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ItemDisplayActivity.this, ItemDisplayActivity.class);
                        intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                        startActivity(intent);
                    }
                });

                /*((ImageView) linearLayout.findViewById(R.id.imageView))
                        .setImageResource(ItemTypeList.getResourceId(this, item.getIcon()));*/
                ItemTypeList.setIntoImageView((ImageView) linearLayout.findViewById(R.id.imageView), item.getIcon());
            } else {
                linearLayout.findViewById(R.id.linearLayout).setVisibility(View.GONE);
            }
            parent.addView(linearLayout);
        }
    }

    private static final String[] DAY = {"日", "一", "二", "三", "四", "五", "六"};

    private TextView addTextView(ViewGroup parent, String text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        parent.addView(textView);
        return textView;
    }

    private TextView addTextView(ViewGroup parent, String text) {
        return addTextView(parent, text, 16);
    }

    private TextView addTextView(ViewGroup parent, Spanned text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        parent.addView(textView);
        return textView;
    }

    private TextView addTextView(ViewGroup parent, Spanned text) {
        return addTextView(parent, text, 16);
    }

    private LinearLayout mCurAttrLinearLayout;
    private int attr = 0;
    private void addAttrView(ViewGroup parent, String title, int value, int icon) {
        if (value == 0) {
            return;
        }

        mItemAttrContainer = (LinearLayout) parent;

        /*if (mItemAttrContainer == null) {
            mItemAttrContainer = (LinearLayout) addCell(mLinearLayout, R.string.attributes);
            parent = mItemAttrContainer;
        }*/

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
            ((TextView) cell.findViewById(R.id.textView2)).setText(KCStringFormatter.getRange(value));
        } else {
            ((TextView) cell.findViewById(R.id.textView2)).setText(String.format("%s%d", value > 0 ? "+" : "", value));
            ((TextView) cell.findViewById(R.id.textView2)).setTextColor(ContextCompat.getColor(this, value > 0 ? R.color.material_green_300 : R.color.material_red_300));
        }

        //((ImageView) cell.findViewById(R.id.imageView)).setImageDrawable(ContextCompat.getDrawable(this, icon));

        if (attr % 2 == 0) {
            mCurAttrLinearLayout = null;
        }
    }
}
