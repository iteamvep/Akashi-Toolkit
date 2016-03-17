package rikka.akashitoolkit;


import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

import moe.xing.daynightmode.DayNightMode;
import rikka.akashitoolkit.support.CrashHandler;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/6.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DayNightMode.setDefaultNightMode(
                this,
                Settings.instance(this)
                        .getIntFromString(Settings.NIGHT_MODE, 0)
        );

        if (!BuildConfig.DEBUG) {
            CrashHandler.init(getApplicationContext());
            CrashHandler.register();
        }

        AVOSCloud.initialize(this, "q6Sj083vVxS6XrNgGD09w9kX-gzGzoHsz", "M9EUfoOVEnm9P5yoGXhAbwly");
        AVAnalytics.enableCrashReport(this, true);
    }
}