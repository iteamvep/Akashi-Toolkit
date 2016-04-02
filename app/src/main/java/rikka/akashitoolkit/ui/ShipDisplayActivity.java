package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
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
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            mId = intent.getIntExtra(EXTRA_ITEM_ID, 0);
            mItem = ShipList.findItemById(this, mId);
            if (mItem == null) {
                Log.d("QAQ", "No item find? id=" + Integer.toString(mId));
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
                "%s",
                /*mItem.getId_illustrations(),*/
                ShipList.shipType[mItem.getType()]
                /*formatStars(mItem.getRarity()))*/));

        addCell(mLinearLayout, "属性");
        TabLayout tabLayout = new TabLayout(this);
        mLinearLayout.addView(tabLayout);

        ViewPager viewPager = (ViewPager) LayoutInflater.from(this).inflate(R.layout.content_viewpager, mLinearLayout, true).findViewById(R.id.view_pager);
        //ViewPager viewPager = new ViewPager(this);
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32) * 6));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", position);
                bundle.putInt("ITEM", mId);
                return bundle;
            }
        };
        adapter.addFragment(AttrFragment.class, "初期值");
        adapter.addFragment(AttrFragment.class, "Lv.99");
        adapter.addFragment(AttrFragment.class, "LV.155");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        addEquip(mLinearLayout);
        addRemodel(mLinearLayout);
    }

    private void addRemodel(ViewGroup parent) {
        if (mItem.getRemodel() != null) {
            parent = addCell(parent, "改造");

            StringBuilder sb = new StringBuilder();
            Ship cur = mItem;
            while (cur.getRemodel().getId_from() != 0) {
                cur = ShipList.findItemById(this, cur.getRemodel().getId_from());
            }

            while (true) {
                sb.append(cur.getName().getZh_cn());

                if (cur.getRemodel().getId_from() != 0) {
                    Ship prev = ShipList.findItemById(this, cur.getRemodel().getId_from());
                    if (prev.getRemodel().getBlueprint() == 0) {
                        sb.append(String.format(" (%d)", prev.getRemodel().getLevel()));
                    } else {
                        sb.append(String.format(" (%d + 改装设计图)", prev.getRemodel().getLevel()));
                    }
                }

                if (cur.getRemodel().getId_to() == 0 ||
                        cur.getRemodel().getId_from() == cur.getRemodel().getId_to()) {
                    break;
                }

                cur = ShipList.findItemById(this, cur.getRemodel().getId_to());
                if (cur.getRemodel().getId_from() != cur.getRemodel().getId_to()) {
                    sb.append(" → ");
                } else {
                    sb.append(" ↔ ");
                }
            }
            addTextView(parent, sb.toString());
        }
    }

    private void addEquip(ViewGroup parent) {
        parent = addCell(parent, "初始装备 & 搭载量");

        List<Integer> equipId = mItem.getEquip().get(0);
        List<Integer> equipSlot = mItem.getEquip().get(1);

        for (int i = 0; i < mItem.getSlot(); i++) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_item, null);

            if (equipId.get(i) > 0) {
                Item item = ItemList.findItemById(this, equipId.get(i));
                if (item == null) {
                    ((TextView) view.findViewById(android.R.id.title)).setText(String.format("找不到装备 (id: %d)", equipId.get(i)));
                    view.findViewById(android.R.id.title).setEnabled(false);
                } else {
                    ((TextView) view.findViewById(android.R.id.title)).setText(item.getName().getZh_cn());
                    ItemTypeList.setIntoImageView((ImageView) view.findViewById(R.id.imageView), item.getIcon());
                }
            } else {
                ((TextView) view.findViewById(android.R.id.title)).setText("未装备");
                view.findViewById(android.R.id.title).setEnabled(false);
            }

            ((TextView) view.findViewById(R.id.textView)).setText(Integer.toString(equipSlot.get(i)));

            parent.addView(view);
        }
    }

    private static String getSpeedString(int value) {
        switch (value) {
            case 10: return "高速";
            case 5: return "低速";
        }
        return "";
    }

    private static String getRangeString(int value) {
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

    public static class AttrFragment extends Fragment {
        private Ship mItem;
        private LinearLayout mLinearLayout;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int i = args.getInt("TYPE");
            int id = args.getInt("ITEM");

            mLinearLayout = new LinearLayout(getContext());
            mLinearLayout.setOrientation(LinearLayout.VERTICAL);
            mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLinearLayout.setPadding(0, Utils.dpToPx(4), 0, 0);
            mItem = ShipList.findItemById(getContext(), id);
            addAttrView(mLinearLayout, "耐久", mItem.getAttr().getHp(), i == 2 ? 2 : 0, R.drawable.item_attr_hp);
            addAttrView(mLinearLayout, "火力", mItem.getAttr().getFire(), i, R.drawable.item_attr_fire);
            addAttrView(mLinearLayout, "对空", mItem.getAttr().getAa(), i, R.drawable.item_attr_aa);
            addAttrView(mLinearLayout, "雷装", mItem.getAttr().getTorpedo(), i, R.drawable.item_attr_torpedo);
            addAttrView(mLinearLayout, "装甲", mItem.getAttr().getArmor(), i, R.drawable.item_attr_armor);
            addAttrView(mLinearLayout, "对潜", mItem.getAttr().getAsw(), i, R.drawable.item_attr_asw);
            addAttrView(mLinearLayout, "回避", mItem.getAttr().getEvasion(), i, R.drawable.item_attr_dodge);
            addAttrView(mLinearLayout, "索敌", mItem.getAttr().getSearch(), i, R.drawable.item_attr_search);
            addAttrView(mLinearLayout, "航速", mItem.getAttr().getSpeed(), R.drawable.item_attr_speed);
            addAttrView(mLinearLayout, "射程", mItem.getAttr().getRange(), R.drawable.item_attr_range);
            addAttrView(mLinearLayout, "运", mItem.getAttr().getLuck(), i, R.drawable.item_attr_luck);
            return mLinearLayout;
        }

        private void addAttrView(ViewGroup parent, String title, List<Integer> value, int i, int icon) {
            if (value.size() <= i) {
                i = value.size() - 1;
            }

            if (value.size() == 2) {
                addAttrView(parent, title, String.format("%d / %d", value.get(0), value.get(1)), icon);
            } else {
                addAttrView(parent, title, Integer.toString(value.get(i)), icon);
            }
        }

        private void addAttrView(ViewGroup parent, String title, int value, int icon) {
            if (value == 0) {
                return;
            }
            if (icon == R.drawable.item_attr_range) {
                addAttrView(parent, title, getRangeString(value), icon);
            } else if (icon == R.drawable.item_attr_speed) {
                addAttrView(parent, title, getSpeedString(value), icon);
            } else {
                addAttrView(parent, title, Integer.toString(value), icon);
            }
        }

        private LinearLayout mCurAttrLinearLayout;
        private int attr = 0;

        private void addAttrView(ViewGroup parent, String title, String value, int icon) {
            attr ++;

            if (mCurAttrLinearLayout == null) {
                mCurAttrLinearLayout = new LinearLayout(getContext());
                mCurAttrLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                mCurAttrLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32)));
                mCurAttrLinearLayout.setBaselineAligned(false);
                mCurAttrLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
                LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_attr_cell, mCurAttrLinearLayout);
                parent.addView(mCurAttrLinearLayout);
            }

            View cell = mCurAttrLinearLayout
                    .findViewById((attr % 2 == 0) ? R.id.item_attr_cell2 : R.id.item_attr_cell);

            cell.setVisibility(View.VISIBLE);

            ((TextView) cell.findViewById(R.id.textView)).setText(title);
            ((TextView) cell.findViewById(R.id.textView2)).setText(value);
            ((ImageView) cell.findViewById(R.id.imageView)).setImageDrawable(ContextCompat.getDrawable(getContext(), icon));

            if (attr % 2 == 0) {
                mCurAttrLinearLayout = null;
            }
        }
    }
}
