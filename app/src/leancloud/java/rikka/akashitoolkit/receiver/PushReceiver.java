package rikka.akashitoolkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import rikka.akashitoolkit.support.PushHandler;

/**
 * Created by Rikka on 2016/5/2.
 */
public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("rikka.akashitool.PUSH_MESSAGE")) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                String message = getJSONString(json, "message");
                String title = getJSONString(json, "title");
                int id = getJSONInt(json, "id");
                String activity = getJSONString(json, "activity");
                String extra = getJSONString(json, "extra");
                String extra2 = getJSONString(json, "extra2");

                PushHandler.sendNotification(context.getApplicationContext(),
                        id,
                        title,
                        message,
                        activity,
                        extra,
                        extra2);
            }
        } catch (Exception ignored) {
        }
    }

    private String getJSONString(JSONObject json, String name) {
        try {
            return json.getString(name);
        } catch (JSONException e) {
            return null;
        }
    }

    private int getJSONInt(JSONObject json, String name) {
        try {
            return json.getInt(name);
        } catch (JSONException e) {
            return 0;
        }
    }
}
