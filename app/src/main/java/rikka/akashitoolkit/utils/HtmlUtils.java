package rikka.akashitoolkit.utils;

import android.os.Build;
import android.text.Html;

/**
 * Created by Rikka on 2016/8/31.
 */
public class HtmlUtils {

    public static String fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString().trim();
        } else {
            return Html.fromHtml(source).toString().trim();
        }
    }
}
