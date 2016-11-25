package rikka.akashitoolkit.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by Rikka on 2016/11/25.
 */

public class PackageUtils {

    public static boolean isInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isEnabled(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Intent getLaunchIntent(Context context, String packageName) {
        if (!isEnabled(context, packageName)) {
            return null;
        }

        try {
            return context.getPackageManager().getLaunchIntentForPackage(packageName);
        } catch (Exception ignored) {
            return null;
        }
    }
}
