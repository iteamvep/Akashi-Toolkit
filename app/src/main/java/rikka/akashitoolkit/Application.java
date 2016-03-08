package rikka.akashitoolkit;

import android.support.v7.preference.PreferenceManager;

import moe.xing.daynightmode.DayNightMode;

/**
 * Created by Rikka on 2016/3/6.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DayNightMode.setSystemNightMode(this,
                Integer.parseInt(
                        PreferenceManager.getDefaultSharedPreferences(this).getString("night_mode", "0")));
    }
}
