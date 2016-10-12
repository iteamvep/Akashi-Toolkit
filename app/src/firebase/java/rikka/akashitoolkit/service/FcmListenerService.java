package rikka.akashitoolkit.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Rikka on 2016/5/3.
 */
public class FcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "FcmListenerService";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        sendNotification(message.getData());
    }

    private void sendNotification(Map<String, String> data) {
        /*int id;
        try {
            id = Integer.parseInt(data.getString("id"));
        } catch (Exception e) {
            id = 0;
        }*/

        /*PushHandler.sendNotification(getApplicationContext(),
                id,
                data.getString("title"),
                data.getString("message"),
                data.getString("activity"),
                data.getString("extra"),
                data.getString("extra2"));*/
    }
}
