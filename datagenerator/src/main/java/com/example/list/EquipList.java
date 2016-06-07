package com.example.list;

import com.example.model.Equip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by Rikka on 2016/4/16.
 */
public class EquipList {
    private static List<Equip> sList;

    public static List<Equip> get() {
        if (sList == null) {
            try {
                sList = new Gson().fromJson(new JsonReader(new FileReader("app/src/main/assets/Equip.json")), new TypeToken<List<Equip>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sList;
    }

    public static Equip findById(int id) throws FileNotFoundException {
        for (Equip item :
                get()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static Equip findByName(String name) {
        return findByName(name, get());
    }

    public static Equip findByName(String name, List<Equip> list) {
        for (Equip item :
                list) {
            if (item.getName().getJa().equals(name)
                    || item.getName().getZh_cn().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
