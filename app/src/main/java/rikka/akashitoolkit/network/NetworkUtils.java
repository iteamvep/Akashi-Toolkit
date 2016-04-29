package rikka.akashitoolkit.network;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Rikka on 2016/4/28.
 */
public class NetworkUtils {
    private static OkHttpClient sClient;

    static {
        sClient = new OkHttpClient.Builder().build();
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        sClient.newCall(request).enqueue(callback);
    }
}
