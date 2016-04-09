package rikka.akashitoolkit.support;

import android.app.Activity;
import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Rikka on 2016/4/5.
 */
public class Statistics {
    public static void init(Application application) {
        AVOSCloud.initialize(application, "q6Sj083vVxS6XrNgGD09w9kX-gzGzoHsz", "M9EUfoOVEnm9P5yoGXhAbwly");
        //AVAnalytics.enableCrashReport(application, true);
    }

    public static void onResume(Activity activity) {
        AVAnalytics.onResume(activity);
    }

    public static void onPause(Activity activity) {
        AVAnalytics.onPause(activity);
    }

    public static void onFragmentStart(String name) {
        AVAnalytics.onFragmentStart(name);
    }

    public static void onFragmentEnd(String name) {
        AVAnalytics.onFragmentEnd(name);
    }
}
