package rikka.akashitoolkit.ui;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import java.util.Locale;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/6.
 */
public abstract class BaseActivity extends BaseDayNightModeActivity {
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    public static final String EXTRA_EXTRA = "EXTRA_EXTRA";
    public static final String EXTRA_EXTRA2 = "EXTRA_EXTRA2";

    private String mLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLocale();

        super.onCreate(savedInstanceState);
    }

    public void setLocale() {
        switch (Settings.instance(this).getString(Settings.APP_LANGUAGE, Utils.getDefaultSettingFromLocale())) {
            case "zh_CN":
                Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
                break;
            case "zh_TW":
                Locale.setDefault(Locale.TRADITIONAL_CHINESE);
                break;
            case "ja":
                Locale.setDefault(Locale.JAPANESE);
                break;
            default:
                Locale.setDefault(Locale.ENGLISH);
                break;
        }

        mLocale = Locale.getDefault().getLanguage();

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = Locale.getDefault();

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    protected void onPause() {
        super.onPause();
        Statistics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        if (!Locale.getDefault().getLanguage().equals(mLocale)) {
            fakeRecreate();
            return;
        }

        Statistics.onResume(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Settings.instance(this).getBoolean(Settings.NAV_BAR_COLOR, false)) {
                setNavigationBarColor();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setNavigationBarColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);

        getWindow().setNavigationBarColor(typedValue.data);
    }

    public boolean checkPermission(String permission) {
        return (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public void getPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    public void fakeRecreate() {
        startActivity(this.getNightModeChangedRestartActivityIntent());
        finish();
        overridePendingTransition(0, 0);
    }
}
