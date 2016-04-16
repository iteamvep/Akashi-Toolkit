package rikka.akashitoolkit.model;

import android.content.Context;

import rikka.akashitoolkit.support.Settings;

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

    public String get(Context context) {
        switch (Settings
                .instance(context)
                .getIntFromString(Settings.DATA_LANGUAGE, ZH_CN)) {
            case ZH_CN:
                return zh_cn;
            case JA:
                return ja;
            default:
                return zh_cn;
        }
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
