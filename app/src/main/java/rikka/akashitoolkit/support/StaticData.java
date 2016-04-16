package rikka.akashitoolkit.support;

import android.content.Context;

import static rikka.akashitoolkit.support.ApiConstParam.Language.ZH_CN;

/**
 * Created by Rikka on 2016/4/16.
 */
public class StaticData {
    private static StaticData sInstance;

    public int language;

    private StaticData(Context context) {
        language = Settings
                .instance(context)
                .getIntFromString(Settings.DATA_LANGUAGE, ZH_CN);
    }

    public static synchronized StaticData instance(Context context) {
        if (sInstance == null) {
            sInstance = new StaticData(context);
        }

        return sInstance;
    }
}
