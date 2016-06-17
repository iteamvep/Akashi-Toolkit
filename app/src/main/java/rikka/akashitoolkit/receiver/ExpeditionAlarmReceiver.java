package rikka.akashitoolkit.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Expedition;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.support.Settings;

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
            Settings.instance(context).putLong(String.format("expedition_time_%d", id), 0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_flower)
                    .setColor(ContextCompat.getColor(context, R.color.material_pink_500))
                    .setContentText(context.getString(R.string.expedition_finished))
                    .setContentTitle(expedition.getName().get(context))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setLights(ContextCompat.getColor(context, R.color.material_pink_500), 1000, 1000)
                    .setAutoCancel(true);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id + 10000, notificationBuilder.build());
        }
    }
}
