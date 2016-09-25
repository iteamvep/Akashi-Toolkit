package com.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/9/17.
 */
public class Ship2 {

    @Expose
    private int id;
    @Expose
    private String wiki_id;
    @Expose
    private MultiLanguageEntry name;
    @Expose
    private int stype;
    @Expose
    private int ctype;
    @Expose
    private int cnum;
    @Expose
    private int rarity;
    @Expose
    private String name_for_search;

    @Expose
    private int build_time;
    @Expose
    private int[] broken;
    @Expose
    private int[] power_up;
    @Expose
    private int[] consume;

    @Expose
    private String painter;
    @Expose
    private String cv;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getResourceConsume() {
        return consume;
    }

    public void setResourceConsume(int[] consume) {
        this.consume = consume;
    }

    public int[] getModernizationBonus() {
        return power_up;
    }

    public void setModernizationBonus(int[] power_up) {
        this.power_up = power_up;
    }

    public int[] getBrokenResources() {
        return broken;
    }

    public void setBrokenResources(int[] broken) {
        this.broken = broken;
    }

    public int getBuildTime() {
        return build_time;
    }

    public void setBuildTime(int build_time) {
        this.build_time = build_time;
    }

    public String getNameForSearch() {
        return name_for_search;
    }

    public void setNameForSearch(String nameForSearch) {
        this.name_for_search = nameForSearch;
    }

    public MultiLanguageEntry getName() {
        if (name == null) {
            name = new MultiLanguageEntry();
        }
        return name;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }

    public int getType() {
        return stype;
    }

    public void setType(int type) {
        this.stype = type;
    }

    public int getClassType() {
        return ctype;
    }

    public void setClassType(int classType) {
        this.ctype = classType;
    }

    public int getClassNum() {
        return cnum;
    }

