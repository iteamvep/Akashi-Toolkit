package rikka.akashitoolkit.staticdata;

import android.annotation.SuppressLint;
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
                    setBookmarked(context, list);
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

    @SuppressLint("DefaultLocale")
    private static void setBookmarked(Context context, List<Ship> list) {
        for (Ship ship :
                list) {
            if (ship.getId() > 500) {
                continue;
            }

            ship.setBookmarked(Settings.instance(context)
                    .getBoolean(String.format("ship_%d_%d", ship.getClassType(), ship.getClassNum()), false));
        }
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

                    int to;
                    if (ships[i].getRemodel() != null)
                        to = ships[i].getRemodel().getToId();
                    else
                        to = 0;

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

                        to = ships[index].getRemodel().getToId();
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
        /*Ship[] ships = new Ship[list.size()];
        boolean[] added = new boolean[list.size()];

        list.toArray(ships);

        List<Ship> newList = new ArrayList<>();
        int curType = 0;
        while (newList.size() < list.size()) {
            for (int i = 0; i < ships.length; i++) {
                if (!added[i] && curType == ships[i].getClassType()) {
                    newList.add(ships[i]);
                    added[i] = true;

                    int to;
                    if (ships[i].getRemodel() != null)
                        to = ships[i].getRemodel().getToId();
                    else
                        to = 0;

                    while (to > 0) {
                        int index = findItemById(to, ships);
                        if (index == -1
                                || curType != ships[index].getClassType()
                                || added[index]) {
                            break;
                        }

                        newList.add(ships[index]);
                        added[index] = true;

                        to = ships[index].getRemodel().getToId();
                    }
                }
            }
            curType++;
        }
        list.clear();*/

        Collections.sort(list, new Comparator<Ship>() {
            @Override
            public int compare(Ship lhs, Ship rhs) {
                /*if (lhs.getId() >= 500 && rhs.getId() < 500) {
                    return 9;
                }
                if (lhs.getClassType() != rhs.getClassType()) {
                    return 1;
                }
                if (lhs.getClassNum() < rhs.getClassNum()) {
                    return -1;
                }*/
                return (lhs.getClassType() * 100 + lhs.getClassNum()) - (rhs.getClassType() * 100 + rhs.getClassNum());
            }
        });
        //list.addAll(newList);
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
            if (item.getWikiId().equals(wiki_id)) {
                return item;
            }
        }
        return null;
    }

    public static final String[] shipType = {
            "",
            "海防舰",
            "驱逐舰",
            "轻巡洋舰",
            "重雷装巡洋舰",
            "重巡洋舰",
            "航空巡洋舰",
            "轻空母",
            "高速战舰",
            "低速战舰",
            "航空战舰",
            "正规空母",
            "超弩级战舰",
            "潜水舰",
            "潜水空母",
            "补给舰",
            "水上机母舰",
            "扬陆舰",
            "装甲空母",
            "工作舰",
            "潜水母舰",
            "练习巡洋舰",
            "补给舰"
    };

    public static final String[] shipType2 = {
            "",
            "海防舰",
            "驱逐舰 (DD)",
            "轻巡洋舰 (CL)",
            "重雷装巡洋舰 (CLT)", // 5

            "重巡洋舰 (CA)",
            "航空巡洋舰 (CAV)",
            "轻空母 (CVL)",
            "高速战舰 (BB)",
            "低速战舰 (BB)", // 10

            "航空战舰 (BBV)",
            "正规空母 (CV)",
            "超弩级战舰",
            "潜水舰 (SS)",
            "潜水空母 (SSV)", // 15

            "补给舰 (AP)",
            "水上机母舰 (AV)",
            "扬陆舰 (LHA)",
            "装甲空母 (CVB)",
            "工作舰 (AR)",

            "潜水母舰 (AS)",
            "练习巡洋舰 (CLP)",
            "补给舰 (AP)"
    };
}
