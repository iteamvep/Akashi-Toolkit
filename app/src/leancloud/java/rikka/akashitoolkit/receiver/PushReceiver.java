package rikka.akashitoolkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import rikka.akashitoolkit.model.PushMessage;
import rikka.akashitoolkit.support.PushHandler;

/**
 * Created by Rikka on 2016/5/2.
 */
public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("rikka.akashitool.PUSH_MESSAGE")) {
                PushMessage message = new Gson().fromJson(intent.getExtras().getString("com.avos.avoscloud.Data"), PushMessage.class);
                if (message != null) {
                    PushHandler.sendNotification(getApplicationContext(), message);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
