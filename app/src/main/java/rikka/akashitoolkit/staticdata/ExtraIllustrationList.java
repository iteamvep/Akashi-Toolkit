package rikka.akashitoolkit.staticdata;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.ExtraIllustration;

/**
 * Created by Rikka on 2016/4/26.
 */
public class ExtraIllustrationList {
    private static final String FILE_NAME = "ExtraIllustration.json";

    private static List<ExtraIllustration> sList;

    public static synchronized List<ExtraIllustration> get(Context context) {
        if (sList == null) {
            sList = new BaseGSONList<ExtraIllustration>().get(context, FILE_NAME, new TypeToken<ArrayList<ExtraIllustration>>() {}.getType());
        }
        return sList;
    }

    public static synchronized void clear() {
        sList = null;
    }

    public static ExtraIllustration findItemById(Context context, int id) {
        for (ExtraIllustration item:
                get(context)) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
