package rikka.akashitoolkit.support;

import android.app.Activity;
import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.BuildConfig;

/**
 * Created by Rikka on 2016/4/5.
 */
public class Statistics {
    public static void init(Application application) {
        if (BuildConfig.DEBUG) {
            return;
        }

        AVOSCloud.initialize(application, "q6Sj083vVxS6XrNgGD09w9kX-gzGzoHsz", "M9EUfoOVEnm9P5yoGXhAbwly");
        //AVAnalytics.enableCrashReport(application, true);
    }

    public static void onResume(Activity activity) {
        if (BuildConfig.DEBUG) {
            return;
        }

        AVAnalytics.onResume(activity);
    }

    public static void onPause(Activity activity) {
        if (BuildConfig.DEBUG) {
            return;
        }

        AVAnalytics.onPause(activity);
    }

    public static void onFragmentStart(String name) {
        if (BuildConfig.DEBUG) {
            return;
        }

        AVAnalytics.onFragmentStart(name);
    }

    public static void onFragmentEnd(String name) {
        if (BuildConfig.DEBUG) {
            return;
        }

        AVAnalytics.onFragmentEnd(name);
    }
}
