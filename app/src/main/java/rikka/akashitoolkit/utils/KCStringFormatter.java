package rikka.akashitoolkit.utils;

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

    public static String getLinkItem(int id, String name) {
        return String.format("<a href=akashitoolkit://item/%d>%s</a>", id, name);
    }

    public static String getLinkShip(int id, String name) {
        return String.format("<a href=akashitoolkit://ship/%d>%s</a>", id, name);
    }
}
