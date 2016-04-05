package rikka.akashitoolkit.ui;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ReadStatusResetAction;
import rikka.akashitoolkit.support.Settings;
import rikka.materialpreference.DropDownPreference;
import rikka.materialpreference.PreferenceFragment;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            SettingFragment fragment = new SettingFragment();

            getFragmentManager().beginTransaction().replace(R.id.fragment,
                    fragment).commit();
        }
    }

    /*@Override
    public boolean shouldOverrideBackTransition() {
        return false;
    }*/

    /*@Override
    public void onBackPressed() {
        if (shouldOverrideBackTransition()) {
            overridePendingTransition(0, moe.xing.daynightmode.R.anim.activity_close_exit);
        }
        ActivityCompat.finishAfterTransition(this);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        private DropDownPreference mDropDownPreference;

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            getPreferenceManager().setSharedPreferencesName(Settings.XML_NAME);
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);

            setPreferencesFromResource(R.xml.settings, null);
            mDropDownPreference = (DropDownPreference) findPreference(Settings.NIGHT_MODE);

            findPreference("reset_read").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Settings.instance(getActivity())
                            .putString(Settings.MESSAGE_READ_STATUS, "");

                    Toast.makeText(getActivity(), R.string.read_status_reset, Toast.LENGTH_SHORT).show();

                    BusProvider.instance()
                            .post(new ReadStatusResetAction());

                    return false;
                }
            });

            if (BuildConfig.isGooglePlay) {
                ((PreferenceCategory) findPreference("update")).removePreference(findPreference("update_check_channel"));
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Settings.NIGHT_MODE)) {
                int mode = Integer.parseInt(
                        (String) mDropDownPreference.getSelectedValue());

                ((BaseDayNightModeActivity) getActivity()).setNightMode(mode);
            }

            BusProvider.instance()
                    .post(new PreferenceChangedAction(key));
        }
    }



}
