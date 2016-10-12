package rikka.akashitoolkit.utils;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/9/26.
 */

public class FlavorsUtils {

    /**
     * 返回是否是 Google Play 版
     *
     * @return 是否是 Google Play 版
     */
    public static boolean isPlay() {
        return BuildConfig.BUILD_TYPE.startsWith("googlePlay");
    }

    /**
     * Google: 不可以，这很色情
     *
     * @return 返回是否需要“安全”检查
     */
    public static boolean shouldSafeCheck() {
        return isPlay() && true;
    }
}
