package rikka.akashitoolkit.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.avos.avoscloud.AVOSCloud;

import org.json.JSONObject;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/5/2.
 */
public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("rikka.akashitool.PUSH_MESSAGE")) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                final String message = json.getString("alert");
                final String title = json.getString("title");
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.mipmap.ic_launcher_flower)
                                .setColor(AVOSCloud.applicationContext.getResources().getColor(R.color.material_pink_500))
                                .setContentTitle(title)
                                .setContentText(message)
                                .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);

                int mNotificationId = 10086;
                NotificationManager mNotifyMgr =
                        (NotificationManager) AVOSCloud.applicationContext
                                .getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        } catch (Exception ignored) {

        }
    }
}
