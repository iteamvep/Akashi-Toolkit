package rikka.akashitoolkit.staticdata;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Quest;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/12.
 */
public class QuestList {
    private static final String FILE_NAME = "Quest.json";

    private static List<Quest> sList;

    public static synchronized List<Quest> get(final Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Quest>() {
                @Override
                public void afterRead(List<Quest> list) {
                    setBookmarked(context, list);
                }
            }.get(context, FILE_NAME, new TypeToken<ArrayList<Quest>>() {}.getType());
        }
        return sList;
    }

    @SuppressLint("DefaultLocale")
    private static void setBookmarked(Context context, List<Quest> list) {
        for (Quest quest :
                list) {
            boolean def = quest.getPeriod() != 0;
            quest.setBookmarked(Settings.instance(context)
                    .getBoolean(String.format("quest_%d", quest.getId()), def));
        }
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static Quest findItemByCode(Context context, String code) {
        for (Quest item:
                get(context)) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
