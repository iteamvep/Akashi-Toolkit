package com.example;

import com.example.model.APIEquipType;
import com.example.model.EquipType;
import com.example.model.EquipTypeParent;
import com.example.model.MultiLanguageEntry;
import com.example.network.RetrofitAPI;
import com.example.utils.Utils;
import com.spreada.utils.chinese.ZHConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipTypeGenerator {

    private static Map<Integer, String> ENGLISH_NAME;
    private static Map<Integer, Integer> PARENT;

    private static final int TYPE_GUN = 1;
    private static final int TYPE_TORP = 2;
    private static final int TYPE_AIRCRAFT = 3;
    private static final int TYPE_RADAR = 4;
    private static final int TYPE_AA_GUN = 5;
    private static final int TYPE_SONAR = 6;
    private static final int TYPE_OTHER = 7;

    static {
        PARENT = new HashMap<>();
        PARENT.put(1, TYPE_GUN);
        PARENT.put(2, TYPE_GUN);
        PARENT.put(3, TYPE_GUN);
        PARENT.put(4, TYPE_GUN);
        PARENT.put(5, TYPE_TORP);
        PARENT.put(6, TYPE_AIRCRAFT);
        PARENT.put(7, TYPE_AIRCRAFT);
        PARENT.put(8, TYPE_AIRCRAFT);
        PARENT.put(9, TYPE_AIRCRAFT);
        PARENT.put(10, TYPE_AIRCRAFT);
        PARENT.put(11, TYPE_AIRCRAFT);
        PARENT.put(12, TYPE_RADAR);
        PARENT.put(13, TYPE_RADAR);
        PARENT.put(14, TYPE_SONAR);
        PARENT.put(15, TYPE_SONAR);
        PARENT.put(16, TYPE_OTHER);
        PARENT.put(17, TYPE_OTHER);
        PARENT.put(18, TYPE_GUN);
        PARENT.put(19, TYPE_GUN);
        PARENT.put(20, TYPE_OTHER);
        PARENT.put(21, TYPE_AA_GUN);
        PARENT.put(22, TYPE_TORP);
        PARENT.put(23, TYPE_OTHER);
        PARENT.put(24, TYPE_OTHER);
        PARENT.put(25, TYPE_AIRCRAFT);
        PARENT.put(26, TYPE_AIRCRAFT);
        PARENT.put(27, TYPE_OTHER);
        PARENT.put(28, TYPE_OTHER);
        PARENT.put(29, TYPE_OTHER);
        PARENT.put(30, TYPE_OTHER);
        PARENT.put(31, TYPE_OTHER);
        PARENT.put(32, TYPE_TORP);
        PARENT.put(33, TYPE_OTHER);
        PARENT.put(34, TYPE_OTHER);
        PARENT.put(35, TYPE_OTHER);
        PARENT.put(36, TYPE_AA_GUN);
        PARENT.put(37, TYPE_OTHER);
        PARENT.put(38, TYPE_GUN);
        PARENT.put(39, TYPE_OTHER);
        PARENT.put(40, TYPE_SONAR);
        PARENT.put(41, TYPE_AIRCRAFT);
        PARENT.put(42, TYPE_OTHER);
        PARENT.put(43, TYPE_OTHER);
        PARENT.put(44, TYPE_OTHER);
        PARENT.put(45, TYPE_AIRCRAFT);
        PARENT.put(46, TYPE_OTHER);
        PARENT.put(47, TYPE_AIRCRAFT);
        PARENT.put(48, TYPE_AIRCRAFT);
        PARENT.put(57, TYPE_AIRCRAFT);
        PARENT.put(93, TYPE_RADAR);
        PARENT.put(94, TYPE_AIRCRAFT);

        ENGLISH_NAME = new HashMap<>();
        ENGLISH_NAME.put(1, "Small Caliber Main Gun");
        ENGLISH_NAME.put(2, "Medium Caliber Main Gun");
        ENGLISH_NAME.put(3, "Large Caliber Main Gun");
        ENGLISH_NAME.put(4, "Secondary Gun");
        ENGLISH_NAME.put(5, "Torpedo");
        ENGLISH_NAME.put(6, "Carrier-based Fighter");
        ENGLISH_NAME.put(7, "Carrier-based Dive Bomber");
        ENGLISH_NAME.put(8, "Carrier-based Torpedo Bomber");
        ENGLISH_NAME.put(9, "Carrier-based Reconnaissance Aircraft");
        ENGLISH_NAME.put(10, "Reconnaissance Seaplane");
        ENGLISH_NAME.put(11, "Seaplane Bomber");
        ENGLISH_NAME.put(12, "Small Radar");
        ENGLISH_NAME.put(13, "Large Radar");
        ENGLISH_NAME.put(14, "Sonar");
        ENGLISH_NAME.put(15, "Depth Charge");
        ENGLISH_NAME.put(16, "Extra Armor");
        ENGLISH_NAME.put(17, "Engine Improvement");
        ENGLISH_NAME.put(18, "Anti-Aircraft Shell");
        ENGLISH_NAME.put(19, "Armor Piercing Shell");
        ENGLISH_NAME.put(20, "Proximity Fuze");
        ENGLISH_NAME.put(21, "Anti-Aircraft Gun");
        ENGLISH_NAME.put(22, "Midget Submarine");
        ENGLISH_NAME.put(23, "Damage Control Personnel");
        ENGLISH_NAME.put(24, "Landing Craft");
        ENGLISH_NAME.put(25, "Autogyro");
        ENGLISH_NAME.put(26, "Anti-submarine Patrol Aircraft");
        ENGLISH_NAME.put(27, "Extra Armor (Medium)");
        ENGLISH_NAME.put(28, "Extra Armor (Large)");
        ENGLISH_NAME.put(29, "Searchlight");
        ENGLISH_NAME.put(30, "Supply Transport Container");
        ENGLISH_NAME.put(31, "Ship Repair Facility");
        ENGLISH_NAME.put(32, "Submarine Torpedo");
        ENGLISH_NAME.put(33, "Star Shell");
        ENGLISH_NAME.put(34, "Command Facility");
        ENGLISH_NAME.put(35, "Aviation Personne");
        ENGLISH_NAME.put(36, "Anti-Aircraft Fire Director");
        ENGLISH_NAME.put(37, "Anti-Ground Equipment");
        ENGLISH_NAME.put(38, "Large Caliber Main Gun (II)");
        ENGLISH_NAME.put(39, "Surface Ship Personnel");
        ENGLISH_NAME.put(40, "Large Sonar");
        ENGLISH_NAME.put(41, "Large Flying Boat");
        ENGLISH_NAME.put(42, "Large Searchlight");
        ENGLISH_NAME.put(43, "Combat Ration");
        ENGLISH_NAME.put(44, "Supplies");
        ENGLISH_NAME.put(45, "Seaplane Fighter");
        ENGLISH_NAME.put(46, "Special Amphibious Tank");
        ENGLISH_NAME.put(47, "Land-based Attack Aircraft");
        ENGLISH_NAME.put(48, "Interceptor Fighter");
        ENGLISH_NAME.put(57, "Jet-powered Fighter-Bomber");
        ENGLISH_NAME.put(93, "Large Radar (II)");
        ENGLISH_NAME.put(94, "Carrier-based Reconnaissance Aircraft (II)");
    }

    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kcwiki.moe")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SlotItemService service = retrofit.create(RetrofitAPI.SlotItemService.class);
        List<APIEquipType> apiList = service.getTypes().execute().body();

        apiList.add(new APIEquipType(1, "噴式戦闘爆撃機", 57, "喷气式战斗轰炸机"));

        List<EquipType> list = new ArrayList<>();
        List<EquipTypeParent> parent = new ArrayList<>();

        parent.add(new EquipTypeParent(1, new MultiLanguageEntry("火炮 / 强化弹", "火砲 / 強化弾", "Main Gun / Shell")));
        parent.add(new EquipTypeParent(2, new MultiLanguageEntry("鱼雷", "魚雷", "Torpedo")));
        parent.add(new EquipTypeParent(3, new MultiLanguageEntry("舰载机 / 陆基战机", "舰载机 / 陆基战机", "Aircraft")));
        parent.add(new EquipTypeParent(4, new MultiLanguageEntry("电探", "電探", "Radar")));
        parent.add(new EquipTypeParent(5, new MultiLanguageEntry("对空机枪 / 高射装置", "対空機銃 / 高射装置", "Anti-Aircraft Gun / Cannon")));
        parent.add(new EquipTypeParent(6, new MultiLanguageEntry("爆雷 / 声纳", "爆雷 / ソナー", "Depth charge / Sonar")));
        parent.add(new EquipTypeParent(7, new MultiLanguageEntry("其他", "他の", "Other")));

        for (EquipTypeParent item : parent) {
            item.getName().setZh_tw(ZHConverter.toTC(item.getName().getZh_cn()));
        }

        for (APIEquipType item : apiList) {
            EquipType equipType = new EquipType();
            equipType.setId(item.getId());
            equipType.setName(new MultiLanguageEntry());
            equipType.getName().setJa(item.getName());
            equipType.getName().setZh_cn(item.getChineseName());
            equipType.getName().setZh_tw(ZHConverter.toTC(item.getChineseName()));
            equipType.getName().setEn(ENGLISH_NAME.get(item.getId()));
            equipType.setParent(PARENT.get(item.getId()));
            list.add(equipType);
        }

        for (EquipType item : list) {
            parent.get(item.getParent() - 1).getChild().add(item.getId());
        }

        Utils.objectToJsonFile(list, "app/src/main/assets/EquipType.json");
        Utils.objectToJsonFile(parent, "app/src/main/assets/EquipTypeParent.json");
    }
}
