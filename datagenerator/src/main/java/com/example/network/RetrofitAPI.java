package com.example.network;

import com.example.model.NewShip;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rikka on 2016/6/28.
 */
public class RetrofitAPI {
    public interface ShipService {
        @GET("/ships/detail")
        Call<List<NewShip>> getDetail();

        @GET("/ship/detail/{id}")
        Call<NewShip> getDetail(@Path("id") int id);
    }

    public interface KcwikiService {
        @GET("/index.php")
        Call<ResponseBody> getPage(@Query("title") String title, @Query("action") String action);
    }
}
