package rikka.akashitoolkit.support;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import rikka.akashitoolkit.service.RegistrationIntentService;

/**
 * Created by Rikka on 2016/5/3.
 */
public class Push {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static BroadcastReceiver mRegistrationBroadcastReceiver;
    private static boolean isReceiverRegistered;


    public static void init(final Activity activity) {
        // Registering BroadcastReceiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Settings.SENT_TOKEN_TO_SERVER, false);
                /*if (sentToken) {
                    Toast.makeText(activity, "gcm_send_message", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "token_error_message", Toast.LENGTH_LONG).show();
                }*/
            }
        };

        registerReceiver(activity);

        if (checkPlayServices(activity)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(activity, RegistrationIntentService.class);
            activity.startService(intent);
        }
    }

    private static void registerReceiver(Activity activity) {
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(activity).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Settings.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(final Activity activity) {
        if (!isPlayStoreInstalled(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle("您的设备没有安装 Google Play")
                    .setMessage("你将不能收到推送通知，获取新版本等。")
                    .setPositiveButton("下载国内版", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.minamion.com/Akashi/")));
                            activity.finish();
                        }
                    })
                    .setNegativeButton("继续使用", null)
                    .setCancelable(false)
                    .show();

            return false;
        }

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();

            } else {
                Log.i("GCM", "This device is not supported.");
                //finish();
            }
            return false;
        }
        return true;
    }

    protected static boolean isPlayStoreInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.android.vending", PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
