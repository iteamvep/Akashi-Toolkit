package com.example.model;

import com.example.list.ShipList;
import com.example.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/4/16.
 */
public class NewEquip {
    @Expose
    private int id;

    @SerializedName("中文名")
    private String name_cn;
    @SerializedName("日文名")
    private String name_jp;

    @Expose
    private MultiLanguageEntry name;
    @Expose
    private MultiLanguageEntry introduction;
    @SerializedName("备注")
    @Expose
    private String remark;
    @SerializedName("属性")
    @Expose
    private AttrEntity attr;
    private String 稀有度;
    @Expose
    @SerializedName("类别")
    private int[] types;
    @Expose
    private int rarity;
    private List<Integer> shipLimit;

    public int[] getTypes() {
        return types;
    }

    @SerializedName("废弃")
    @Expose
    private int[] broken;

    public int[] getBrokenResources() {
        return broken;
    }

    public void setBrokenResources(int[] broken) {
        this.broken = broken;
    }

    public String getNameCN() {
        return name_cn;
    }

    public String getNameJP() {
        return name_jp;
    }

    public String get稀有度() {
        return 稀有度;
    }

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

    public MultiLanguageEntry getIntroduction() {
        if (introduction == null) {
            introduction = new MultiLanguageEntry();
        }
        return introduction;
    }

    public void setIntroduction(MultiLanguageEntry introduction) {
        this.introduction = introduction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AttrEntity getAttr() {
        return attr;
    }

    public void setAttr(AttrEntity attr) {
        this.attr = attr;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public List<Integer> getShipLimit() {
        return shipLimit;
    }

    public void setShipLimit(List<Integer> shipLimit) {
        this.shipLimit = shipLimit;
    }


    @SerializedName("装备改修")
    private ImprovementEntity 装备改修;
    @SerializedName("装备改修2")
    private ImprovementEntity 装备改修2;

    public ImprovementEntity get装备改修() {
        return 装备改修;
    }

    public ImprovementEntity get装备改修2() {
        return 装备改修2;
    }

    @Expose
    private ImprovementEntity[] improvements;

    public ImprovementEntity[] getImprovements() {
        return improvements;
    }

    public void setImprovements(ImprovementEntity[] improvements) {
        this.improvements = improvements;
    }

    public static class ImprovementEntity {
        @Expose
        @SerializedName("改修备注")
        private String remark;
        @Expose
        @SerializedName("资源消费")
        private int[] cost;
        @Expose
        @SerializedName("初期消费")
        private int[] item;
        @Expose
        @SerializedName("中段消费")
        private int[] item2;
        @Expose
        @SerializedName("更新消费")
        private int[] item3;
        @SerializedName("日期")
        private String[][] date;
        @Expose
        private List<List<Integer>> ship;
        @Expose
        @SerializedName("更新装备")
        private int[] upgrade;

        public String getRemark() {
            return remark;
        }

        public int[] getCost() {
            return cost;
        }

        public int[] getItem() {
            return item;
        }

        public int[] getItem2() {
            return item2;
        }

        public int[] getItem3() {
            return item3;
        }

        public List<List<Integer>> getShips() {
            return ship;
        }

        public String[][] getDate() {
            return date;
        }

        public void setShips() {
            ship = new ArrayList<>();

            for (String[] array : date) {
                List<Integer> list = new ArrayList<>();
                for (String name : array) {
                    if (name.equals("〇")) {
                        list.add(0);
                        break;
                    } else {
                        Ship ship = ShipList.findByName(name);
                        if (ship == null) {
                            //System.out.println("ImprovementEntity: " + "找不到舰娘 " + name);
                            continue;
                        }
                        list.add(ship.getId());
                    }
                }
                ship.add(list);
            }
        }
    }


    public void set改修数据(NewEquip.ImprovementEntity 改修数据) {
        this.装备改修 = 改修数据;
    }

    public void set改修数据2(NewEquip.ImprovementEntity 改修数据2) {
        this.装备改修2 = 改修数据2;
    }

    public static class AttrEntity {
        @SerializedName("射程")
        @Expose
        private int range;
        private int speed;
        @SerializedName("对空")
        @Expose
        private int aa;
        @SerializedName("装甲")
        @Expose
        private int armor;
        @SerializedName("对潜")
        @Expose
        private int asw;
        @SerializedName("回避")
        @Expose
        private int evasion;
        @SerializedName("火力")
        @Expose
        private int fire;
        private int hp;
        private int luck;
        @SerializedName("索敌")
        @Expose
        private int los;
        @SerializedName("雷装")
        @Expose
        private int torpedo;
        @SerializedName("爆装")
        @Expose
        private int bomb;
        @SerializedName("命中")
        @Expose
        private int accuracy;

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public void setAA(int aa) {
            this.aa = aa;
        }

        public void setArmor(int armor) {
            this.armor = armor;
        }

        public void setASW(int asw) {
            this.asw = asw;
        }

        public void setEvasion(int evasion) {
            this.evasion = evasion;
        }

        public void setFirepower(int fire) {
            this.fire = fire;
        }

        public void setHP(int hp) {
            this.hp = hp;
        }

        public void setLuck(int luck) {
            this.luck = luck;
        }

        public void setLos(int los) {
            this.los = los;
        }

        public void setTorpedo(int torpedo) {
            this.torpedo = torpedo;
        }

        public void setBombing(int bomb) {
            this.bomb = bomb;
        }

        public void setAccuracy(int accuracy) {
            this.accuracy = accuracy;
        }

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = Integer.parseInt(speed);
        }

        public void setAA(String aa) {
            this.aa = Utils.stringToInt(aa);
        }

        public void setArmor(String armor) {
            this.armor = Utils.stringToInt(armor);
        }

        public void setASW(String asw) {
            this.asw = Utils.stringToInt(asw);
        }

        public void setEvasion(String evasion) {
            this.evasion = Utils.stringToInt(evasion);
        }

        public void setFirepower(String fire) {
            this.fire = Utils.stringToInt(fire);
        }

        public void setHP(String hp) {
            this.hp = Utils.stringToInt(hp);
        }

        public void setLuck(String luck) {
            this.luck = Utils.stringToInt(luck);
        }

        public void setLOS(String los) {
            this.los = Utils.stringToInt(los);
        }

        public void setTorpedo(String torpedo) {
            this.torpedo = Utils.stringToInt(torpedo);
        }

        public void setBombing(String bomb) {
            this.torpedo = Utils.stringToInt(bomb);
        }

        public void setAccuarcy(String acc) {
            this.torpedo = Utils.stringToInt(acc);
        }
    }
}
