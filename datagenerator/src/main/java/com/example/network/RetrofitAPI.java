package com.example.network;

import com.example.model.APIEquipType;
import com.example.model.APIShipType;
import com.example.model.NewShip;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

        /**
         * 舰娘类型数据接口
         * https://github.com/kcwikizh/kcwiki-api/wiki/Ship#type
         *
         * @return 列表
         */
        @GET("/ships/type")
        Call<List<APIShipType>> getTypes();
    }

    public interface SlotItemService {
        /**
         * 返回装备类型信息（基于api_mst_slotitem_equiptype）
         * http://api.kcwiki.moe/slotitems/type
         *
         * @return 列表
         */
        @GET("/slotitems/type")
        Call<List<APIEquipType>> getTypes();
    }


    public interface KcwikiService {
        @Headers({
                "Host: zh.kcwiki.moe",
                "Referer: https://zh.kcwiki.moe/wiki/",
                "User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"
        })
        @GET("/index.php")
        Call<ResponseBody> getPage(@Query("title") String title, @Query("action") String action);
    }

    public interface KancollewikiService {
        @GET("/index.php")
        Call<ResponseBody> getPage(@Query("title") String title, @Query("action") String action);
    }
}
