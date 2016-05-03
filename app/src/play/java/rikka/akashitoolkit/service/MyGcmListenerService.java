package rikka.akashitoolkit.service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import rikka.akashitoolkit.support.PushHandler;

/**
 * Created by Rikka on 2016/5/3.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(data);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    private void sendNotification(Bundle data) {
        int id;
        try {
            id = Integer.parseInt(data.getString("id"));
        } catch (Exception e) {
            id = 0;
        }

        PushHandler.sendNotification(getApplicationContext(),
                id,
                data.getString("title"),
                data.getString("message"),
                data.getString("activity"),
                data.getString("extra"),
                data.getString("extra2"));
    }
}
