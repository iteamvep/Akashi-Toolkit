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

import rikka.akashitoolkit.model.Map;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MapList {
    private static final String FILE_NAME = "Map.json";

    private static List<Map> sList;

    public static synchronized List<Map> get(Context context) {
        if (sList == null) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStream ims = assetManager.open(FILE_NAME);
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(ims);
                Type listType = new TypeToken<ArrayList<Map>>() {}.getType();
                sList = gson.fromJson(reader, listType);
            } catch (IOException e) {
                e.printStackTrace();
                sList = new ArrayList<>();
            }
        }
        return sList;
    }
}
