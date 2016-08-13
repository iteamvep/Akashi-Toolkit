package rikka.akashitoolkit.event;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.staticdata.BaseGSONList;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventMapList {
    private static final String FILE_NAME = "EventMap.json";
    private static List<EventMap> sList;

    public static synchronized List<EventMap> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<EventMap>().get(
                    context,
                    FILE_NAME,
                    new TypeToken<ArrayList<EventMap>>() {
                    }.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static EventMap findItemById(Context context, int id) {
        for (EventMap item :
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
