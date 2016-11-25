package rikka.akashitoolkit.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Expedition;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.utils.PackageUtils;

/**
 * Created by Rikka on 2016/6/17.
 */
public class ExpeditionAlarmReceiver extends BroadcastReceiver {
    @SuppressLint("DefaultLocale")
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("ExpeditionAlarmReceiver_ID", -1);

        Expedition expedition = ExpeditionList.findItemById(context, id);

        if (expedition != null) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Settings.instance(context).putLong(String.format("expedition_time_%d", id), 0);

            Intent i = new Intent(context, ExpeditionAlarmResetReceiver.class);
            i.putExtra("ExpeditionAlarmReceiver_ID", id);
            PendingIntent actionIntent = PendingIntent.getBroadcast(context, id, i, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_flower)
                    .setGroup("expedition")
                    .setColor(ContextCompat.getColor(context, R.color.material_pink_500))
                    .setContentTitle(context.getString(R.string.expedition_finished))
                    .setContentText(expedition.getName().get())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setLights(ContextCompat.getColor(context, R.color.material_pink_500), 1000, 1000)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setWhen(System.currentTimeMillis())
                    .addAction(new NotificationCompat.Action(R.drawable.ic_alarm_plus_24dp, context.getString(R.string.reset_alarm), actionIntent));

            Intent launchIntent = PackageUtils.getLaunchIntent(context, "com.dmm.dmmlabo.kancolle");
            PendingIntent contentIntent = null;
            if (launchIntent != null) {
                contentIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_ONE_SHOT);
            }
            builder.setContentIntent(contentIntent);

            notificationManager.notify(id + 10000, builder.build());

            // group
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher_flower)
                        .setColor(ContextCompat.getColor(context, R.color.material_pink_500))
                        .setShowWhen(true)
                        .setWhen(System.currentTimeMillis())
                        .setGroup("expedition")
                        .setGroupSummary(true)
                        .setContentIntent(contentIntent);

                notificationManager.notify(10000, builder.build());
            }
        }
    }
}
