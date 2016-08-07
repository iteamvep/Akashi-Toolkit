package rikka.akashitoolkit.settings;

import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import moe.xing.daynightmode.DayNightMode;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ReadStatusResetAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.utils.Utils;
import rikka.materialpreference.DropDownPreference;
import rikka.materialpreference.Preference;
import rikka.materialpreference.PreferenceCategory;
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
        getSupportActionBar().setTitle(R.string.setting);

        if (savedInstanceState == null) {
            SettingFragment fragment = new SettingFragment();

            getFragmentManager().beginTransaction().replace(R.id.fragment,
                    fragment).commit();
        }
    }

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

        private AsyncTask<Void, Void, Void> mTask;

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

                    BusProvider.instance()
                            .post(new ReadStatusResetAction());

                    return false;
                }
            });

            if (BuildConfig.isGooglePlay) {
                ((PreferenceCategory) findPreference("update")).removePreference(findPreference(Settings.UPDATE_CHECK_CHANNEL));
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ((PreferenceCategory) findPreference("general")).removePreference(findPreference(Settings.NAV_BAR_COLOR));
                ((PreferenceCategory) findPreference("general")).removePreference(findPreference(Settings.OPEN_IN_NEW_DOCUMENT));
            }

            if (!StaticData.instance(getActivity()).isTablet) {
                ((PreferenceCategory) findPreference("twitter")).removePreference(findPreference(Settings.TWITTER_GRID_LAYOUT));
            }

            findPreference("clear_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
                        return false;
                    }

                    mTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Glide.get(getActivity()).clearDiskCache();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if (isVisible()) {
                                Toast.makeText(getActivity(), getString(R.string.cleared), Toast.LENGTH_SHORT).show();
                            }

                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                    return false;
                }
            });

            final DropDownPreference dropDownPreference = (DropDownPreference) findPreference("data_language");
            dropDownPreference.addItem(Locale.SIMPLIFIED_CHINESE.getDisplayName(), 0);
            dropDownPreference.addItem(Locale.JAPANESE.getDisplayName(), 1);
            dropDownPreference.setSelectedValue(Settings.instance(getActivity()).getInt(Settings.DATA_LANGUAGE, Utils.getDefaultDataLanguage()));
            dropDownPreference.setCallback(new DropDownPreference.Callback() {
                @Override
                public boolean onItemSelected(int i, Object o) {
                    Settings.instance(getActivity()).putInt(Settings.DATA_LANGUAGE, (Integer) o);
                    StaticData.instance(getActivity()).dataLanguage = (Integer) o;

                    BusProvider
                            .instance()
                            .post(new DataChangedAction("any"));
                    return true;
                }
            });

            final DropDownPreference dropDownPreference2 = (DropDownPreference) findPreference("ui_language");
            dropDownPreference2.addItem(Locale.SIMPLIFIED_CHINESE.getDisplayName(), "sc");
            dropDownPreference2.addItem(Locale.TRADITIONAL_CHINESE.getDisplayName(), "tc");
            dropDownPreference2.addItem(Locale.ENGLISH.getDisplayName(), "en");
            dropDownPreference2.addItem(Locale.JAPANESE.getDisplayName(), "ja");
            dropDownPreference2.setSelectedValue(Settings.instance(getActivity()).getString(Settings.APP_LANGUAGE, Utils.getDefaultSettingFromLocale()));
            dropDownPreference2.setCallback(new DropDownPreference.Callback() {
                @Override
                public boolean onItemSelected(int i, Object o) {
                    dropDownPreference2.setEnabled(false);

                    ((BaseActivity) getActivity()).setLocale();

                    // so bad
                    Settings.instance(getActivity()).putString(Settings.APP_LANGUAGE, (String) o);
                    getActivity().findViewById(R.id.toolbar).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((BaseActivity) getActivity()).fakeRecreate();
                        }
                    }, 300);
                    //((BaseActivity) getActivity()).fakeRecreate();
                    return false;
                }
            });
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
            switch (key) {
                case Settings.NIGHT_MODE:
                    int mode = Integer.parseInt(
                            (String) mDropDownPreference.getSelectedValue());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mode == DayNightMode.MODE_NIGHT_FOLLOW_SYSTEM) {
                            new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog_Alert)
                                    .setMessage(R.string.night_mode_require_restart)
                                    .setNegativeButton(R.string.continue_use, null)
                                    .setPositiveButton(R.string.restart_app, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(0);
                                        }
                                    })
                                    .show();
                            break;
                        } else if (mode == DayNightMode.MODE_NIGHT_AUTO &&
                                ((UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE))
                                        .getNightMode() == UiModeManager.MODE_NIGHT_AUTO) {
                            new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog_Alert)
                                    .setMessage(R.string.night_mode_system_auto)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                            break;
                        }
                    }

                    ((BaseDayNightModeActivity) getActivity()).setNightMode(mode);
                    break;
                case Settings.DATA_LANGUAGE:
                case Settings.DATA_TITLE_LANGUAGE:
                    BusProvider
                            .instance()
                            .post(new DataChangedAction("any"));
                    break;
                case Settings.NAV_BAR_COLOR:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (Settings.instance(getActivity()).getBoolean(Settings.NAV_BAR_COLOR, false)) {
                            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        } else {
                            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getActivity(), android.R.color.black));
                        }
                    }
                    break;
            }

            BusProvider
                    .instance()
                    .post(new PreferenceChangedAction(key));
        }
    }
}
