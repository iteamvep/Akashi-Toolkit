package rikka.akashitoolkit.model;

import android.text.TextUtils;

import static rikka.akashitoolkit.support.ApiConstParam.Language.EN;
import static rikka.akashitoolkit.support.ApiConstParam.Language.JA;
import static rikka.akashitoolkit.support.ApiConstParam.Language.ZH_CN;
import static rikka.akashitoolkit.support.ApiConstParam.Language.ZH_TW;

/**
 * Created by Rikka on 2016/4/16.
 * 根据数据语言设置
 */
public class MultiLanguageEntry {

    private String zh_cn;
    private String ja;
    private String en;
    private String zh_tw;

    public static int language = ZH_CN;
    public static boolean titleUseJa = true;

    public MultiLanguageEntry() {
        zh_cn = "";
        ja = "";
        en = "";
        zh_tw = "";
    }

    public String get(boolean isTitle) {
        if (isTitle && titleUseJa) {
            return ja;
        }

        return get();
    }

    public String get() {
        return get(language);
    }

    /**
     * 得到根据当前语言设置决定的文本
     *
     * @return 根据当前语言设置决定的文本
     */

    public String get(int language) {
        String s;
        switch (language) {
            case ZH_CN:
                s = zh_cn;
                break;
            case JA:
                s = ja;
                break;
            case EN:
                s = en;
                break;
            case ZH_TW:
                s = zh_tw;
                break;
            default:
                s = zh_cn;
        }

        if (TextUtils.isEmpty(s)) {
            if (!TextUtils.isEmpty(ja)) {
                return ja;
            } else if (!TextUtils.isEmpty(zh_cn)) {
                return zh_cn;
            } else if (!TextUtils.isEmpty(zh_tw)) {
                return zh_tw;
            } else if (!TextUtils.isEmpty(en)) {
                return en;
            }
        }

        return s;
    }

    public String getZhCN() {
        return zh_cn;
    }

    public String getJa() {
        return ja;
    }

    public String getEn() {
        return en;
    }

    public String getZhTW() {
        return zh_tw;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MultiLanguageEntry) {
            MultiLanguageEntry entry = (MultiLanguageEntry) obj;
            return ((zh_cn == null || zh_cn.equals(entry.getZhCN()))
                    && (ja == null || ja.equals(entry.getJa()))
                    && (en == null || en.equals(entry.getEn()))
                    && (zh_tw == null || zh_tw.equals(entry.getEn()))
            );
        }
        return false;
    }
}
