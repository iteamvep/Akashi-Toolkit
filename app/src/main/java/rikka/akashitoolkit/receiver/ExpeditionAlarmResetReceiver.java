package rikka.akashitoolkit.receiver;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.home.MessageExpeditionViewHolder;
import rikka.akashitoolkit.model.Expedition;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/11/25.
 */

public class ExpeditionAlarmResetReceiver extends BroadcastReceiver {

    @SuppressLint("DefaultLocale")
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("ExpeditionAlarmReceiver_ID", -1);

        Expedition expedition = ExpeditionList.findItemById(context, id);
        if (expedition != null) {
            // TODO refresh home UI

            long time = System.currentTimeMillis() + (expedition.getTime() - 1) * 1000 * 60;
            if (BuildConfig.DEBUG) {
                time = System.currentTimeMillis() + (expedition.getTime() - 1) * 1000;
            }
            Settings.instance(context).putLong(String.format("expedition_time_%d", expedition.getId()), time);
            MessageExpeditionViewHolder.setAlarm(context, time, expedition.getId());

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.cancel(id + 10000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                int count = 0;
                for (StatusBarNotification sbn : notificationManager.getActiveNotifications()) {
                    if (sbn.getId() >= 10001 && sbn.getId() <= 10050) {
                        count++;
                    }
                }
                if (count == 0) {
                    notificationManager.cancel(10000);
                }
            }
        }
    }
}
