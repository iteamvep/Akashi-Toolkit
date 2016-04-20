package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.MapType;

/**
 * Created by Rikka on 2016/4/20.
 */
public class MapTypeList {
    private static final String FILE_NAME = "MapType.json";

    private static List<MapType> sList;

    public static synchronized List<MapType> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<MapType>().get(context, FILE_NAME, new TypeToken<ArrayList<MapType>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }
}
