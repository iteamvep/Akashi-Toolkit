package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.staticdata.ItemList;

public class ItemDisplayActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        int id;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            id = intent.getIntExtra(EXTRA_ITEM_ID, 0);
        } else {
            finish();
            return;
        }

        Item item = ItemList.findItemById(this, id);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        if (item.getName() != null) {
            getSupportActionBar().setTitle(item.getName());
        }
        addTextView(mLinearLayout, formatStars(item.getRarity()));
        addAttrTextView(mLinearLayout, "火力", item.getAttr().getFire());
        addAttrTextView(mLinearLayout, "对空", item.getAttr().getAa());
        addAttrTextView(mLinearLayout, "命中", item.getAttr().getAcc());
        addAttrTextView(mLinearLayout, "雷装", item.getAttr().getTorpedo());
        addAttrTextView(mLinearLayout, "爆装", item.getAttr().getBomb());
        addAttrTextView(mLinearLayout, "对潜", item.getAttr().getAs());
        addAttrTextView(mLinearLayout, "回避", item.getAttr().getDodge());
        addAttrTextView(mLinearLayout, "索敌", item.getAttr().getSearch());
        addRangeTextView(mLinearLayout, "射程", item.getAttr().getRange());
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

    private String formatStars(int value) {
        String star = "";
        while (value > 0) {
            star += "★";
            value --;
        }
        return star;
    }

    private void addRangeTextView(ViewGroup parent, String attrName, int value) {
        if (value == 0) {
            return;
        }

        addTextView(parent, formatRangeString(attrName, value));
    }

    private String formatRangeString(String attrName, int value) {
        String range = "";
        switch (value) {
            case 1: range = "短"; break;
            case 2: range = "中"; break;
            case 3: range = "长"; break;
            case 4: range = "超长"; break;
        }
        return String.format("%s: %s", attrName, range);
    }

    private void addAttrTextView(ViewGroup parent, String attrName, int value) {
        if (value == 0) {
            return;
        }

        addTextView(parent, formatAttrString(attrName, value));
    }

    private String formatAttrString(String attrName, int value) {
        return String.format("%s: %s%d", attrName, value > 0 ? "+" : "", value);
    }

    private void addTextView(ViewGroup parent, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        parent.addView(textView);
    }
}
