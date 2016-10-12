package rikka.akashitoolkit.support;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;
/*import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;*/

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/4/5.
 */
public class Statistics {
    //private static Tracker sTracker;

    public static synchronized void init(Application application) {
        AVOSCloud.initialize(application, BuildConfig.LEANCLOUD_APP_ID, BuildConfig.LEANCLOUD_APP_KEY);
        AVAnalytics.enableCrashReport(application, true);

        //sTracker = getDefaultTracker(application);
    }

    /*/**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    /*private static synchronized Tracker getDefaultTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG

        return analytics.newTracker(R.xml.global_tracker);
    }*/

    public static void onResume(Activity activity) {
        if (activity.getClass().getSimpleName().equals("MainActivity")) {
            return;
        }

        //sTracker.setScreenName(activity.getClass().getSimpleName());
        //sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void onPause(Activity activity) {
    }

    public static void onFragmentStart(String name) {
        //sTracker.setScreenName(name);
        //sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void onFragmentEnd(String name) {
    }

    public static void sendEvent(String category, String action, String label, long value) {
        /*sTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());*/
    }
}
