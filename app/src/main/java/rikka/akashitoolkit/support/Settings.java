package rikka.akashitoolkit.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Set;

/**
 * Created by Rikka on 2016/3/8.
 */
public class Settings {
    public static final String XML_NAME = "settings";


    public static final String NIGHT_MODE = "night_mode";
    public static final String TWITTER_COUNT = "twitter_count";
    public static final String TWITTER_AVATAR_URL = "twitter_avatar_url";
    public static final String TWITTER_LANGUAGE = "twitter_display_language";
    public static final String LAST_DRAWER_ITEM_ID = "last_drawer_item_id";
    public static final String UPDATE_CHECK_CHANNEL = "update_check_channel";
    public static final String MESSAGE_READ_STATUS = "message_read_status";
    public static final String QUEST_FILTER = "quest_filter";
    public static final String SHIP_FILTER = "ship_filter";
    public static final String SHIP_FINAL_VERSION = "ship_show_final_version";
    public static final String SHIP_SPEED = "ship_show_speed";

    public static final String DATA_LANGUAGE = "data_language";
    public static final String DATA_TITLE_LANGUAGE = "data_title_language";


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

    public Settings putGSON(String key, Object obj) {
        mPrefs.edit()
                .putString(key, new Gson().toJson(obj))
                .apply();
        return this;
    }

    public <T> T getGSON(String key, Class<T> c) {
        return new Gson().fromJson(mPrefs.getString(key, ""), c);
    }
}