    public void setClassNum(int classNum) {
        this.cnum = classNum;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public String getWikiId() {
        return wiki_id;
    }

    public void setWikiId(String wikiId) {
        this.wiki_id = wikiId;
    }

    @Expose
    private Ship.EquipEntity equip;

    public Ship.EquipEntity getEquip() {
        return equip;
    }

    public void setEquip(Ship.EquipEntity equip) {
        this.equip = equip;
    }

    public static class EquipEntity {
        @Expose
        private int slots;
        @Expose
        private int[] id;
        @Expose
        private int[] space;

        public int getSlots() {
            return slots;
        }

        public int[] getId() {
            return id;
        }

        public int[] getSpace() {
            return space;
        }
    }

    @Expose
    private RemodelEntity remodel;

    public RemodelEntity getRemodel() {
        return remodel;
    }

    public void setRemodel(RemodelEntity remodel) {
        this.remodel = remodel;
    }

    public static class RemodelEntity {
        @Expose
        @SerializedName("blueprint")
        private boolean requireBlueprint;
        @Expose
        private boolean catapult;

        @Expose
        @SerializedName("from_id")
        private int fromId;
        @Expose
        @SerializedName("to_id")
        private int toId;
        @Expose
        private int level;
        @Expose
        private int[] cost;


        public boolean isRequireBlueprint() {
            return requireBlueprint;
        }

        public int getFromId() {
            return fromId;
        }

        public int getToId() {
            return toId;
        }

        public int getLevel() {
            return level;
        }

        public int[] getCost() {
            return cost;
        }

        public void setRequireBlueprint(boolean requireBlueprint) {
            this.requireBlueprint = requireBlueprint;
        }

        public void setCatapult(boolean catapult) {
            this.catapult = catapult;
        }

        public void setFromId(int fromId) {
            this.fromId = fromId;
        }

        public void setToId(int toId) {
            this.toId = toId;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setCost(int[] cost) {
            this.cost = cost;
        }
    }

    @Expose
    private AttrEntity attr;
    @Expose
    private AttrEntity attr_max;
    @Expose
    private AttrEntity attr_99;
    @Expose
    private AttrEntity attr_155;

    public AttrEntity getAttr() {
        return attr;
    }

    public AttrEntity getAttrMax() {
        return attr_max;
    }

    public AttrEntity getAttr99() {
        return attr_99;
    }

    public AttrEntity getAttr155() {
        return attr_155;
    }

    public void setAttr(AttrEntity attr) {
        this.attr = attr;
    }

    public void setAttr_max(AttrEntity attr_max) {
        this.attr_max = attr_max;
    }

    public void setAttr_99(AttrEntity attr_99) {
        this.attr_99 = attr_99;
    }

    public void setAttr_155(AttrEntity attr_155) {
        this.attr_155 = attr_155;
    }

    private String 日文名;
    private String 假名;
    private String 中文名;
    private 数据Entity 数据;
    @Expose
    private GetEntity get;
    private 消耗Entity 消耗;
    private 改修Entity 改修;
    private 解体Entity 解体;
    private List<String> 级别;

    public String get日文名() {
        return 日文名;
    }

    public void set日文名(String 日文名) {
        this.日文名 = 日文名;
    }

    public String get假名() {
        return 假名;
    }

    public void set假名(String 假名) {
        this.假名 = 假名;
    }

    public String get中文名() {
        return 中文名;
    }

    public void set中文名(String 中文名) {
        this.中文名 = 中文名;
    }

    public 数据Entity get数据() {
        return 数据;
    }

    public void set数据(数据Entity 数据) {
        this.数据 = 数据;
    }

    public GetEntity get获得() {
        return get;
    }

    public void set获得(GetEntity 获得) {
        this.get = 获得;
    }

    public 消耗Entity get消耗() {
        return 消耗;
    }

    public void set消耗(消耗Entity 消耗) {
        this.消耗 = 消耗;
    }

    public 改修Entity get改修() {
        return 改修;
    }

    public void set改修(改修Entity 改修) {
        this.改修 = 改修;
    }

    public 解体Entity get解体() {
        return 解体;
    }

    public void set解体(解体Entity 解体) {
        this.解体 = 解体;
    }

    public List<String> get级别() {
        return 级别;
    }

    public void set级别(List<String> 级别) {
        this.级别 = 级别;
    }

    public static class 数据Entity {
        private int 速力;
        private int 射程;
        private int 稀有度;
        private List<Integer> 耐久;
        private List<Integer> 火力;
        private List<Integer> 雷装;
        private List<Integer> 对空;
        private List<Integer> 装甲;
        private List<Integer> 对潜;
        private List<Integer> 回避;
        private List<Integer> 索敌;
        private List<Integer> 运;

        public int get速力() {
            return 速力;
        }

        public void set速力(int 速力) {
            this.速力 = 速力;
        }

        public int get射程() {
            return 射程;
        }

        public void set射程(int 射程) {
            this.射程 = 射程;
        }

        public int get稀有度() {
            return 稀有度;
        }

        public void set稀有度(int 稀有度) {
            this.稀有度 = 稀有度;
        }

        public List<Integer> get耐久() {
            return 耐久;
        }

        public void set耐久(List<Integer> 耐久) {
            this.耐久 = 耐久;
        }

        public List<Integer> get火力() {
            return 火力;
        }

        public void set火力(List<Integer> 火力) {
            this.火力 = 火力;
        }

        public List<Integer> get雷装() {
            return 雷装;
        }

        public void set雷装(List<Integer> 雷装) {
            this.雷装 = 雷装;
        }

        public List<Integer> get对空() {
            return 对空;
        }

        public void set对空(List<Integer> 对空) {
            this.对空 = 对空;
        }

        public List<Integer> get装甲() {
            return 装甲;
        }

        public void set装甲(List<Integer> 装甲) {
            this.装甲 = 装甲;
        }

        public List<Integer> get对潜() {
            return 对潜;
        }

        public void set对潜(List<Integer> 对潜) {
            this.对潜 = 对潜;
        }

        public List<Integer> get回避() {
            return 回避;
        }

        public void set回避(List<Integer> 回避) {
            this.回避 = 回避;
        }

        public List<Integer> get索敌() {
            return 索敌;
        }

        public void set索敌(List<Integer> 索敌) {
            this.索敌 = 索敌;
        }

        public List<Integer> get运() {
            return 运;
        }

        public void set运(List<Integer> 运) {
            this.运 = 运;
        }
    }

    public static class GetEntity {
        @Expose
        private int drop;
        @Expose
        private int remodel;
        @Expose
        private int build;
        @Expose
        private int build_time;

        public int getDrop() {
            return drop;
        }

        public void setDrop(int drop) {
            this.drop = drop;
        }

        public int getRemodel() {
            return remodel;
        }

        public void setRemodel(int remodel) {
            this.remodel = remodel;
        }

        public int getBuild() {
            return build;
        }

        public void setBuild(int build) {
            this.build = build;
        }

        public int getBuildTime() {
            return build_time;
        }

        public void setBuildTime(int build_time) {
            this.build_time = build_time;
        }
    }

    public static class 消耗Entity {
        private int 燃料;
        private int 弹药;

        public int get燃料() {
            return 燃料;
        }

        public void set燃料(int 燃料) {
            this.燃料 = 燃料;
        }

        public int get弹药() {
            return 弹药;
        }

        public void set弹药(int 弹药) {
            this.弹药 = 弹药;
        }
    }

    public static class 改修Entity {
        private int 火力;
        private int 雷装;
        private int 对空;
        private int 装甲;

        public int get火力() {
            return 火力;
        }

        public void set火力(int 火力) {
            this.火力 = 火力;
        }

        public int get雷装() {
            return 雷装;
        }

        public void set雷装(int 雷装) {
            this.雷装 = 雷装;
        }

        public int get对空() {
            return 对空;
        }

        public void set对空(int 对空) {
            this.对空 = 对空;
        }

        public int get装甲() {
            return 装甲;
        }

        public void set装甲(int 装甲) {
            this.装甲 = 装甲;
        }
    }

    public static class 解体Entity {
        private int 燃料;
        private int 弹药;
        private int 钢材;
        private int 铝;

        public int get燃料() {
            return 燃料;
        }

        public void set燃料(int 燃料) {
            this.燃料 = 燃料;
        }

        public int get弹药() {
            return 弹药;
        }

        public void set弹药(int 弹药) {
            this.弹药 = 弹药;
        }

        public int get钢材() {
            return 钢材;
        }

        public void set钢材(int 钢材) {
            this.钢材 = 钢材;
        }

        public int get铝() {
            return 铝;
        }

        public void set铝(int 铝) {
            this.铝 = 铝;
        }
    }

    private int after_fuel;
    private int after_bull;

    public int getAfter_fuel() {
        return after_fuel;
    }

    public int getAfter_bull() {
        return after_bull;
    }

    @Expose
    private List<Integer> extra_equip_type;

    public List<Integer> getExtraEquipType() {
        if (extra_equip_type == null) {
            extra_equip_type = new ArrayList<>();
        }
        return extra_equip_type;
    }

    public void setExtraEquipType(List<Integer> extra_equip_type) {
        this.extra_equip_type = extra_equip_type;
    }

    @Override
    public String toString() {
        return "Ship2{" + 中文名 + " " + id + '}';
    }


}

