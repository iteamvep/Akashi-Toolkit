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

    private NameEntity name;
    /**
     * name : {"zh_cn":"驱逐舰","ja":"駆逐艦"}
     * short : DD
     */

    @SerializedName("short")
    private String shortX;

    public NameEntity getName() {
        return name;
    }

    public void setName(NameEntity name) {
        this.name = name;
    }

    public String getShortX() {
        return shortX;
    }

    public void setShortX(String shortX) {
        this.shortX = shortX;
    }

    public static class NameEntity {
        private String zh_cn;
        private String ja;

        public String getZh_cn() {
            return zh_cn;
        }

        public void setZh_cn(String zh_cn) {
            this.zh_cn = zh_cn;
        }

        public String getJa() {
            return ja;
        }

        public void setJa(String ja) {
            this.ja = ja;
        }
    }
}
