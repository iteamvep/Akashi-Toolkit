package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.AttrEntity;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.model.Ship;

/**
 * Created by Rikka on 2016/7/29.
 */
public class FleetList {

    private static final String JSON_NAME = "/json/fleets.json";

    private static List<Fleet> sList;

    public static synchronized List<Fleet> get(Context context) {
        if (sList == null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            try {
                sList = gson.fromJson(
                        new FileReader(context.getFilesDir() + JSON_NAME),
                        new TypeToken<ArrayList<Fleet>>() {
                        }.getType());
            } catch (Exception e) {
                sList = new ArrayList<>();
            }
        }

        init(context, sList);

        return sList;
    }

    /**
     * 检查数据保证不会出现 NullPointerException
     * 计算获得制空值等
     *
     * @param context Context
     * @param list    fleet list
     */
    private static void init(Context context, List<Fleet> list) {
        for (Fleet fleet : list) {
            fleet.init(context);
        }
    }
}
