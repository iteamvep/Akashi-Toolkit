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
import java.util.List;

import rikka.akashitoolkit.model.Ship;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipList {
    private static final String FILE_NAME = "Ship.json";

    private static List<Ship> sList;

    public static synchronized List<Ship> get(Context context) {
        if (sList == null) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStream ims = assetManager.open(FILE_NAME);
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(ims);
                Type listType = new TypeToken<ArrayList<Ship>>() {}.getType();
                sList = gson.fromJson(reader, listType);
            } catch (IOException e) {
                e.printStackTrace();
                sList = new ArrayList<>();
            }
        }
        return sList;
    }

    public static Ship findItemById(Context context, int id) {
        for (Ship item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
