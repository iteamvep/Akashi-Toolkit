package rikka.akashitoolkit;

import moe.xing.daynightmode.DayNightMode;
import rikka.akashitoolkit.cache.DiskCacheProvider;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.Subtitle;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.utils.NetworkUtils;

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

        DiskCacheProvider.init(this);

        // so bad to avoid ConcurrentModificationException
        ShipList.get(this);
        EquipList.get(this);

        /*if (!BuildConfig.DEBUG) {
            CrashHandler.init(getApplicationContext());
            CrashHandler.register();
        }*/

        Statistics.init(this);

        NetworkUtils.init(this);
        Subtitle.init(this);
    }
}