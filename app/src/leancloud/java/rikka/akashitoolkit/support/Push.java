package rikka.akashitoolkit.support;

import android.content.Context;
import android.util.ArraySet;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;

import java.util.HashSet;
import java.util.Set;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.MainActivity;
import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/5/3.
 */
public class Push {

    public static void init(Context context) {
        PushService.setDefaultPushCallback(context, MainActivity.class);

        resetSubscribedChannels(context);
    }

    public static void resetSubscribedChannels(Context context) {
        String[] topics = context.getResources().getStringArray(R.array.push_topics_value);
        Set<String> subscribed = Settings.instance().getStringSet(Settings.PUSH_TOPICS, new HashSet<String>());
        for (String s : subscribed) {
            PushService.subscribe(context, s, MainActivity.class);
        }
        for (String s : topics) {
            if (!subscribed.contains(s)) {
                PushService.unsubscribe(context, s);
            }
        }

        if (BuildConfig.DEBUG) {
            PushService.subscribe(context, "debug", MainActivity.class);
        }
        AVInstallation.getCurrentInstallation().saveInBackground();
    }
}
