package rikka.akashitoolkit.staticdata;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipList {
    private static final String FILE_NAME = "Equip.json";

    private static List<Equip> sList;

    public static synchronized List<Equip> get(final Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Equip>() {
                @Override
                public void afterRead(List<Equip> list) {
                    sort(list);
                    setBookmarked(context, list);
                    modifyItemIntroduction(list);
                }
            }.get(context, FILE_NAME, new TypeToken<ArrayList<Equip>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    @SuppressLint("DefaultLocale")
    private static void setBookmarked(Context context, List<Equip> list) {
        for (Equip equip :
                list) {
            equip.setBookmarked(Settings.instance2(context)
                    .getBoolean(String.format("equip_%d", equip.getId()), false));
        }
    }

    private static void sort(List<Equip> list) {
        List<Equip> newList = new ArrayList<>();
        int curType = 1;
        while (newList.size() != list.size()) {
            for (Equip item :
                    list) {
                if (curType == item.getSubType()) {
                    newList.add(item);
                }
            }
            curType ++;
        }
        list.clear();
        list.addAll(newList);
    }

    private static void modifyItemIntroduction(List<Equip> list) {
        for (Equip item :
                list) {
            item.getIntroduction().setZh_cn(
                    item.getIntroduction().getZh_cn().replace("<ref>", "（").replace("</ref>", "）"));
        }
    }

    public static Equip findItemById(Context context, int id) {
        for (Equip item :
             get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
