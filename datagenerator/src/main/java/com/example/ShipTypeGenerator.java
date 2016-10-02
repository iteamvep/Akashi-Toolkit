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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rikka on 2016/8/10.
 */
public class ShipTypeGenerator {
    private static Map<Integer, String> ENGLISH_NAME;

    static {
        ENGLISH_NAME = new HashMap<>();
        ENGLISH_NAME.put(1, "Escort ship");
        ENGLISH_NAME.put(2, "Destroyer");
        ENGLISH_NAME.put(3, "Light Cruisers");
        ENGLISH_NAME.put(4, "Torpedo Cruiser");
        ENGLISH_NAME.put(5, "Heavy Cruiser");
        ENGLISH_NAME.put(6, "Aviation Cruiser");
        ENGLISH_NAME.put(7, "Light Aircraft Carrier");
        ENGLISH_NAME.put(8, "Battleship (High speed)");
        ENGLISH_NAME.put(9, "Battleship");
        ENGLISH_NAME.put(10, "Aviation Battleship");
        ENGLISH_NAME.put(11, "Standard Aircraft Carrier");
        ENGLISH_NAME.put(12, "Battleship");
        ENGLISH_NAME.put(13, "Submarine");
        ENGLISH_NAME.put(14, "Submarine Aircraft Carrier");
        ENGLISH_NAME.put(15, "Carriers");
        ENGLISH_NAME.put(16, "Seaplane Tender");
        ENGLISH_NAME.put(17, "Amphibious Assault Ship");
        ENGLISH_NAME.put(18, "Armored Aircraft Carrier");
        ENGLISH_NAME.put(19, "Repair Ship");
        ENGLISH_NAME.put(20, "Submarine Tender");
        ENGLISH_NAME.put(21, "Training Cruiser");
        ENGLISH_NAME.put(22, "Fleet Oiler");
    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        List<ShipType> list = new Gson().fromJson(new JsonReader(new FileReader("app/src/main/assets/ShipType.json")), new TypeToken<List<ShipType>>() {
        }.getType());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kcwiki.moe")
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

        for (ShipType shipType : list) {
            shipType.getName().setJa(shipType.getName().getJa().trim());
            shipType.getName().setZh_cn(shipType.getName().getZh_cn().trim());
            shipType.getName().setEn(ENGLISH_NAME.get(shipType.getId()));
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
