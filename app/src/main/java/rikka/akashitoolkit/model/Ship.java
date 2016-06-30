package rikka.akashitoolkit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rikka on 2016/6/28.
 */
public class Ship extends BaseDataModel {
    private String wiki_id;
    private MultiLanguageEntry name;
    private int stype;
    private int ctype;
    private int cnum;
    private int rarity;
    private String name_for_search;

    private int build_time;
    private int[] broken;
    private int[] power_up;
    private int[] consume;

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

    private EquipEntity equip;

    public EquipEntity getEquip() {
        return equip;
    }

    public void setEquip(EquipEntity equip) {
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
    }

    @Expose
    private AttrEntity attr;

    public AttrEntity getAttr() {
        return attr;
    }

    public void setAttr(AttrEntity attr) {
        this.attr = attr;
    }

    public static class AttrEntity {
        private int range;
        private int speed;
        private String[] aa;
        private String[] armor;
        private String[] asw;
        private String[] evasion;
        private String[] fire;
        private String[] hp;
        private String[] luck;
        private String[] los;
        private String[] torpedo;

        public int getRange() {
            return range;
        }

        public int getSpeed() {
            return speed;
        }

        public String[] getAA() {
            return aa;
        }

        public String[] getArmor() {
            return armor;
        }

        public String[] getASW() {
            return asw;
        }

        public String[] getEvasion() {
            return evasion;
        }

        public String[] getFirepower() {
            return fire;
        }

        public String[] getHP() {
            return hp;
        }

        public String[] getLuck() {
            return luck;
        }

        public String[] getLOS() {
            return los;
        }

        public String[] getTorpedo() {
            return torpedo;
        }
    }
}
