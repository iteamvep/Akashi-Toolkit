package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Item;
import rikka.akashitoolkit.model.ItemImprovement;

/**
 * Created by Rikka on 2016/3/17.
 */
public class ItemImprovementList {
    private static final String FILE_NAME = "ItemImprovement.json";
    private static List<ItemImprovement> sList;

    public static synchronized List<ItemImprovement> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<ItemImprovement>().get(
                    context,
                    FILE_NAME,
                    new TypeToken<ArrayList<ItemImprovement>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static ItemImprovement findItemById(Context context, int id) {
        for (ItemImprovement item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
