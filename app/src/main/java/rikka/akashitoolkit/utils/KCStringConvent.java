package rikka.akashitoolkit.utils;

/**
 * Created by Rikka on 2016/4/4.
 */
public class KCStringConvent {
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
}
