package rikka.akashitoolkit.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.ClipBoardUtils;
import rikka.akashitoolkit.utils.UpdateCheck;
import rikka.materialpreference.PreferenceFragment;

public class AboutActivity extends AppCompatActivity {

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

        if (savedInstanceState == null) {
            About fragment = new About();

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

    public static class About extends PreferenceFragment {
        @Override
        public void onStop() {
            UpdateCheck.instance().recycle();
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
                    Toast.makeText(getActivity(), String.format("%s copied to clipboard.", "437033068"), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            findPreference("check").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    UpdateCheck.instance().check(getActivity(), true);
                    return false;
                }
            });
        }
    }
}
