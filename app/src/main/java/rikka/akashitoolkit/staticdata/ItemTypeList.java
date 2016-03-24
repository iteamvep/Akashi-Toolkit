package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rikka.akashitoolkit.model.ItemType;

/**
 * Created by Rikka on 2016/3/24.
 */
public class ItemTypeList {
    private static final String FILE_NAME = "ItemType.json";

    private static List<ItemType> sList;
    private static Map<String, Integer> sParentList;

    public static synchronized List<ItemType> get(Context context) {
        if (sList == null) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStream ims = assetManager.open(FILE_NAME);
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(ims);
                Type listType = new TypeToken<ArrayList<ItemType>>() {}.getType();
                sList = gson.fromJson(reader, listType);

                generateParentList(context);
            } catch (IOException e) {
                e.printStackTrace();
                sList = new ArrayList<>();
            }
        }
        return sList;
    }

    public static synchronized Map<String, Integer> getsParentList(Context context) {
        if (sParentList == null) {
            get(context);
        }
        return sParentList;
    }

    public static ItemType findItemById(Context context, int id) {
        for (ItemType item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private static void generateParentList(Context context) {
        if (sParentList != null) {
            return;
        }

        sParentList = new HashMap<>();

        for (ItemType item:
                get(context)) {

            if (sParentList.get(item.getParent()) == null) {
                sParentList.put(item.getParent(), sParentList.size());
            }
        }
    }
}
