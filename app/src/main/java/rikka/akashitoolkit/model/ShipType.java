package rikka.akashitoolkit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rikka on 2016/3/27.
 */
public class ShipType {

    /**
     * zh_cn : 驱逐舰
     * ja : 駆逐艦
     */
    private int id;

    private MultiLanguageEntry name;
    /**
     * name : {"zh_cn":"驱逐舰","ja":"駆逐艦"}
     * short : DD
     */

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

    public boolean canEquip(Equip equip) {
        return canEquip(equip.getTypes()[2]);
    }

    public boolean canEquip(int equip_type) {
        return this.equip_type.substring(equip_type - 1, equip_type).equals("1");
    }

    public String getEquipType() {
        return equip_type;
    }
}
