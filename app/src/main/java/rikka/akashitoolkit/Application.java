package rikka.akashitoolkit;


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

        //CrashHandler.init(getApplicationContext());
        //CrashHandler.register();
    }
}