package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.ShipClass;

/**
 * Created by Rikka on 2016/5/10.
 */
public class ShipClassList {
    private static final String FILE_NAME = "ShipClass.json";

    private static List<ShipClass> sList;

    public static synchronized List<ShipClass> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<ShipClass>().get(context, FILE_NAME, new TypeToken<ArrayList<ShipClass>>() {
            }.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static ShipClass findItemById(Context context, int id) {
        for (ShipClass item :
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
