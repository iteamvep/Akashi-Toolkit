package rikka.akashitoolkit.support;

import android.content.Context;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;

import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/5/3.
 */
public class Push {
    public static void init(Context context) {
        PushService.setDefaultPushCallback(context, MainActivity.class);
        PushService.subscribe(context, "public", MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();
    }
}
