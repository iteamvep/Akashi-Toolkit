package rikka.akashitoolkit.staticdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
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

                    if (Settings
                            .instance(context)
                            .getInt(Settings.SHIP_SORT, 0) == 0) {
                        _sortByType(list);
                    } else {
                        _sortByClass(list);
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

    public static synchronized void sortByType() {
        _sortByType(sList);
    }

    public static synchronized void sortByClass() {
        _sortByClass(sList);
    }

    private static synchronized void _sortByClass(List<Ship> list) {
        if (list == null) {
            throw new NullPointerException();
        }

        sortById(list);
        sortByRemodel(list);
        sortByClass(list);
        //sortByType(list);
    }

    private static synchronized void _sortByType(List<Ship> list) {
        if (list == null) {
            throw new NullPointerException();
        }

        sortById(list);
        sortByRemodel(list);
        //sortByClass(list);
        sortByType(list);
    }

    private static synchronized void sortById(List<Ship> list) {
        Collections.sort(list, new Comparator<Ship>() {
            @Override
            public int compare(Ship lhs, Ship rhs) {
                return lhs.getId() - rhs.getId();
            }
        });
    }

    private static synchronized void sortByType(List<Ship> list) {
        Collections.sort(list, new Comparator<Ship>() {
            @Override
            public int compare(Ship lhs, Ship rhs) {
                return lhs.getType() - rhs.getType();
            }
        });
    }

    private static synchronized void sortByType(List<Ship> list, List<List<Ship>> _list) {
        Collections.sort(_list, new Comparator<List<Ship>>() {
            @Override
            public int compare(List<Ship> lhs, List<Ship> rhs) {
                return (lhs.get(0).getType() - rhs.get(0).getType());
            }
        });

        list.clear();
        for (List<Ship> l : _list) {
            for (Ship s : l) {
                list.add(s);
            }
        }
    }

    private static synchronized void sortByRemodel(List<Ship> list) {
        List<List<Ship>> newShips = new ArrayList<>();
        boolean[] added = new boolean[2000];

        int count = 0;
        while (count < list.size()) {
            for (Ship ship : list) {
                if (added[ship.getId()]) {
                    continue;
                }

                List<Ship> newList = new ArrayList<>();
                Ship s = ship;
                if (s.getRemodel() != null) {
                    while (s.getRemodel().getFromId() != 0) {
                        Ship next = findItemById(s.getRemodel().getFromId(), list);
                        if (next == null) {
                            break;
                        }
                        s = next;
                    }
                }

                while (true) {
                    newList.add(s);
                    count++;
                    added[s.getId()] = true;

                    if (s.getRemodel() == null ||
                            s.getRemodel().getToId() == s.getRemodel().getFromId()) {
                        break;
                    }

                    Ship next = findItemById(s.getRemodel().getToId(), list);
                    if (next == null) {
                        break;
                    }
                    s = next;
                }
                newShips.add(newList);
            }
        }

        sortByType(list, newShips);
    }

    private static synchronized void sortByClass(List<Ship> list) {
        Collections.sort(list, new Comparator<Ship>() {
            @Override
            public int compare(Ship lhs, Ship rhs) {
                return (lhs.getClassType() * 100 + lhs.getClassNum()) - (rhs.getClassType() * 100 + rhs.getClassNum());
            }
        });
    }


    public static Ship findItemById(Context context, int id) {
        return findItemById(id, get(context));
    }

    public static Ship findItemById(int id, List<Ship> ships) {
        for (Ship item : ships) {
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
}
