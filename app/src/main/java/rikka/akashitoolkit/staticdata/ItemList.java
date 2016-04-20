package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Item;

/**
 * Created by Rikka on 2016/4/19.
 */
public class ItemList {
    private static final String FILE_NAME = "Item.json";

    private static List<Item> sList;

    public static synchronized List<Item> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Item>() {
                @Override
                public void afterRead(List<Item> list) {
                }
            }.get(context, FILE_NAME, new TypeToken<ArrayList<Item>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static Item findItemById(Context context, int id) {
        for (Item item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
