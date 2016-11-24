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
import android.text.TextUtils;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.equip_detail.EquipDetailActivity;
import rikka.akashitoolkit.model.PushMessage;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.map.MapActivity;
import rikka.akashitoolkit.ship_detail.ShipDetailActivity;
import rikka.akashitoolkit.utils.IntentUtils;
import rikka.akashitoolkit.utils.LocaleUtils;

/**
 * Created by Rikka on 2016/5/3.
 */
public class PushHandler {

    public static void sendNotification(Context context, PushMessage message) {
        int language = LocaleUtils.getAppLanguage(context);

        sendNotification(context,
                message.getId(),
                message.getTitle().get(language),
                message.getMessage().get(language),
                message.getUri());
    }

    public static void sendNotification(Context context, int id, String title, String message, String uri) {
        if (title == null) {
            title = context.getString(R.string.app_name);
        }

        PendingIntent pendingIntent;
        if (TextUtils.isEmpty(uri)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(BaseActivity.EXTRA_FROM_NOTIFICATION, true);

            pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

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

        if (id == 0) {
            id = (int) System.currentTimeMillis() / 1000;
        }
        notificationManager.notify(id, notificationBuilder.build());
    }
}
