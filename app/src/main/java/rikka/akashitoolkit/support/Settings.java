package rikka.akashitoolkit.support;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rikka on 2016/3/8.
 */
public class Settings {
    public static final String XML_NAME = "settings";


    public static final String NIGHT_MODE = "night_mode";
    public static final String TWITTER_COUNT = "twitter_count";
    public static final String TWITTER_LANGUAGE = "twitter_display_language";
    public static final String LAST_DRAWER_ITEM_ID = "last_drawer_item_id";
    public static final String UPDATE_CHECK_PERIOD = "update_check";

    private static Settings sInstance;
    private SharedPreferences mPrefs;

    private Settings(Context context) {
        mPrefs = context.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized Settings instance(Context context) {
        if (sInstance == null) {
            sInstance = new Settings(context);
        }

        return sInstance;
    }

    public Settings putBoolean(String key, boolean value) {
        mPrefs.edit()
                .putBoolean(key, value)
                .apply();

        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPrefs.getBoolean(key, def);
    }

    public Settings putInt(String key, int value) {
        mPrefs.edit()
                .putInt(key, value)
                .apply();

        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public Settings putString(String key, String value) {
        mPrefs.edit()
                .putString(key, value)
                .apply();

        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public Settings putIntToString(String key, int value) {
        mPrefs.edit()
                .putString(key, Integer.toString(value))
                .apply();

        return this;
    }

    public int getIntFromString(String key, int defValue) {
        return Integer.parseInt(mPrefs.getString(key, Integer.toString(defValue)));
    }
}
