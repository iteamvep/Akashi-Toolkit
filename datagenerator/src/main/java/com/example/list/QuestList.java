package com.example.list;

import com.example.model.Quest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by Rikka on 2016/4/16.
 */
public class QuestList {
    private static List<Quest> sList;

    public QuestList() throws FileNotFoundException {
    }

    public static List<Quest> get() {
        if (sList == null) {
            try {
                sList = new Gson().fromJson(new JsonReader(new FileReader("app/src/main/assets/Equip.json")), new TypeToken<List<Quest>>() {
                }.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sList;
    }

    public static Quest findById(int id) {
        for (Quest item :
                get()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static Quest findByCode(String name) {
        return findByCode(name, get());
    }

    public static Quest findByCode(String name, List<Quest> list) {
        for (Quest item :
                list) {
            if (item.getCode().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
