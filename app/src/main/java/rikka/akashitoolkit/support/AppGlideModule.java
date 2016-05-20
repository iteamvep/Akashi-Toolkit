package rikka.akashitoolkit.support;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import rikka.akashitoolkit.cache.DiskCacheProvider;

/**
 * Created by Rikka on 2016/5/19.
 */
public class AppGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(DiskCacheProvider.getFactory(context));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
