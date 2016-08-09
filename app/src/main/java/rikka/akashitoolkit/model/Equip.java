package rikka.akashitoolkit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public boolean isAircraft() {
        return type[3] == 6 || type[3] == 7 || type[3] == 8 || type[3] == 9 || type[3] == 10
                || type[3] == 21 || type[3] == 22 || type[3] == 33 || type[3] == 37 || type[3] == 38;
    }

    /**
     * @return 是否为舰载机
     */
    public boolean isCarrierBasedAircraft() {
        return type[3] == 6 || type[3] == 7 || type[3] == 8;
    }

    /**
     * @return 是否为水上机
     */
    public boolean isSeaplane() {
        //
        return type[3] == 6 || type[2] == 11 || type[3] == 45;
    }

    private StatusEntity status;

    public static class StatusEntity {
        private int research;
        private int improvement;
        private int upgrade;
        private int rank;

        public int getResearch() {
            return research;
        }

        public int getImprovement() {
            return improvement;
        }

        public int getUpgrade() {
            return upgrade;
        }

        public int getRank() {
            return rank;
        }
    }

    /**
     * @return 是否可以开发
     */
    public boolean isResarchable() {
        return status.research == 1;
    }

    /**
     * @return 是否可以改修
     */
    public boolean isImprovable() {
        return status.improvement == 1;
    }

    /**
     * @return 是否可以更新
     */
    public boolean isUpgradeable() {
        return status.upgrade == 1;
    }

    /**
     * @return 是否有熟练度
     */
    public boolean isRankupable() {
        return status.rank == 1;
    }
}
