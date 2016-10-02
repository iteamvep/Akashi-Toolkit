package rikka.akashitoolkit.utils;

import android.animation.ArgbEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;

import static rikka.akashitoolkit.support.ApiConstParam.Language.EN;
import static rikka.akashitoolkit.support.ApiConstParam.Language.JA;
import static rikka.akashitoolkit.support.ApiConstParam.Language.ZH_CN;

/**
 * Created by Rikka on 2016/3/8.
 */
public class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean isNightMode(Resources resources) {
        return ((resources.getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES) > 0);
    }

    public static void colorAnimation(int colorFrom, int colorTo, int duration, ValueAnimator.AnimatorUpdateListener listener) {
        colorAnimation(colorFrom, colorTo, duration, null, listener);
    }

    public static void colorAnimation(int colorFrom, int colorTo, int duration, TimeInterpolator interpolator, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration);
        colorAnimation.setInterpolator(interpolator);
        colorAnimation.addUpdateListener(listener);
        colorAnimation.start();
    }

    public static String md5( String input ) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(String.format("%02x", anArray));
            }
            return sb.toString();
        } catch ( NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getKCWikiFileUrl(String fileName) {
        String md5 = Utils.md5(fileName);
        if (md5 == null) {
            return null;
        }

        String a = md5.substring(0, 1);
        String b = md5.substring(0, 2);

        return String.format("https://kc.6candy.com/commons/%s/%s/%s", a, b, fileName);
    }

    public static GlideUrl getGlideUrl(String url) {
        /*return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "AkashiToolkit")
                .build());*/
        if (url.contains("https://kc.6candy.com/")) {
            return new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6,en-US;q=0.4,en;q=0.2")
                    .addHeader("Host", "kc.6candy.com")
                    .addHeader("Referer", "https://zh.kcwiki.moe/wiki/%E8%88%B0%E5%A8%98%E7%99%BE%E7%A7%91")
                    .build());
        } else {
            return new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6,en-US;q=0.4,en;q=0.2")
                    .build());
        }

    }

    private static final String[] CHINESE_NUMBER = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};

    public static String getChineseNumberString(int number) {
        StringBuilder sb = new StringBuilder();

        int a = number / 10;
        if (a > 0) {
            if (a > 1) {
                sb.append(CHINESE_NUMBER[a]);
            }
            sb.append(CHINESE_NUMBER[10]);
        }

        if (number % 10 != 0) {
            sb.append(CHINESE_NUMBER[number % 10]);
        }

        return sb.toString();
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isPackageEnabled(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static String getDefaultSettingFromLocale() {
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                switch (Locale.getDefault().getCountry()) {
                    case "CN":
                        return Locale.SIMPLIFIED_CHINESE.toString();
                    default:
                        return Locale.TRADITIONAL_CHINESE.toString();
                }
            case "ja":
                return Locale.JAPAN.toString();
        }

        return Locale.ENGLISH.toString();
    }

    public static int getDefaultDataLanguage() {
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                return ZH_CN;
            case "ja":
                return JA;
            case "en":
                return EN;
        }

        return ZH_CN;
    }

    public static boolean isJapanese(Context context) {
        return StaticData.instance(context).dataLanguage == JA;
    }
}
