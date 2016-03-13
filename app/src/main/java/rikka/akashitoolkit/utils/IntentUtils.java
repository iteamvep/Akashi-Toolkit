package rikka.akashitoolkit.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Rikka on 2016/3/13.
 */
public class IntentUtils {
    public static int getSize(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size();
    }

    public static boolean isValid(Context context, Intent intent) {
        return isValid(context, intent, 0);
    }

    public static boolean isValid(Context context, Intent intent, int minSize) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > minSize;
    }
}
