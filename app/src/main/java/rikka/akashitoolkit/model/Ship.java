package rikka.akashitoolkit.model;

import java.util.List;

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

    private ShipType ship_type;

    public ShipType getShipType() {
        return ship_type;
    }

    public void setShipType(ShipType ship_type) {
        this.ship_type = ship_type;
    }

    private List<Integer> extra_equip_type;

    public List<Integer> getExtraEquipType() {
        return extra_equip_type;
    }

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
        private int slots;
        private int[] id;
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

    private RemodelEntity remodel;

    public RemodelEntity getRemodel() {
        return remodel;
    }

    public void setRemodel(RemodelEntity remodel) {
        this.remodel = remodel;
    }

    public static class RemodelEntity {
        private boolean blueprint;
        private boolean catapult;
        private int from_id;
        private int to_id;
        private int level;
        private int[] cost;

        /**
         * @return 是否需要“试制甲板用弹射器”
         */
        public boolean requireCatapult() {
            return catapult;
        }

        /**
         *
         * @return 是否需要“改装设计图”
         */
        public boolean requireBlueprint() {
            return blueprint;
        }

        public int getFromId() {
            return from_id;
        }

        public int getToId() {
            return to_id;
        }

        public int getLevel() {
            return level;
        }

        public int[] getCost() {
            return cost;
        }
    }

    private AttributeEntity attr;
    private AttributeEntity attr_max;
    private AttributeEntity attr_99;
    private AttributeEntity attr_155;

    public AttributeEntity getAttribute() {
        return attr;
    }

    public AttributeEntity getAttributeMax() {
        return attr_max;
    }

    public AttributeEntity getAttribute99() {
        return attr_99;
    }

    public AttributeEntity getAttr155() {
        return attr_155;
    }

    public boolean isEnemy() {
        return (getId() >= 1500 &&  getId() < 1800);
        //return getId() >= 500;
    }

    private String painter;
    private String cv;

    public String getCV() {
        return cv;
    }

    public String getPainter() {
        return painter;
    }

    private GetEntity get;

    public GetEntity getGet() {
        return get;
    }

    public static class GetEntity {
        private int drop;
        private int remodel;
        private int build;
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

        /**
         * 得到建造时间 (分钟)
         *
         * @return 建造时间 (分钟)
         */
        public int getBuildTime() {
            return build_time;
        }

        public void setBuildTime(int build_time) {
            this.build_time = build_time;
        }
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + getId() + '\'' +
                ", name=" + name.getZhCN() +
                '}';
    }
}
