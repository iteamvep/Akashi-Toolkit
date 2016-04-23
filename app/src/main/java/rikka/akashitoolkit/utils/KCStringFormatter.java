package rikka.akashitoolkit.utils;

import android.text.Html;
import android.text.Spanned;

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

    public static String getRange(int value) {
        switch (value) {
            case 1: return "短";
            case 2: return "中";
            case 3: return "长";
            case 4: return "超长";
        }
        return "";
    }

    public static String getSpeed(int value) {
        switch (value) {
            case 10: return "高速";
            case 5: return "低速";
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
                return "不存在的阵形?";
        }
    }

    public static String getLinkEquip(int id, String name) {
        return String.format("<a href=akashitoolkit://equip/%d>%s</a>", id, name);
    }

    public static Spanned getLinkEquip(Equip equip) {
        return Html.fromHtml(String.format("<a href=akashitoolkit://equip/%d>%s</a>", equip.getId(), equip.getName().getZh_cn()));
    }

    public static String getLinkShip(int id, String name) {
        return String.format("<a href=akashitoolkit://ship/%d>%s</a>", id, name);
    }
}
