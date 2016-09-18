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

    private static Context sContext;

    private String zh_cn;
    private String ja;

    public MultiLanguageEntry() {
        zh_cn = "";
        ja = "";
    }

    public static void init(Context context) {
        sContext = context;
    }

    public String get(Context context, boolean isTitle) {
        if (isTitle && Settings
                        .instance(context)
                        .getBoolean(Settings.DATA_TITLE_LANGUAGE, true)) {
            return ja;
        }

        return get(context);
    }

    /**
     * 得到根据当前语言设置决定的文本
     *
     * @return 根据当前语言设置决定的文本
     */
    public String get() {
        if (sContext == null) {
            throw new RuntimeException("Call init(Context) in Application onCreate()");
        }

        return get(sContext);
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MultiLanguageEntry) {
            return (zh_cn == null || zh_cn.equals(((MultiLanguageEntry) obj).getZh_cn()))
                    && (ja == null || ja.equals(((MultiLanguageEntry) obj).getJa()));
        }
        return false;
    }
}
