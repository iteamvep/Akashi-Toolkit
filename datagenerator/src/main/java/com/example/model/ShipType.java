package com.example.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rikka on 2016/8/10.
 */
public class ShipType {
    private int id;

    private MultiLanguageEntry name;

    @SerializedName("short")
    private String shortX;

    private String equip_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MultiLanguageEntry getName() {
        return name;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }

    public String getShortX() {
        return shortX;
    }

    public void setShortX(String shortX) {
        this.shortX = shortX;
    }

    public String getEquipType() {
        return equip_type;
    }

    public void setEquipType(String equip_type) {
        this.equip_type = equip_type;
    }
}
