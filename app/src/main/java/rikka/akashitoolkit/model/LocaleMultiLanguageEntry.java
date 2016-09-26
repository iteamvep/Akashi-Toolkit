package rikka.akashitoolkit.model;

import java.util.Locale;

import rikka.akashitoolkit.support.ApiConstParam;

/**
 * Created by Rikka on 2016/9/26.
 */

public class LocaleMultiLanguageEntry extends MultiLanguageEntry {

    @Override
    public String get() {
        if (Locale.getDefault().equals(Locale.SIMPLIFIED_CHINESE)) {
            return get(ApiConstParam.Language.ZH_CN);
        } else if (Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE)) {
            return get(ApiConstParam.Language.ZH_TW);
        } else if (Locale.getDefault().equals(Locale.JAPANESE)) {
            return get(ApiConstParam.Language.JA);
        } else if (Locale.getDefault().equals(Locale.ENGLISH)) {
            return get(ApiConstParam.Language.EN);
        }
        return super.get();
    }
}
