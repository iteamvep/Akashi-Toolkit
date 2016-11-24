package rikka.akashitoolkit.utils;

import android.content.Context;

import java.util.Locale;

import rikka.akashitoolkit.support.ApiConstParam;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;

/**
 * Created by Rikka on 2016/11/24.
 */

public class LocaleUtils {

    public static String getDefaultLocale() {
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                switch (Locale.getDefault().getCountry()) {
                    case "CN":
                        return Locale.SIMPLIFIED_CHINESE.toString();
                    default:
                        return Locale.TRADITIONAL_CHINESE.toString();
                }
            case "ja":
                return Locale.JAPANESE.toString();
            default:
                return Locale.ENGLISH.toString();
        }
    }

    public static int getDefaultDataLanguage() {
        switch (Locale.getDefault().getLanguage()) {
            case "zh":
                switch (Locale.getDefault().getCountry()) {
                    case "CN":
                        return ApiConstParam.Language.ZH_CN;
                    default:
                        return ApiConstParam.Language.ZH_TW;
                }
            case "ja":
                return ApiConstParam.Language.JA;
            default:
                return ApiConstParam.Language.EN;
        }
    }

    public static int getAppLanguage(Context context) {
        String language = Settings.instance(context).getString(Settings.APP_LANGUAGE, getDefaultLocale());
        if (language.equals(Locale.SIMPLIFIED_CHINESE.toString())) {
            return ApiConstParam.Language.ZH_CN;
        }
        if (language.equals(Locale.TRADITIONAL_CHINESE.toString())) {
            return ApiConstParam.Language.ZH_TW;
        }
        if (language.equals(Locale.JAPANESE.toString())) {
            return ApiConstParam.Language.JA;
        }
        if (language.equals(Locale.ENGLISH.toString())) {
            return ApiConstParam.Language.EN;
        }
        return ApiConstParam.Language.ZH_CN;
    }

    public static boolean isDataLanguageJapanese(Context context) {
        return StaticData.instance(context).dataLanguage == ApiConstParam.Language.JA;
    }
}
