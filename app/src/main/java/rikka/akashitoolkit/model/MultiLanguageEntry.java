package rikka.akashitoolkit.model;

import android.content.Context;
import android.text.TextUtils;

import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;

import static rikka.akashitoolkit.support.ApiConstParam.Language.JA;
import static rikka.akashitoolkit.support.ApiConstParam.Language.ZH_CN;

/**
 * Created by Rikka on 2016/4/16.
 */
public class MultiLanguageEntry {
    private String zh_cn;
    private String ja;

    public MultiLanguageEntry() {
        zh_cn = "";
        ja = "";
    }

    public String get(Context context, boolean isTitle) {
        if (isTitle && Settings
                        .instance(context)
                        .getBoolean(Settings.DATA_TITLE_LANGUAGE, true)) {
            return ja;
        }

        return get(context);
    }

    public String get(Context context) {
        String s = null;
        switch (StaticData.instance(context).dataLanguage) {
            case ZH_CN:
                s = zh_cn;
                break;
            case JA:
                s = ja;
                break;
            default:
                s = zh_cn;
        }
        if (TextUtils.isEmpty(s)) {
            return ja;
        }

        return s;
    }

    public String getZh_cn() {
        return zh_cn;
    }

    public void setZh_cn(String zh_cn) {
        this.zh_cn = zh_cn;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }
}
