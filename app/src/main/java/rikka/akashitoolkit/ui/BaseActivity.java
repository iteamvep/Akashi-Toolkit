package rikka.akashitoolkit.ui;

import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Statistics;

/**
 * Created by Rikka on 2016/3/6.
 */
public class BaseActivity extends BaseDayNightModeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onPause() {
        super.onPause();
        Statistics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        Statistics.onResume(this);
    }

    protected boolean checkPermission(String permission) {
        return (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
    }

    protected void getPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }
}
