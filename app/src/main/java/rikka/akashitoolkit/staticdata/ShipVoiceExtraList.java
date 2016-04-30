package rikka.akashitoolkit.staticdata;

import android.content.Context;

import rikka.akashitoolkit.model.ShipVoice;

/**
 * Created by Rikka on 2016/4/30.
 */
public class ShipVoiceExtraList {
    private static final String FILE_NAME = "ShipVoiceExtra.json";

    private static ShipVoice sList;

    public static synchronized ShipVoice get(Context context) {
        if (sList == null) {
            sList = (ShipVoice) new BaseGSON().get(context, FILE_NAME, ShipVoice.class);
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }
}
