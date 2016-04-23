package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.MapDetail;

/**
 * Created by Rikka on 2016/4/23.
 */
public class MapDetailList {
    private static final String FILE_NAME = "MapDetail.json";

    private static List<MapDetail> sList;

    public static synchronized List<MapDetail> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<MapDetail>().get(context, FILE_NAME, new TypeToken<ArrayList<MapDetail>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static MapDetail findItemById(Context context, int id) {
        for (MapDetail item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
