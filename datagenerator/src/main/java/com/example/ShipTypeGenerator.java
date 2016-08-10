package com.example;

import com.example.model.APIShipType;
import com.example.model.ShipType;
import com.example.network.RetrofitAPI;
import com.example.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rikka on 2016/8/10.
 */
public class ShipTypeGenerator {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        List<ShipType> list = new Gson().fromJson(new JsonReader(new FileReader("app/src/main/assets/ShipType.json")), new TypeToken<List<ShipType>>() {
        }.getType());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.kcwiki.moe")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.ShipService service = retrofit.create(RetrofitAPI.ShipService.class);
        List<APIShipType> apiList = service.getTypes().execute().body();

        for (ShipType shipType : list) {
            for (APIShipType apiShipType : apiList) {
                if (shipType.getId() == apiShipType.getId()) {
                    parse(shipType, apiShipType);
                }
            }
        }

        Utils.objectToJsonFile(list, "app/src/main/assets/ShipType.json");
    }

    private static void parse(ShipType shipType, APIShipType apiShipType) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 94; i++) {
            Field field = apiShipType.getEquip_type().getClass().getDeclaredField("value" + Integer.toString(i));
            field.setAccessible(true);
            int value = (int) field.get(apiShipType.getEquip_type());
            sb.append(value);
        }
        shipType.setEquipType(sb.toString());
    }
}
