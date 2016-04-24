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
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.utils.KCStringFormatter;
import rikka.akashitoolkit.utils.MySpannableFactory;
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

        mId = -1;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            mId = intent.getIntExtra(EXTRA_ITEM_ID, 0);
        } else if (intent.getData() != null) {
            try {
                mId = Integer.parseInt(intent.getData().toString().split("/")[3]);
            } catch (Exception ignored) {
            }
        }

        mItem = ShipList.findItemById(this, mId);
        if (mItem == null) {
            Log.d("QAQ", "Ship not found? id=" + Integer.toString(mId));
            finish();
            return;
        }

        setContentView(R.layout.activity_ship_display);

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

    private void setViews() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle(
                String.format("No.%s %s",
                        mItem.getWiki_id(),
                        ShipList.shipType[mItem.getType()]));

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

        TabLayout tabLayout = new TabLayout(this);
        mLinearLayout.addView(tabLayout);

        ViewPager viewPager = (ViewPager) LayoutInflater.from(this).inflate(R.layout.content_viewpager, mLinearLayout, true).findViewById(R.id.view_pager);
        //ViewPager viewPager = new ViewPager(this);
        viewPager.setPadding(0, Utils.dpToPx(4), 0, 0);
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32) * 6 + Utils.dpToPx(16)));
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
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));

        addEquip(mLinearLayout);
        addRemodel(mLinearLayout);
        addIllustration(mLinearLayout);
    }

    private void addIllustration(ViewGroup parent) {
        parent = addCell(parent, "图鉴");

        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_illustrations_container, parent);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.content_container);

        List<String> urlList = new ArrayList<>();
        urlList.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sIllust.png", mItem.getWiki_id().replace("a", ""))));
        urlList.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sDmgIllust.png", mItem.getWiki_id().replace("a", ""))));

        for (String url :
                urlList) {
            Log.d(MapActivity.class.getSimpleName(), url);

            ImageView imageView = (ImageView) LayoutInflater.from(this)
                    .inflate(R.layout.item_illustrations, container, false)
                    .findViewById(R.id.imageView);
            container.addView(imageView);

            Glide.with(this)
                    .load(url)
                    .into(imageView);
        }
    }

    private void addRemodel(ViewGroup parent) {
        if (mItem.getRemodel() != null) {
            parent = addCell(parent, "改造");
            GridLayout gridLayout = new GridLayout(this);
            gridLayout.setColumnCount(2);

            //StringBuilder sb = new StringBuilder();
            Ship cur = mItem;
            while (cur.getRemodel().getId_from() != 0) {
                cur = ShipList.findItemById(this, cur.getRemodel().getId_from());
            }

            while (true) {
                //sb.append(KCStringFormatter.getLinkShip(cur.getId(), cur.getName().get(this)));
                StringBuilder sb = new StringBuilder();
                sb.append(cur.getName().get(this));
                ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_remodel_item, null);
                view.setLayoutParams(
                        new GridLayout.LayoutParams(
                                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        )
                );
                gridLayout.addView(view);

                final Ship finalCur = cur;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShipDisplayActivity.this, ShipDisplayActivity.class);
                        intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, finalCur.getId());
                        startActivity(intent);
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("akashitoolkit://ship/" + Integer.toString(finalCur.getId()))));
                    }
                });

                if (cur.getRemodel().getId_from() != 0) {
                    Ship prev = ShipList.findItemById(this, cur.getRemodel().getId_from());
                    if (prev.getRemodel().getBlueprint() == 0) {
                        sb.append(String.format(" (%d)", prev.getRemodel().getLevel()));
                    } else {
                        sb.append(String.format(" (%d + 改装设计图)", prev.getRemodel().getLevel()));
                    }
                }

                ((TextView) view.findViewById(android.R.id.title)).setText(sb.toString());

                if (cur.getRemodel().getId_to() == 0 ||
                        cur.getRemodel().getId_from() == cur.getRemodel().getId_to()) {

                    view.findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
                    break;
                }

                cur = ShipList.findItemById(this, cur.getRemodel().getId_to());
                if (cur.getRemodel().getId_from() != cur.getRemodel().getId_to()) {
                    //sb.append(" → ");
                } else {
                    //sb.append(" ↔ ");
                    ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                }


            }
            //addTextView(parent, Html.fromHtml(sb.toString()));
            parent.addView(gridLayout);
        }
    }

    private void addEquip(ViewGroup parent) {
        parent = addCell(parent, "初始装备 & 搭载量");

        List<Integer> equipId = mItem.getEquip().get(0);
        List<Integer> equipSlot = mItem.getEquip().get(1);

        for (int i = 0; i < mItem.getSlot(); i++) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_item, null);

            if (equipId.get(i) > 0) {
                final Equip item = EquipList.findItemById(this, equipId.get(i));
                if (item == null) {
                    ((TextView) view.findViewById(android.R.id.title)).setText(String.format("找不到装备 (id: %d)", equipId.get(i)));
                    view.findViewById(android.R.id.title).setEnabled(false);
                } else {
                    //((TextView) view.findViewById(android.R.id.title)).setSpannableFactory(MySpannableFactory.getInstance());
                    //((TextView) view.findViewById(android.R.id.title)).setText(KCStringFormatter.getLinkEquip(equip));
                    //((TextView) view.findViewById(android.R.id.title)).setMovementMethod(new LinkMovementMethod());
                    //view.findViewById(android.R.id.title).setClickable(true);
                    ((TextView) view.findViewById(android.R.id.title)).setText(item.getName().get(this));
                    //EquipTypeList.setIntoImageView((ImageView) view.findViewById(R.id.imageView), equip.getIcon());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ShipDisplayActivity.this, EquipDisplayActivity.class);
                            intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                            startActivity(intent);
                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("akashitoolkit://equip/"+Integer.toString(equip.getId()))));
                        }
                    });
                }
            } else {
                ((TextView) view.findViewById(android.R.id.title)).setText("未装备");
                view.findViewById(android.R.id.title).setEnabled(false);
            }

            ((TextView) view.findViewById(R.id.textView)).setText(Integer.toString(equipSlot.get(i)));

            parent.addView(view);
        }
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
        textView.setMovementMethod(new LinkMovementMethod());
        textView.setClickable(true);
        textView.setSpannableFactory(MySpannableFactory.getInstance());
        //textView.setLinkTextColor(getColor(R.color.material_red_300));
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
            /*attr = 0;
            mCurAttrLinearLayout = null;
            addAttrView(mLinearLayout, "燃料消耗", mItem.getRemodel().getCost().get(0), 0);
            addAttrView(mLinearLayout, "弹药消耗", mItem.getRemodel().getCost().get(1), 0);*/

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
                addAttrView(parent, title, KCStringFormatter.getRange(value), icon);
            } else if (icon == R.drawable.item_attr_speed) {
                addAttrView(parent, title, KCStringFormatter.getSpeed(value), icon);
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
            //((ImageView) cell.findViewById(R.id.imageView)).setImageDrawable(ContextCompat.getDrawable(getContext(), icon));

            if (attr % 2 == 0) {
                mCurAttrLinearLayout = null;
            }
        }
    }
}
