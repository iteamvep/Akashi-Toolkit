package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.EquipTypeParent;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipTypeParentList {

    private static List<EquipTypeParent> sList;

    public static void init(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<EquipTypeParent>() {
                @Override
                public void afterRead(List<EquipTypeParent> list) {
                }
            }.get(context, "EquipTypeParent.json", new TypeToken<ArrayList<EquipTypeParent>>() {
            }.getType());
        }
    }

    public static List<EquipTypeParent> getList() {
        return sList;
    }

    public static EquipTypeParent get(int id) {
        if (sList == null) {
            throw new RuntimeException("call init() first");
        }

        for (EquipTypeParent item : sList) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }
}
