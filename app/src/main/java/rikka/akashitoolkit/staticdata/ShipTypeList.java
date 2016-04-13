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

import rikka.akashitoolkit.model.ShipType;

/**
 * Created by Rikka on 2016/3/27.
 */
public class ShipTypeList {
    private static final String FILE_NAME = "ShipType.json";

    private static List<ShipType> sList;

    public static synchronized List<ShipType> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<ShipType>().get(context, FILE_NAME, new TypeToken<ArrayList<ShipType>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }
}
