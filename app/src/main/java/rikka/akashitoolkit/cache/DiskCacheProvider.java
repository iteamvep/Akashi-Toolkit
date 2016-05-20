package rikka.akashitoolkit.cache;

import android.content.Context;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import rikka.akashitoolkit.support.Settings;

/**
 * 直接使用 Glide 的 DiskLruCache
 */
public class DiskCacheProvider {
    public static DiskCache diskCache;

    public static void init(Context context) {
        diskCache = getFactory(context).build();
    }

    public static synchronized DiskCache get() {
        if (diskCache == null) {
            throw new NullPointerException("call init first");
        }

        return diskCache;
    }

    public static DiskLruCacheFactory getFactory(Context context) {
        int size = Settings.instance(context)
                .getIntFromString(Settings.CACHE_MAX_SIZE, 250);

        if (size <= 0) {
            size = 250;
        }

        return new InternalCacheDiskCacheFactory(context, "disk_cache", size * 1024 * 1024);
    }
}
