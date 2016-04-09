package rikka.akashitoolkit;

import moe.xing.daynightmode.DayNightMode;
import rikka.akashitoolkit.support.CrashHandler;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;

/**
 * Created by Rikka on 2016/3/6.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DayNightMode.setDefaultNightMode(
                Settings.instance(this)
                        .getIntFromString(Settings.NIGHT_MODE, 0)
        );

        if (!BuildConfig.DEBUG) {
            CrashHandler.init(getApplicationContext());
            CrashHandler.register();
        }

        if (!BuildConfig.DEBUG) {
            Statistics.init(this);
        }
    }
}