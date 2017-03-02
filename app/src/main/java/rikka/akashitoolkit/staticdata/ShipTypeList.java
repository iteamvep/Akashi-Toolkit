package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.ShipType;

/**
 * Created by Rikka on 2016/3/27.
 */
public class ShipTypeList {
    private static final String FILE_NAME = "ShipType.json";

    private static List<ShipType> sList;

    public static void init(Context context) {
        get(context);
    }

    public static synchronized List<ShipType> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<ShipType>().get(context, FILE_NAME, new TypeToken<ArrayList<ShipType>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static ShipType findItemById(int id) {
        return findItemById(id, sList);
    }

    public static ShipType findItemById(Context context, int id) {
        return findItemById(id, get(context));
    }

    public static ShipType findItemById(int id, List<ShipType> ships) {
        for (ShipType item : ships) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
