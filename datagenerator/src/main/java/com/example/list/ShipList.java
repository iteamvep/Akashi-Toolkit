package com.example.list;

import com.example.model.Ship;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by Rikka on 2016/4/16.
 */
public class ShipList {
    private static List<Ship> sList;

    public static List<Ship> get() {
        if (sList == null) {
            try {
                sList = new Gson().fromJson(new JsonReader(new FileReader("app/src/main/assets/Ship.json")), new TypeToken<List<Ship>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sList;
    }

    public static Ship findById(int id) {
        for (Ship item :
                get()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static Ship findByName(String name) {
        return findByName(name, get());
    }

    public static Ship findByName(String name, List<Ship> list) {
        for (Ship item :
                list) {
            if (item.getName().getJa().equals(name)
                    || item.getName().getZh_cn().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public static int findIdByName(String name) {
        return findIdByName(name, get());
    }

    public static int findIdByName(String name, List<Ship> list) {
        for (Ship item : list) {
            if (item.getName().getJa().equals(name)
                    || item.getName().getZh_cn().equals(name)) {
                return item.getId();
            }
        }
        return 0;
    }

}
