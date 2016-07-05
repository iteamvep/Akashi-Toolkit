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
            return item2;
        }

        public List<List<Integer>> getShips() {
            return ship;
        }

        public int[] getUpgrade() {
            return upgrade;
        }
    }

    public static class AttrEntity {
        private int range;
        private int speed;
        private int aa;
        private int armor;
        private int asw;
        private int evasion;
        private int fire;
        private int hp;
        private int luck;
        private int los;
        private int torpedo;
        private int bomb;
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

        public int getAA() {
            return aa;
        }

        public int getArmor() {
            return armor;
        }

        public int getASW() {
            return asw;
        }

        public int getEvasion() {
            return evasion;
        }

        public int getFirepower() {
            return fire;
        }

        public int getHP() {
            return hp;
        }

        public int getLuck() {
            return luck;
        }

        public int getLOS() {
            return los;
        }

        public int getTorpedo() {
            return torpedo;
        }

        public int getBombing() {
            return bomb;
        }

        public int getAccuracy() {
            return accuracy;
        }
    }
}
