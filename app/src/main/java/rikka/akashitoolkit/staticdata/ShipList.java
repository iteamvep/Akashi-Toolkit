package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipList {
    private static final String FILE_NAME = "Ship.json";

    private static List<Ship> sList;

    public static synchronized List<Ship> get(final Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Ship>() {
                @Override
                public void afterRead(List<Ship> list) {

                    sortById(list);

                    if (Settings
                            .instance(context)
                            .getInt(Settings.SHIP_SORT, 0) == 0) {
                        sort(list);
                    } else {
                        sortByClass(list);
                    }

                }
            }.get(context, FILE_NAME, new TypeToken<ArrayList<Ship>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static synchronized void sortById(List<Ship> list) {
        Collections.sort(list, new Comparator<Ship>() {
            @Override
            public int compare(Ship lhs, Ship rhs) {
                return lhs.getId() - rhs.getId();
            }
        });
    }

    public static synchronized void sort() {
        if (sList == null) {
            throw new NullPointerException();
        }

        sortById(sList);
        sort(sList);
    }

    public static synchronized void sort(List<Ship> list) {
        Ship[] ships = new Ship[list.size()];
        boolean[] added = new boolean[list.size()];

        list.toArray(ships);

        List<Ship> newList = new ArrayList<>();
        int curType = 1;
        while (newList.size() < list.size()) {
            for (int i = 0; i < ships.length; i++) {
                if (!added[i] && curType == ships[i].getType()) {
                    newList.add(ships[i]);
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

                        newList.add(ships[index]);
                        added[index] = true;

                        to = ships[index].getRemodel().getId_to();
                    }
                }
            }
            curType ++;
        }
        list.clear();
        list.addAll(newList);
    }

    public static synchronized void sortByClass() {
        if (sList == null) {
            throw new NullPointerException();
        }

        sortById(sList);
        sortByClass(sList);
    }

    public static synchronized void sortByClass(List<Ship> list) {
        Ship[] ships = new Ship[list.size()];
        boolean[] added = new boolean[list.size()];

        list.toArray(ships);

        List<Ship> newList = new ArrayList<>();
        int curType = 0;
        while (newList.size() < list.size()) {
            for (int i = 0; i < ships.length; i++) {
                if (!added[i] && curType == ships[i].getCtype()) {
                    newList.add(ships[i]);
                    added[i] = true;

                    int to = ships[i].getRemodel().getId_to();
                    while (to > 0) {
                        int index = findItemById(to, ships);
                        if (index == -1
                                || curType != ships[index].getCtype()
                                /*|| ships[index].getRemodel().getId_to() == ships[index].getRemodel().getId_from()*/
                                || added[index]) {
                            break;
                        }

                        newList.add(ships[index]);
                        added[index] = true;

                        to = ships[index].getRemodel().getId_to();
                    }
                }
            }
            curType++;
        }
        list.clear();
        list.addAll(newList);
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

    public static Ship findItemByWikiId(Context context, String wiki_id) {
        for (Ship item:
                get(context)) {
            if (item.getWiki_id().equals(wiki_id)) {
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
