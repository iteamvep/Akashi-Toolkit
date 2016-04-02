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
import java.util.Collection;
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
                sort();
            } catch (IOException e) {
                e.printStackTrace();
                sList = new ArrayList<>();
            }
        }
        return sList;
    }

    private static void sort() {
        Ship[] ships = new Ship[sList.size()];
        boolean[] added = new boolean[sList.size()];

        sList.toArray(ships);

        List<Ship> list = new ArrayList<>();
        int curType = 1;
        while (list.size() < sList.size()) {
            for (int i = 0; i < ships.length; i++) {
                if (!added[i] && curType == ships[i].getType()) {
                    list.add(ships[i]);
                    added[i] = true;

                    int to = ships[i].getRemodel().getId_to();
                    while (to > 0) {
                        int index = findItemById(to, ships);
                        if (index == -1
                                || curType != ships[index].getType()
                                /*|| ships[index].getRemodel().getId_to() == ships[index].getRemodel().getId_from()*/
                                || added[index]) {
                            break;
                        }

                        list.add(ships[index]);
                        added[index] = true;

                        to = ships[index].getRemodel().getId_to();
                    }
                }
            }
            curType ++;
        }
        sList.clear();
        sList.addAll(list);
    }

    public static int findItemById(int id, Ship[] ships) {
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].getId() == id) {
                return i;
            }
        }
        return -1;
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

    public static final String[] shipType = {
            "",
            "海防艦",
            "駆逐艦",
            "軽巡洋艦",
            "重雷装巡洋艦",
            "重巡洋艦",
            "航空巡洋艦",
            "軽空母",
            "高速戦艦",
            "低速戦艦",
            "航空戦艦",
            "正規空母",
            "超弩級戦艦",
            "潜水艦",
            "潜水空母",
            "補給艦",
            "水上機母艦",
            "揚陸艦",
            "装甲空母",
            "工作艦",
            "潜水母艦",
            "練習巡洋艦",
            "補給艦"
    };
}
