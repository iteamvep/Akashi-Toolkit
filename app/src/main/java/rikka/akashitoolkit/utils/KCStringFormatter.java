package rikka.akashitoolkit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;

/**
 * Created by Rikka on 2016/4/4.
 */
public class KCStringFormatter {
    public static String getStars(int value) {
        String star = "";
        while (value > 0) {
            star += "★";
            value --;
        }
        return star;
    }

    public static String getRange(Context context, int value) {
        switch (value) {
            case 1:
                return context.getString(R.string.range_short);
            case 2:
                return context.getString(R.string.range_medium);
            case 3:
                return context.getString(R.string.range_long);
            case 4:
                return context.getString(R.string.range_very_long);
        }
        return "";
    }

    public static String getSpeed(Context context, int value) {
        switch (value) {
            case 10:
                return context.getString(R.string.speed_fast);
            case 5:
                return context.getString(R.string.speed_slow);
            case 0:
                return context.getString(R.string.speed_none);
        }
        return "";
    }

    public static String getFormation(int value) {
        switch (value) {
            case 1:
                return "单纵";
            case 2:
                return "复纵";
            case 3:
                return "轮型";
            case 4:
                return "梯形";
            case 5:
                return "单横";
            default:
                return "?";
        }
    }

    @SuppressLint("DefaultLocale")
    public static String getLinkEquip(int id, String name) {
        return String.format("<a href=akashitoolkit://equip/%d>%s</a>", id, name);
    }

    @SuppressLint("DefaultLocale")
    public static Spanned getLinkEquip(Equip equip) {
        return Html.fromHtml(String.format("<a href=akashitoolkit://equip/%d>%s</a>", equip.getId(), equip.getName().getZhCN()));
    }

    @SuppressLint("DefaultLocale")
    public static String getLinkShip(int id, String name) {
        return String.format("<a href=akashitoolkit://ship/%d>%s</a>", id, name);
    }
}
