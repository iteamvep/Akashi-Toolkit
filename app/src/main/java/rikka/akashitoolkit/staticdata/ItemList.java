package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Item;

/**
 * Created by Rikka on 2016/3/23.
 */
public class ItemList {
    private static final String FILE_NAME = "Item.json";

    private static List<Item> sList;

    public static synchronized List<Item> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Item>() {
                @Override
                public void afterRead(List<Item> list) {
                    sort(list);
                    modifyItemIntroduction(list);
                }
            }.get(context, FILE_NAME, new TypeToken<ArrayList<Item>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    private static void sort(List<Item> list) {
        List<Item> newList = new ArrayList<>();
        int curType = 1;
        while (newList.size() != list.size()) {
            for (Item item :
                    list) {
                if (curType == item.getSubType()) {
                    newList.add(item);
                }
            }
            curType ++;
        }
        list.clear();
        list.addAll(newList);
    }

    private static void modifyItemIntroduction(List<Item> list) {
        for (Item item :
                list) {
            item.getIntroduction().setZh_cn(
                    item.getIntroduction().getZh_cn().replace("<ref>", "（").replace("</ref>", "）"));
        }
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
