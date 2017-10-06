package com.example.model;

import com.example.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/4/16.
 */
public class Equip {
    private int id;

    @SerializedName("中文名")
    private String name_cn;
    @SerializedName("日文名")
    private String name_jp;

    private MultiLanguageEntry name;
    private MultiLanguageEntry introduction;
    private String remark;

    private AttrEntity attr;
    private int rarity;
    private int type;
    private int subType;
    private int icon;
    private ImprovementEntity improvement;
    private List<Integer> discard;
    private List<Integer> shipLimit;

    private GetEntity get;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ImprovementEntity getImprovement() {
        return improvement;
    }

    public void setImprovement(ImprovementEntity improvement) {
        this.improvement = improvement;
    }

    public List<Integer> getDiscard() {
        return discard;
    }

    public void setDiscard(List<Integer> discard) {
        this.discard = discard;
    }

    public List<Integer> getShipLimit() {
        return shipLimit;
    }

    public void setShipLimit(List<Integer> shipLimit) {
        this.shipLimit = shipLimit;
    }

    public GetEntity getGet() {
        return get;
    }

    public void setGet(GetEntity get) {
        this.get = get;
    }

    public static class AttrEntity {
        @Expose
        private int range;
        @Expose
        private int speed;
        @Expose
        private int aa;
        @Expose
        private int armor;
        @Expose
        private int asw;
        @Expose
        private int evasion;
        @Expose
        private int fire;
        @Expose
        private int hp;
        @Expose
        private int luck;
        @Expose
        private int los;
        @Expose
        private int torpedo;
        @Expose
        private int bomb;
        @Expose
        private int accuracy;

        public void setRange(int range) {
            this.range = range;
        }

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

        public void setRange(String range) {
            this.range = Integer.parseInt(range);
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

    public static class ImprovementEntity {
        /**
         * base : [10,10,10,10]
         * item : [[7,9,5,7,1,2],[7,9,5,7,1,2]]
         * levelup : 1
         */

        private ResourceEntity resource;
        /**
         * name : 夕立改二
         * day : [false,true,true,true,false,false,false]
         */

        private List<SecretaryEntity> secretary;

        private int levelup;

        public ResourceEntity getResource() {
            return resource;
        }

        public void setResource(ResourceEntity resource) {
            this.resource = resource;
        }

        public List<SecretaryEntity> getSecretary() {
            return secretary;
        }

        public void setSecretary(List<SecretaryEntity> secretary) {
            this.secretary = secretary;
        }

        public int getLevelup() {
            return levelup;
        }

        public void setLevelup(int levelup) {
            this.levelup = levelup;
        }

        public static class ResourceEntity {
            private List<Integer> base;
            private List<List<Integer>> item;

            public ResourceEntity() {
                base = new ArrayList<>();
                item = new ArrayList<>();
            }

            public List<Integer> getBase() {
                return base;
            }

            public void setBase(List<Integer> base) {
                this.base = base;
            }

            public List<List<Integer>> getItem() {
                return item;
            }

            public void setItem(List<List<Integer>> item) {
                this.item = item;
            }
        }

        public static class SecretaryEntity {
            private String name;
            private List<Boolean> day;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Boolean> getDay() {
                return day;
            }

            public void setDay(List<Boolean> day) {
                this.day = day;
            }
        }
    }

    public static class GetEntity {
        private String rank;
        private String quest;
        private String event;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getQuest() {
            return quest;
        }

        public void setQuest(String quest) {
            this.quest = quest;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }
    }
}
