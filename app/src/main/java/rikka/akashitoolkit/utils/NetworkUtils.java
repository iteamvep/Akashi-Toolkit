package rikka.akashitoolkit.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Rikka on 2016/8/31.
 */
public class NetworkUtils {

    private static Context sContext;
    private static OkHttpClient sForceCacheClient;
    private static OkHttpClient sClient;

    public static void init(Context context) {
        sContext = context;

        File file = new File(context.getCacheDir(), "api_cache");
        Cache cache = new Cache(file, 10 * 1024 * 1024);

        sForceCacheClient = new OkHttpClient
                .Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request()
                                .newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build());
                    }
                })
                .build();

        sClient = new OkHttpClient
                .Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request()
                                .newBuilder()
                                //.header("Cache-Control", "public, max-age=" + 60)
                                .build());
                    }
                })
                .build();
    }

    /**
     * 返回一个强制使用缓存的 OkHttpClient
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getForceCacheClient() {
        return sForceCacheClient;
    }

    public static OkHttpClient getClient(boolean force_cache) {
        return force_cache ? sForceCacheClient : sClient;
    }

    /**
     * 返回一个普通的 OkHttpClient
     * @return OkHttpClient
     */
    public static OkHttpClient getClient() {
        return sClient;
    }
}
