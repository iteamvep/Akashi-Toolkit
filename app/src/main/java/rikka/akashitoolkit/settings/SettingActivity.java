package rikka.akashitoolkit.settings;

import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import moe.xing.daynightmode.BaseDayNightModeActivity;
import moe.xing.daynightmode.DayNightMode;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.MultiLanguageEntry;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ReadStatusResetAction;
import rikka.akashitoolkit.support.ApiConstParam;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.Utils;
import rikka.materialpreference.DropDownPreference;
import rikka.materialpreference.ListPreference;
import rikka.materialpreference.Preference;
import rikka.materialpreference.PreferenceCategory;
import rikka.materialpreference.PreferenceFragment;
import rikka.materialpreference.PreferenceViewHolder;

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

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
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

        private AsyncTask<Void, Void, Void> mTask;

        @Nullable
        @Override
        public DividerDecoration onCreateItemDecoration() {
            return new CategoryDivideDividerDecoration();
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            getPreferenceManager().setSharedPreferencesName(Settings.XML_NAME);
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);

            setPreferencesFromResource(R.xml.settings, null);

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

            if (FlavorsUtils.isPlay()) {
                ((PreferenceCategory) findPreference("update")).removePreference(findPreference(Settings.UPDATE_CHECK_CHANNEL));
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ((PreferenceCategory) findPreference("general")).removePreference(findPreference(Settings.NAV_BAR_COLOR));
                ((PreferenceCategory) findPreference("general")).removePreference(findPreference(Settings.OPEN_IN_NEW_DOCUMENT));
            }

            if (!StaticData.instance(getActivity()).isTablet) {
                ((PreferenceCategory) findPreference("general")).removePreference(findPreference(Settings.TWITTER_GRID_LAYOUT));
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

            final ListPreference dropDownPreference = (ListPreference) findPreference(Settings.DATA_LANGUAGE);
            dropDownPreference.setEntries(new CharSequence[]{Locale.SIMPLIFIED_CHINESE.getDisplayName(), Locale.JAPANESE.getDisplayName()});
            dropDownPreference.setEntryValues(new CharSequence[]{"0", "1"});
            if (dropDownPreference.getValue() == null) {
                dropDownPreference.setValueIndex(Utils.getDefaultDataLanguage());
            }

            final ListPreference dropDownPreference2 = (ListPreference) findPreference(Settings.APP_LANGUAGE);
            dropDownPreference2.setEntries(new CharSequence[]{
                    Locale.SIMPLIFIED_CHINESE.getDisplayName(),
                    Locale.TRADITIONAL_CHINESE.getDisplayName(),
                    Locale.ENGLISH.getDisplayName(),
                    Locale.JAPANESE.getDisplayName()
            });
            dropDownPreference2.setEntryValues(new CharSequence[]{
                    "sc",
                    "tc",
                    "en",
                    "ja"
            });
            if (dropDownPreference2.getValue() == null) {
                dropDownPreference2.setValue(Settings.instance(getActivity()).getString(Settings.APP_LANGUAGE, Utils.getDefaultSettingFromLocale()));
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
            switch (key) {
                case Settings.NIGHT_MODE:
                    int mode = Integer.parseInt(sharedPreferences.getString(key, "0"));

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
                    int language = Integer.parseInt(
                            sharedPreferences.getString(key, Integer.toString(ApiConstParam.Language.ZH_CN)));

                    StaticData.instance(getActivity()).dataLanguage = language;
                    MultiLanguageEntry.language = language;
                case Settings.DATA_TITLE_LANGUAGE:
                    BusProvider
                            .instance()
                            .post(new DataChangedAction("any"));
                    MultiLanguageEntry.titleUseJa = sharedPreferences.getBoolean(Settings.DATA_TITLE_LANGUAGE, true);
                    break;
                case Settings.NAV_BAR_COLOR:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (sharedPreferences.getBoolean(key, false)) {
                            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        } else {
                            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getActivity(), android.R.color.black));
                        }
                    }
                    break;
                case Settings.APP_LANGUAGE:
                    // android.view.WindowLeaked
                    getListView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((BaseActivity) getActivity()).fakeRecreate();
                        }
                    }, 300);
                    break;
            }

            BusProvider
                    .instance()
                    .post(new PreferenceChangedAction(key));
        }
    }
}
