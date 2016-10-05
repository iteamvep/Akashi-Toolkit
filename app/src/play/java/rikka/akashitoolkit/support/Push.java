package rikka.akashitoolkit.support;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/5/3.
 */
public class Push {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static void init(final Activity activity) {
        if (checkPlayServices(activity)) {
            FirebaseMessaging.getInstance().subscribeToTopic("all");
            // TODO option
            FirebaseMessaging.getInstance().subscribeToTopic("news");
        }
    }

    private static boolean checkPlayServices(final Activity activity) {
        if (!isPlayStoreInstalled(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.play_service_check_google_play_not_installed)
                    .setMessage(R.string.play_service_check_reason)
                    .setPositiveButton(R.string.play_service_check_download_non_play_version, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.kcwiki.moe/")));
                            activity.finish();
                        }
                    })
                    .setNegativeButton(R.string.play_service_check_ignore, null)
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
                Log.i("FCM", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    private static boolean isPlayStoreInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.android.vending", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
