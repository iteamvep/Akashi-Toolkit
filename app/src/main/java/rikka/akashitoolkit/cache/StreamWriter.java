package rikka.akashitoolkit.cache;

import com.bumptech.glide.load.engine.cache.DiskCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 实现 Glide 的 DiskCache.Writer
 * <p/>
 * 直接写文件..
 */
public class StreamWriter implements DiskCache.Writer {
    private final InputStream inputStream;

    public StreamWriter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public boolean write(File file) {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
