package rikka.akashitoolkit.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rikka on 2016/9/26.
 */

public class FileUtils {

    public static File saveStreamToCacheFile(Context context, InputStream inputStream, String name) {
        String FilePath = context.getCacheDir().getAbsolutePath() + name;

        return saveStreamToFile(inputStream, FilePath);
    }

    public static File saveStreamToFile(InputStream inputStream, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(path);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void copyFile(File src, File dst) throws IOException {
        if (!dst.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            dst.getParentFile().mkdirs();
        }
        //noinspection ResultOfMethodCallIgnored
        dst.createNewFile();

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }
}
