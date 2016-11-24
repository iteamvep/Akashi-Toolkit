package rikka.akashitoolkit.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

import rikka.akashitoolkit.model.PushMessage;
import rikka.akashitoolkit.support.PushHandler;

/**
 * Created by Rikka on 2016/5/3.
 */
public class FcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "FcmListenerService";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();

        Log.d(TAG, "From: " + from);

        sendNotification(message.getData());
    }

    private void sendNotification(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, String> e : data.entrySet()) {
            sb.append("\"").append(e.getKey()).append("\"");
            if (e.getValue().startsWith("{")) {
                sb.append(":").append(e.getValue());

            } else {
                sb.append(":").append("\"").append(e.getValue()).append("\"");
            }
            sb.append(",");
        }
        if (data.entrySet().size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");

        PushMessage message = new Gson().fromJson(sb.toString(), PushMessage.class);
        if (message != null) {
            PushHandler.sendNotification(getApplicationContext(), message);
        }
    }
}
