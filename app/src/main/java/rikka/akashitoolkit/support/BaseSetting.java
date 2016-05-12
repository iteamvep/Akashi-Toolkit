package rikka.akashitoolkit.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Rikka on 2016/5/12.
 */
public class BaseSetting {
    private SharedPreferences mPrefs;

    public BaseSetting(Context context, String name) {
        mPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public BaseSetting putBoolean(String key, boolean value) {
        mPrefs.edit()
                .putBoolean(key, value)
                .apply();

        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPrefs.getBoolean(key, def);
    }

    public BaseSetting putInt(String key, int value) {
        mPrefs.edit()
                .putInt(key, value)
                .apply();

        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public BaseSetting putString(String key, String value) {
        mPrefs.edit()
                .putString(key, value)
                .apply();

        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public BaseSetting putIntToString(String key, int value) {
        mPrefs.edit()
                .putString(key, Integer.toString(value))
                .apply();

        return this;
    }

    public int getIntFromString(String key, int defValue) {
        return Integer.parseInt(mPrefs.getString(key, Integer.toString(defValue)));
    }

    public BaseSetting putGSON(String key, Object obj) {
        mPrefs.edit()
                .putString(key, new Gson().toJson(obj))
                .apply();
        return this;
    }

    public <T> T getGSON(String key, Class<T> c) {
        return new Gson().fromJson(mPrefs.getString(key, ""), c);
    }
}
