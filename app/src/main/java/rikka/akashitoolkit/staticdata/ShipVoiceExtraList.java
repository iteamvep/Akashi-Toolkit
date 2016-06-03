package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.ShipVoice;

/**
 * Created by Rikka on 2016/4/30.
 */
public class ShipVoiceExtraList {
    private static final String FILE_NAME = "ShipVoiceExtra.json";

    private static List<ShipVoice> sList;

    public static synchronized List<ShipVoice> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<ShipVoice>().get(context, FILE_NAME, new TypeToken<ArrayList<ShipVoice>>() {
            }.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }
}
