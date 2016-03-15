package rikka.akashitoolkit.network;

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
    }

    public interface CheckUpdateService {
        @GET("/Akashi/info.php")
        Call<CheckUpdate> get();
    }
}
