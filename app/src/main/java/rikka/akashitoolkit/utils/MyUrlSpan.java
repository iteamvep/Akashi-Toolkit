package rikka.akashitoolkit.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import rikka.akashitoolkit.ui.ItemDisplayActivity;
import rikka.akashitoolkit.ui.ShipDisplayActivity;

/**
 * Created by Rikka on 2016/4/6.
 */
public class MyUrlSpan extends URLSpan {
    public MyUrlSpan(URLSpan src) {
        super(src.getURL());
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //ds.setColor(ds.linkColor);
        //ds.setColor(Color.parseColor("#8C9EFF"));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        String url = getURL();
        Context context = widget.getContext();
        if (url.startsWith("akshitoolkit://")) {
            Intent intent = null;
            int id = Integer.parseInt(url.split("/")[3]);
            if (url.contains("item/")) {
                intent = new Intent(widget.getContext(), ItemDisplayActivity.class);
                intent.putExtra(ItemDisplayActivity.EXTRA_ITEM_ID, id);
            } else if (url.contains("item/")) {
                intent = new Intent(widget.getContext(), ShipDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, id);
            }
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                super.onClick(widget);
            }
        } else {
            super.onClick(widget);
        }
    }
}
