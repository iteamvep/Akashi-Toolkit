package rikka.akashitoolkit.support;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.ui.EquipDisplayActivity;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.ui.MapActivity;
import rikka.akashitoolkit.ui.ShipDisplayActivity;

/**
 * Created by Rikka on 2016/5/3.
 */
public class PushHandler {
    public static void sendNotification(Context context, int id, String title, String message, String activity) {
        sendNotification(context, id, title, message, activity, null);
    }

    public static void sendNotification(Context context, int id, String title, String message, String activity, String extra) {
        sendNotification(context, id, title, message, activity, null, null);
    }

    public static void sendNotification(Context context, int id, String title, String message, String activity, String extra, String extra2) {
        if (title == null) {
            title = context.getString(R.string.app_name);
        }

        Intent intent = getIntent(context, activity);
        intent.putExtra(BaseActivity.EXTRA_FROM_NOTIFICATION, true);
        intent.putExtra(BaseActivity.EXTRA_EXTRA, extra);
        intent.putExtra(BaseActivity.EXTRA_EXTRA2, extra2);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_flower)
                .setColor(ContextCompat.getColor(context, R.color.material_pink_500))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent);

        if (Settings.instance(context).getBoolean(Settings.NOTIFICATION_SOUND, true)) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(defaultSoundUri);
        }

        int priority = Settings.instance(context).getIntFromString(Settings.NOTIFICATION_PRIORITY, NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setPriority(priority);
        if (priority >= NotificationCompat.PRIORITY_HIGH
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVibrate(new long[0]);
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (id == -1) {
            id = (int) System.currentTimeMillis() / 1000;
        }
        notificationManager.notify(id, notificationBuilder.build());
    }

    private static Intent getIntent(Context context, String activity) {
        Intent intent;
        if (activity != null) {
            switch (activity) {
                case "EquipDisplayActivity":
                    return new Intent(context, EquipDisplayActivity.class);
                case "ShipDisplayActivity":
                    return new Intent(context, ShipDisplayActivity.class);
                case "MapActivity":
                    return new Intent(context, MapActivity.class);
            }
        }

        intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
