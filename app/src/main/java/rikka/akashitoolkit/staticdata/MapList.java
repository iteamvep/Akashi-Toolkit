package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Map;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MapList {
    private static final String FILE_NAME = "Map.json";

    private static List<Map> sList;

    public static synchronized List<Map> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Map>().get(context, FILE_NAME, new TypeToken<ArrayList<Map>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }
}
