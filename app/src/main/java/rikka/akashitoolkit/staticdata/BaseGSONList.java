package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/4/13.
 */
public class BaseGSONList<T> {

    public List<T> get(Context context, String filename, Type type) {
        InputStream is;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/json/" + filename);
            if (file.exists()) {
                is = new FileInputStream(file);
                return read(is, type);
            }

            AssetManager assetManager = context.getAssets();
            is = assetManager.open(filename);
            return read(is, type);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseGSONList", "bad file: " + filename);
        }
        return new ArrayList<>();
    }

    private List<T> read(InputStream is, Type type) {
        List<T> list;
        Reader reader = new InputStreamReader(is);
        list = new Gson().fromJson(reader, type);
        afterRead(list);
        return list;
    }

    public void afterRead(List<T> list) {
    }

}