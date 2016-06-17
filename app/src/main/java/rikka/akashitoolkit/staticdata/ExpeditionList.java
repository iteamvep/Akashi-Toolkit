package rikka.akashitoolkit.staticdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.Expedition;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionList {
    private static final String FILE_NAME = "Expedition.json";

    private static List<Expedition> sList;

    public static synchronized List<Expedition> get(final Context context) {
        if (sList == null) {
            sList = new BaseGSONList<Expedition>() {
                @Override
                public void afterRead(List<Expedition> list) {
                    setBookmarked(context, list);
                }
            }.get(context, FILE_NAME, new TypeToken<ArrayList<Expedition>>() {
            }.getType());
        }
        return sList;
    }

    @SuppressLint("DefaultLocale")
    private static void setBookmarked(Context context, List<Expedition> list) {
        for (Expedition item :
                list) {
            item.setBookmarked(Settings.instance(context)
                    .getBoolean(String.format("expedition_%d", item.getId()), false));
        }
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static Expedition findItemById(Context context, int id) {
        for (Expedition item :
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
