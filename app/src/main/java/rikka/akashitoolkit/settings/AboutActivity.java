package rikka.akashitoolkit.settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.billing.DonateHelper;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.tools.SendReportActivity;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.utils.AlipayDonateUtils;
import rikka.akashitoolkit.utils.ClipBoardUtils;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.PackageUtils;
import rikka.materialpreference.Preference;
import rikka.materialpreference.PreferenceFragment;
import rikka.materialpreference.PreferenceScreen;

public class AboutActivity extends BaseActivity {

    DonateHelper mDonateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.about);

        if (savedInstanceState == null) {
            AboutFragment fragment = new AboutFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                    fragment).commit();
        }

        if (FlavorsUtils.isPlay()) {
            mDonateHelper = new DonateHelper(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FlavorsUtils.isPlay()) {
            if (!mDonateHelper.onActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (FlavorsUtils.isPlay()) {
            mDonateHelper.onDestroy();
        }
        super.onDestroy();
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

    public static class AboutFragment extends PreferenceFragment {
        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            setPreferencesFromResource(R.xml.about, null);

            try {
                PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);

                findPreference("version").setSummary(String.format(getActivity().getString(R.string.version_format), info.versionName, info.versionCode));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            findPreference("qq_group").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ClipBoardUtils.putTextIntoClipboard(getActivity(), "437033068");
                    Toast.makeText(getActivity(), String.format(getString(R.string.copied_to_clipboard_format), "437033068"), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SendReportActivity.sendEmail(getActivity(),
                            "Akashi Toolkit 反馈",
                            String.format("系统版本: %s (API %d)\n设备型号: %s\n设备制造商: %s\n应用版本: %d\nFlavors: %s",
                                    Build.VERSION.RELEASE,
                                    Build.VERSION.SDK_INT,
                                    Build.MODEL,
                                    Build.MANUFACTURER,
                                    StaticData.instance(getActivity()).versionCode,
                                    BuildConfig.FLAVOR));
                    return false;
                }
            });

            if (FlavorsUtils.isPlay()) {
                findPreference("donate").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if (((AboutActivity) getActivity()).mDonateHelper.isSuccess()) {
                            final DonateHelper.OnPurchaseSuccessListener listener = new DonateHelper.OnPurchaseSuccessListener() {
                                @Override
                                public void onSuccess(String sku) {
                                    if (isVisible()) {
                                        Toast.makeText(getActivity(), R.string.donate_gp_thanks, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            new AlertDialog.Builder(getActivity())
                                    .setItems(new CharSequence[]{"1 USD", "2 USD", "5 USD", "10 USD"}, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    ((AboutActivity) getActivity()).mDonateHelper.start(getActivity(), DonateHelper.SKU_DONATE_1, listener);
                                                    break;
                                                case 1:
                                                    ((AboutActivity) getActivity()).mDonateHelper.start(getActivity(), DonateHelper.SKU_DONATE_2, listener);
                                                    break;
                                                case 2:
                                                    ((AboutActivity) getActivity()).mDonateHelper.start(getActivity(), DonateHelper.SKU_DONATE_5, listener);
                                                    break;
                                                case 3:
                                                    ((AboutActivity) getActivity()).mDonateHelper.start(getActivity(), DonateHelper.SKU_DONATE_10, listener);
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.donate_gp_set_up_failed)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                        return false;
                    }
                });
            } else {
                ((PreferenceScreen) findPreference("screen")).removePreference(findPreference("donate"));
            }


            findPreference("donate_alipay").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlipayDonateUtils.startActivity(getActivity());
                    return false;
                }
            });

            if (FlavorsUtils.isPlay()
                    && !PackageUtils.isInstalled(getActivity(), AlipayDonateUtils.PACKAGENAME_ALIPAY)) {
                ((PreferenceScreen) findPreference("screen")).removePreference(findPreference("donate_alipay"));
            }

            findPreference("version").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    getActivity().getWindow().getDecorView().removeCallbacks(clearClickCount);
                    getActivity().getWindow().getDecorView().postDelayed(clearClickCount, 3000);

                    click ++;

                    if (click == 10) {
                        Settings.instance(getActivity()).putBoolean(Settings.DEVELOPER, true);
                        Toast.makeText(getActivity(), "You are now developer.", Toast.LENGTH_SHORT).show();
                        getActivity().recreate();
                    }

                    return false;
                }
            });
        }

        private int click = 0;
        private Runnable clearClickCount = new Runnable() {
            @Override
            public void run() {
                click = 0;
            }
        };
    }
}
