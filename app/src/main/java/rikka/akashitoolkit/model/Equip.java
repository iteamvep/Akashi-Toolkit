package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/3/26.
 */
public class Equip extends BaseDataModel {
    private MultiLanguageEntry name;
    private MultiLanguageEntry introduction;
    private String remark;
    private AttrEntity attr;
    private int rarity;
    private int[] type;
    private int parent_type;
    private List<Integer> shipLimit;
    private List<Integer> shipFrom;

    private int[] broken;

    public int getParentType() {
        return parent_type;
    }

    public void setParentType(int parent_type) {
        this.parent_type = parent_type;
    }

    public int getIcon() {
        return type[3];
    }

    public int getType() {
        return type[3];
    }

    public int[] getBrokenResources() {
        return broken;
    }

    public void setBrokenResources(int[] broken) {
        this.broken = broken;
    }

    public MultiLanguageEntry getName() {
        return name;
    }

    public List<Integer> getShipFrom() {
        return shipFrom;
    }

    public void setShipFrom(List<Integer> shipFrom) {
        this.shipFrom = shipFrom;
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

    public int[] getTypes() {
        return type;
    }

    public void setType(int[] type) {
        this.type = type;
    }

    public List<Integer> getShipLimit() {
        return shipLimit;
    }

    public void setShipLimit(List<Integer> shipLimit) {
        this.shipLimit = shipLimit;
    }

    private ImprovementEntity[] improvements;

    public ImprovementEntity[] getImprovements() {
        return improvements;
    }

    public void setImprovements(ImprovementEntity[] improvements) {
        this.improvements = improvements;
    }

    public static class ImprovementEntity {
        private String remark;
        private int[] cost;
        private int[] item;
        private int[] item2;
        private int[] item3;
        private List<List<Integer>> ship;
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

        public int[] getUpgrade() {
            return upgrade;
        }
    }
}
