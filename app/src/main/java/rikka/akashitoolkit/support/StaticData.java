package rikka.akashitoolkit.support;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import static rikka.akashitoolkit.support.ApiConstParam.Language.ZH_CN;

/**
 * Created by Rikka on 2016/4/16.
 */
public class StaticData {
    private static StaticData sInstance;

    public int language;
    public int versionCode;
    public String versionName;

    private StaticData(Context context) {
        language = Settings
                .instance(context)
                .getIntFromString(Settings.DATA_LANGUAGE, ZH_CN);

        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = -1;
        }
    }

    public static synchronized StaticData instance(Context context) {
        if (sInstance == null) {
            sInstance = new StaticData(context);
        }

        return sInstance;
    }
}
