package rikka.akashitoolkit.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/3/6.
 */
public class BaseActivity extends BaseDayNightModeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.background)));
    }
}
