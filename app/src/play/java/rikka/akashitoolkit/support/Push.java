package rikka.akashitoolkit.support;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

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
                    .setTitle("您的设备没有安装 Google Play")
                    .setMessage("您正在使用 Google Play 版，该版本通过 Google Play 更新。且推送使用 Firebase Cloud Messaging，中国大陆可能无法正常使用。")
                    .setPositiveButton("下载国内版", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.kcwiki.moe/")));
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
