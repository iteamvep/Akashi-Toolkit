package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.model.Version;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.utils.NetworkUtils;

/**
 * Created by Rikka on 2016/9/4.
 */
public class Subtitle {

    private static boolean use_cache;

    public Subtitle() {
        use_cache = true;
    }

    public static void init(final Context context) {
        final String version = Settings.instance(context).getString(Settings.SUBTITLE_VERSION, "-1");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kcwiki.moe")
                .client(NetworkUtils.getCacheClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SubtitleAPI service = retrofit.create(RetrofitAPI.SubtitleAPI.class);
        Call<Version> call = service.getVersion();

        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if (response.body() != null) {
                    use_cache = response.body().getVersion().equals(version);
                    Settings.instance(context).putString(Settings.SUBTITLE_VERSION, response.body().getVersion());
                    Log.d("Subtitle", "use cache: " + use_cache + " local: " + version + " online: " + response.body().getVersion());
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {
            }
        });
    }


    /**
     * @return 是否应该使用缓存
     */
    public static boolean shouldUseCache() {
        return use_cache;
    }
}
