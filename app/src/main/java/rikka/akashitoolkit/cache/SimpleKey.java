package rikka.akashitoolkit.cache;

import com.bumptech.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 实现 Glide 的 Key
 * <p/>
 * 只根据名字
 */
public class SimpleKey implements Key {
    private final String id;

    public SimpleKey(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SimpleKey) {
            if (this == o) {
                return true;
            }

            if (((SimpleKey) o).id.equals(id)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(id.getBytes(STRING_CHARSET_NAME));
    }
}
