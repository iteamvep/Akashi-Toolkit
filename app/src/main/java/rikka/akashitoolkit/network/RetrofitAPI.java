package rikka.akashitoolkit.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.model.Twitter;

/**
 * Created by Rikka on 2016/3/8.
 */
public class RetrofitAPI {
    public interface TwitterService {
        @GET("/")
        Call<Twitter> get(
                @Query("json") int json,
                @Query("count") int count);

        @GET("/image_url.txt")
        Call<ResponseBody> getAvatarUrl();
    }

    public interface CheckUpdateService {
        @GET("/Akashi/info.php")
        Call<CheckUpdate> get(
                @Query("api_version") int api_version,
                @Query("channel") int channel);
    }
}
