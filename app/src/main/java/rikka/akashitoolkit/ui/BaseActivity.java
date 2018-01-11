package rikka.akashitoolkit.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import java.io.File;
import java.util.Locale;

import moe.shizuku.fontprovider.FontProviderClient;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.utils.LocaleUtils;

/**
 * Created by Rikka on 2016/3/6.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    public static final String EXTRA_EXTRA = "EXTRA_EXTRA";
    public static final String EXTRA_EXTRA2 = "EXTRA_EXTRA2";

    private static boolean sFontInitialized;

    private String mLocale;

    private static void initializeFont(Context context) {
        if (sFontInitialized) {
            return;
        }
        sFontInitialized = true;

        try {
            if (!new File("/system/fonts/NotoSansCJK-Medium.ttc").exists()
                    && !new File("/system/fonts/NotoSansCJKsc-Medium.ttf").exists()
                    && !new File("/system/fonts/NotoSansCJKsc-Medium.otf").exists()) {
                FontProviderClient client = FontProviderClient.create(context);
                if (client != null) {
                    client.replace("Noto Sans CJK",
                            "sans-serif", "sans-serif-medium");
                }
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeFont(this);

        setLocale();

        super.onCreate(savedInstanceState);
    }

    public void setLocale() {
        switch (Settings.instance(this).getString(Settings.APP_LANGUAGE, LocaleUtils.getDefaultLocale())) {
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
            doRecreate();
            return;
        }
        if (getDelegate().applyDayNight()) {
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

    public void doRecreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recreate();
        } else {
            recreate();
        }
    }
}
