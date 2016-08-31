package rikka.akashitoolkit.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Rikka on 2016/8/31.
 */
public class HtmlUtils {

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
