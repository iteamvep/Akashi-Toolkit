package rikka.akashitoolkit.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.GalleryAdapter;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipType;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.ShipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.utils.KCStringFormatter;
import rikka.akashitoolkit.utils.Utils;

public class EquipDisplayActivity extends BaseItemDisplayActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_EQUIP_IMPROVE_ID = "EXTRA_EQUIP_IMPROVE_ID";

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Equip mItem;
    private EquipImprovement mItem2;

    private boolean mIsEnemy;

    private Toast mToast;

    @SuppressLint("DefaultLocale")
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

        if (getIntent().getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)) {
            String extra = getIntent().getStringExtra(EXTRA_EXTRA);
            if (extra != null) {
                try {
                    id = Integer.parseInt(extra);
                } catch (Exception ignored) {
                }
            }
        }

        if (getIntent().hasExtra(EXTRA_EQUIP_IMPROVE_ID)) {
            mItem2 = EquipImprovementList.findItemById(this, getIntent().getIntExtra(EXTRA_EQUIP_IMPROVE_ID, -1));
        } else {
            mItem2 = null;
        }

        mIsEnemy = id >= 500;

        mItem = EquipList.findItemById(this, id);
        if (mItem == null) {
            Log.d("EquipDisplayActivity", "No item find? id=" + Integer.toString(id));
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

    @SuppressLint("DefaultLocale")
    private void setViews() {
        setToolBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (mItem.getName() != null) {
            getSupportActionBar().setTitle(mItem.getName().get(this));
        }

        /*((TextView) findViewById(R.id.text_title)).setText(*/
        getSupportActionBar().setSubtitle(String.format(
                "No. %d %s",
                mItem.getId(),
                EquipTypeList.findItemById(this, mItem.getType()).getName(this)
                /*KCStringFormatter.getStars(mItem.getRarity())*/));

        addAttrViews(mLinearLayout);
        //addShipType();
        addItemImprovementViews(mLinearLayout);
        addRemark(mLinearLayout);
        addIllustration(mLinearLayout);
        addIntroduction(mLinearLayout);
        addEquippedShip(mLinearLayout);
    }

    private void addAttrViews(ViewGroup parent) {
        if (mItem.getAttr() == null) {
            return;
        }

        addAttrView(parent, R.string.attr_firepower, mItem.getAttr().getFirepower(), R.drawable.item_attr_fire);
        addAttrView(parent, R.string.attr_aa, mItem.getAttr().getAA(), R.drawable.item_attr_aa);
        addAttrView(parent, R.string.attr_accuracy, mItem.getAttr().getAccuracy(), R.drawable.item_attr_acc);
        addAttrView(parent, R.string.attr_torpedo, mItem.getAttr().getTorpedo(), R.drawable.item_attr_torpedo);
        addAttrView(parent, R.string.attr_boom, mItem.getAttr().getBombing(), R.drawable.item_attr_bomb);
        addAttrView(parent, R.string.attr_asw, mItem.getAttr().getASW(), R.drawable.item_attr_asw);
        addAttrView(parent, R.string.attr_evasion, mItem.getAttr().getEvasion(), R.drawable.item_attr_dodge);
        addAttrView(parent, R.string.attr_los, mItem.getAttr().getLOS(), R.drawable.item_attr_search);
        addAttrView(parent, R.string.attr_armor, mItem.getAttr().getArmor(), R.drawable.item_attr_armor);
        if (mItem.getParentType() == 3) {
            addAttrView(parent, R.string.attr_range_aircraft, mItem.getAttr().getRange(), R.drawable.item_attr_range);
        } else {
            addAttrView(parent, R.string.attr_range, mItem.getAttr().getRange(), R.drawable.item_attr_range);
        }
    }

    private void addShipType() {
        if (mItem.getShipLimit() == null) {
            return;
        }

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
            //textView.setPadding(i == 0 ? Utils.dpToPx(16) : Utils.dpToPx(4), Utils.dpToPx(2), i == 2 ? Utils.dpToPx(16) : Utils.dpToPx(4), Utils.dpToPx(2));
            textView.setPadding((int) getResources().getDimension(R.dimen.item_activity_margin), 0, (int) getResources().getDimension(R.dimen.item_activity_margin), 0);

            linearLayout.addView(textView);

            i++;
        }

    }

    @SuppressLint("DefaultLocale")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_feedback:
                SendReportActivity.sendEmail(this,
                        "Akashi Toolkit 装备数据反馈",
                        String.format("应用版本: %d\n装备名称: %s\n\n请写下您的建议或是指出错误的地方。\n\n",
                                StaticData.instance(this).versionCode,
                                getTaskDescriptionLabel()));
                break;
            case R.id.action_bookmark:
                if (mToast != null) {
                    mToast.cancel();
                }

                if (mItem2 == null) {
                    mItem.setBookmarked(!mItem.isBookmarked());

                    Settings.instance(this)
                            .putBoolean(String.format("equip_%d", mItem.getId()), mItem.isBookmarked());

                    item.setIcon(
                            AppCompatDrawableManager.get().getDrawable(this, mItem.isBookmarked() ? R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp));

                    mToast = Toast.makeText(this, mItem.isBookmarked() ? getString(R.string.bookmark_add) : getString(R.string.bookmark_remove), Toast.LENGTH_SHORT);
                } else {
                    mItem2.setBookmarked(!mItem2.isBookmarked());

                    Settings.instance(this)
                            .putBoolean(String.format("equip_improve_%d", mItem2.getId()), mItem2.isBookmarked());

                    item.setIcon(
                            AppCompatDrawableManager.get().getDrawable(this, mItem2.isBookmarked() ? R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp));

                    mToast = Toast.makeText(this, mItem2.isBookmarked() ? getString(R.string.bookmark_add) : getString(R.string.bookmark_remove), Toast.LENGTH_SHORT);
                }

                mToast.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addRemark(ViewGroup parent) {
        if (mItem.getRemark() != null && mItem.getRemark().length() > 0) {
            ViewGroup cell = addCell(parent, R.string.remark);
            addTextView(cell, Html.fromHtml(mItem.getRemark())).setPadding((int) getResources().getDimension(R.dimen.item_activity_margin), Utils.dpToPx(4), (int) getResources().getDimension(R.dimen.item_activity_margin), 0);
        }
    }

    private void addIntroduction(ViewGroup parent) {
        if (mItem.getIntroduction() != null
                && mItem.getIntroduction().get(this) != null
                && mItem.getIntroduction().get(this).length() > 0) {
            ViewGroup cell = addCell(parent, R.string.introduction);
            addTextView(cell, mItem.getIntroduction().get(this)).setPadding((int) getResources().getDimension(R.dimen.item_activity_margin), 0, (int) getResources().getDimension(R.dimen.item_activity_margin), 0);
        }
    }

    private void addEquippedShip(ViewGroup parent) {
        if (mItem.getShipFrom() == null || mItem.getShipFrom().size() == 0) {
            return;
        }

        parent = addCell(parent, R.string.ship_initial_equip);

        LinearLayout linearLayout = null;

        int i = 0;
        for (Integer shipId : mItem.getShipFrom()) {
            if (i % 3 == 0) {
                linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setWeightSum(3);

                parent.addView(linearLayout);

                i = 0;
            }

            TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.clickable_textview, linearLayout, false);
            view.setText(ShipList.findItemById(this, shipId).getName().get(this));

            final int id = shipId;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EquipDisplayActivity.this, ShipDisplayActivity.class);
                    intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, id);
                    startActivity(intent);
                }
            });

            linearLayout.addView(view);

            i++;
        }
    }

    private ViewGroup addCell(ViewGroup parent, int ResId) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.content_item_display_cell, null);
        ((TextView) view.findViewById(android.R.id.title)).setText(ResId);
        parent.addView(view);
        return view;
    }

    private void addItemImprovementViews(ViewGroup parent) {
        if (mItem.getImprovements() == null) {
            return;
        }

        parent = addCell(parent, R.string.item_improvement_data);

        for (int i = 0; i < mItem.getImprovements().length; i++) {
            Equip.ImprovementEntity improvement = mItem.getImprovements()[i];

            if (mItem.getImprovements().length > 1) {
                String name = "";
                if (improvement.getUpgrade() != null) {
                    Equip equip = EquipList.findItemById(this, improvement.getUpgrade()[0]);
                    if (equip != null) {
                        name = equip.getName().get(this);
                    }
                }

                TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_improvement_branch, parent, false);
                textView.setText(String.format(getString(R.string.improvement_branch), name));
                if (i >= 1) {
                    textView.setPadding(
                            textView.getPaddingLeft(),
                            Utils.dpToPx(24),
                            textView.getPaddingRight(),
                            textView.getPaddingBottom()
                    );
                }
                parent.addView(textView);
            }

            addItemImprovementView(parent, improvement);

        }
    }

    private static Character[] sShortWeekdays = null;

    static {
        String DATE_FORMAT_SHORT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                ? "ccccc" : "ccc";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat shortFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);

        if (sShortWeekdays == null) {
            sShortWeekdays = new Character[7];
        }

        final long aSunday = new GregorianCalendar(2014, Calendar.JULY, 20).getTimeInMillis();

        for (int i = 0; i < 7; i++) {
            final long dayMillis = aSunday + i * DateUtils.DAY_IN_MILLIS;
            sShortWeekdays[i] = shortFormat.format(new Date(dayMillis)).charAt(0);
        }
    }

    private void addItemImprovementView(ViewGroup parent, Equip.ImprovementEntity improvement) {
        Map<Integer, List<Integer>> ships = new HashMap<>();
        Map<Integer, Integer> ids = new LinkedHashMap<>();

        for (List<Integer> entry : improvement.getShips()) {
            for (Integer id : entry) {
                if (id >= 0) {
                    ids.put(id, 0);
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : ids.entrySet()) {
            int id = entry.getKey();

            int data = 0;
            for (int i = 0; i < 7; i++) {
                List<Integer> e = improvement.getShips().get(i);
                for (Integer id2 : e) {
                    if (id2 == id) {
                        data |= (1 << i);
                    }
                }
            }

            List<Integer> list = ships.get(data);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(id);
            ships.put(data, list);
        }

        for (Map.Entry<Integer, List<Integer>> entry : ships.entrySet()) {
            int flag = entry.getKey();
            List<Integer> ship = entry.getValue();

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding((int) getResources().getDimension(R.dimen.item_activity_margin), Utils.dpToPx(8), (int) getResources().getDimension(R.dimen.item_activity_margin), Utils.dpToPx(0));
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);

            for (int i = 0; i < 7; i++) {
                TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.day_cricle, parent, false);
                view.setText(sShortWeekdays[i].toString());
                view.setEnabled((flag & 1 << i) > 0);
                linearLayout.addView(view);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    ViewCompat.setBackgroundTintList(view, ContextCompat.getColorStateList(this, R.color.day_bg_color));
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int id : ship) {
                if (sb.length() > 0) {
                    sb.append(" / ");
                }

                if (id == 0) {
                    sb.append(getString(R.string.improvement_any));
                } else {
                    Ship s = ShipList.findItemById(this, id);
                    if (s != null)
                        sb.append(s.getName().get(this));
                }
            }
            TextView view = new TextView(this);
            view.setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
            view.setText(sb.toString());
            view.setTextSize(16);
            linearLayout.addView(view);

            parent.addView(linearLayout);
        }

        addItemImprovementInsideView(parent, 0, getString(R.string.improvement_resource), improvement.getCost());

        if (improvement.getItem() != null) {
            addItemImprovementInsideView(parent, 1, "~ ★+6", improvement.getItem());
        }
        if (improvement.getItem2() != null) {
            addItemImprovementInsideView(parent, 1, "~ MAX", improvement.getItem2());
        }
        if (improvement.getUpgrade() != null && improvement.getItem3() != null) {
            addItemImprovementInsideView(parent, 1, getString(R.string.improvement_upgrade), improvement.getItem3());
        }
        if (improvement.getUpgrade() != null) {
            addItemImprovementInsideView(parent, getString(R.string.improvement_upgrade_to), improvement.getUpgrade());
        }
    }

    private void addItemImprovementInsideView(ViewGroup parent, String title, int[] id) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_improvement_levelup, null);
        ((TextView) linearLayout.findViewById(android.R.id.title)).setText(title);

        final Equip item = EquipList.findItemById(this, id[0]);
        if (item == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(item.getName().get(this));
        if (id[1] > 0) {
            sb.append(" ★+").append(id[1]);
        }
        ((TextView) linearLayout.findViewById(R.id.text_number_0)).setText(sb.toString());

        ((View) linearLayout.findViewById(R.id.text_number_0).getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EquipDisplayActivity.this, EquipDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                BaseItemDisplayActivity.start(EquipDisplayActivity.this, intent);
            }
        });

        EquipTypeList.setIntoImageView((ImageView) linearLayout.findViewById(R.id.imageView), item.getIcon());

        parent.addView(linearLayout);
    }

    @SuppressLint("DefaultLocale")
    private void addItemImprovementInsideView(ViewGroup parent, int type, String title, int[] res) {
        if (type == 0) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_improvement_cost_base, null);
            ((TextView) linearLayout.findViewById(android.R.id.title)).setText(title);
            ((TextView) linearLayout.findViewById(R.id.text_number_0)).setText(String.format("%d", res[0]));
            ((TextView) linearLayout.findViewById(R.id.text_number_1)).setText(String.format("%d", res[1]));
            ((TextView) linearLayout.findViewById(R.id.text_number_2)).setText(String.format("%d", res[2]));
            ((TextView) linearLayout.findViewById(R.id.text_number_3)).setText(String.format("%d", res[3]));
            parent.addView(linearLayout);
        } else {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_improvement_cost_item, null);
            ((TextView) linearLayout.findViewById(android.R.id.title)).setText(title);
            ((TextView) linearLayout.findViewById(R.id.text_number_0)).setText(String.format("%d(%d)", res[0], res[1]));
            ((TextView) linearLayout.findViewById(R.id.text_number_1)).setText(String.format("%d(%d)", res[2], res[3]));

            if (res.length == 6) {
                final Equip item = EquipList.findItemById(this, res[5]);
                if (item != null) {
                    ((TextView) linearLayout.findViewById(R.id.text_number_2)).setText(
                            String.format("%s ×%d",
                                    item.getName().get(this),
                                    res[4]));

                    ((View) linearLayout.findViewById(R.id.text_number_2).getParent()).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(EquipDisplayActivity.this, EquipDisplayActivity.class);
                            intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                            startActivity(intent);
                        }
                    });

                    EquipTypeList.setIntoImageView((ImageView) linearLayout.findViewById(R.id.imageView), item.getIcon());
                } else {
                    Log.d("EquipDisplayActivity", "Equip not found: " + res[5]);
                }
            } else {
                linearLayout.findViewById(R.id.linearLayout).setVisibility(View.GONE);
            }
            parent.addView(linearLayout);
        }
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

    private LinearLayout mCurAttrLinearLayout;
    private int attr = 0;

    @SuppressLint("DefaultLocale")
    private void addAttrView(ViewGroup parent, @StringRes int title, int value, int icon) {
        if (value == 0) {
            return;
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

        if (title == R.string.attr_range) {
            ((TextView) cell.findViewById(R.id.textView2)).setText(KCStringFormatter.getRange(cell.getContext(), value));
        } else if (title == R.string.attr_range_aircraft) {
            ((TextView) cell.findViewById(R.id.textView2)).setText(String.format("%d", value));
        } else {
            ((TextView) cell.findViewById(R.id.textView2)).setText(String.format("%s%d", value > 0 ? "+" : "", value));
            if (value < 0)
                ((TextView) cell.findViewById(R.id.textView2)).setTextColor(ContextCompat.getColor(this, R.color.material_red_300));
        }

        if (attr % 2 == 0) {
            mCurAttrLinearLayout = null;
        }
    }

    @SuppressLint("DefaultLocale")
    private void addIllustration(ViewGroup parent) {
        if (mIsEnemy) {
            return;
        }

        parent = addCell(parent, R.string.illustration);

        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.illustrations_container, parent);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_container);

        final List<String> urlList = new ArrayList<>();
        urlList.add(Utils.getKCWikiFileUrl(String.format("Soubi%03dFull.png", mItem.getId())));
        urlList.add(Utils.getKCWikiFileUrl(String.format("Soubi%03dArnament.png", mItem.getId())));
        urlList.add(Utils.getKCWikiFileUrl(String.format("Soubi%03dFairy.png", mItem.getId())));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                if (parent.getChildLayoutPosition(view) < parent.getAdapter().getItemCount() - 1) {
                    outRect.right = Utils.dpToPx(8);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final GalleryAdapter adapter = new GalleryAdapter() {
            @Override
            public void onItemClicked(View v, List<String> data, int position) {
                ImagesActivity.start(v.getContext(), data, position, getTaskDescriptionLabel());
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                ImageView imageView = (ImageView) holder.itemView;
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dpToPx(150), Utils.dpToPx(150)));
            }
        };
        adapter.setUrls(urlList);
        recyclerView.setAdapter(adapter);


        /*for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);

            Log.d(getClass().getSimpleName(), url);

            ImageView imageView = (ImageView) LayoutInflater.from(this)
                    .inflate(R.layout.equip_illustrations, recyclerView, false)
                    .findViewById(R.id.imageView);
            recyclerView.addView(imageView);

            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EquipDisplayActivity.this, ImagesActivity.class);
                    intent.putStringArrayListExtra(ImagesActivity.EXTRA_URL, (ArrayList<String>) urlList);
                    intent.putExtra(ImagesActivity.EXTRA_POSITION, finalI);
                    intent.putExtra(ImagesActivity.EXTRA_TITLE, getTaskDescriptionLabel());
                    startActivity(intent);
                }
            });

            Glide.with(this)
                    .load(Utils.getGlideUrl(url))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(imageView);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ship_display, menu);

        if (!mIsEnemy) {
            if (mItem2 == null) {
                menu.findItem(R.id.action_bookmark).setIcon(
                        AppCompatDrawableManager.get().getDrawable(this, mItem.isBookmarked() ? R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp));
            } else {
                menu.findItem(R.id.action_bookmark).setIcon(
                        AppCompatDrawableManager.get().getDrawable(this, mItem2.isBookmarked() ? R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp));
            }
        } else {
            menu.findItem(R.id.action_bookmark).setVisible(false);
        }

        return true;
    }
}
