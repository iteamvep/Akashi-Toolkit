package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/4/30.
 */
public class BaseGSON {
    public Object get(Context context, String filename, Class c) {
        InputStream is;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/json/" + filename);
            if (file.exists()) {
                is = new FileInputStream(file);
                return read(is, c);
            }

            AssetManager assetManager = context.getAssets();
            is = assetManager.open(filename);
            return read(is, c);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseGSON", "bad file: " + filename);
        }
        return null;
    }

    private Object read(InputStream is, Class c) {
        Object object;
        Reader reader = new InputStreamReader(is);
        object = new Gson().fromJson(reader, c);
        afterRead(object);
        return object;
    }

    public void afterRead(Object object) {
    }
}
