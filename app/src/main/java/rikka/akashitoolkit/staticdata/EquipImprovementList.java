package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.EquipImprovement;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovementList {
    private static final String FILE_NAME = "EquipImprovement.json";
    private static List<EquipImprovement> sList;

    public static synchronized List<EquipImprovement> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<EquipImprovement>().get(
                    context,
                    FILE_NAME,
                    new TypeToken<ArrayList<EquipImprovement>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static EquipImprovement findItemById(Context context, int id) {
        for (EquipImprovement item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
