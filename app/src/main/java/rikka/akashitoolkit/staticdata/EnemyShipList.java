package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.EnemyShip;

/**
 * Created by Rikka on 2016/4/23.
 */
public class EnemyShipList {
    private static final String FILE_NAME = "EnemyShip.json";

    private static List<EnemyShip> sList;

    public static synchronized List<EnemyShip> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<EnemyShip>().get(context, FILE_NAME, new TypeToken<ArrayList<EnemyShip>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static EnemyShip findItemById(Context context, int id) {
        for (EnemyShip item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
