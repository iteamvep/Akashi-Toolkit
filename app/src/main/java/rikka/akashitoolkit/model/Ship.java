package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/3/29.
 */
public class Ship {

    /**
     * id : 1
     * id_illustrations : 31
     * name : {"ja":"睦月","zh_cn":"睦月"}
     * rarity : 3
     * type : 2
     * attr : {"aa":[7,29],"armor":[5,18],"asw":[16,39,52],"evasion":[4,17,24],"fire":[6,29],"hp":[13,24,17],"luck":[12,49],"range":1,"search":[4,17,24],"speed":10,"torpedo":[18,59]}
     * slot : 2
     * equip : [[1,37,-1,-1],[0,0,0,0]]
     * modernization : [1,1,0,0]
     * dismantling_res : [1,1,4,0]
     * build_time : 18
     * remodel : {"blueprint":0,"cost":[15,15],"id_from":0,"id_to":254,"level":20}
     */

    private int id;
    private int id_illustrations;
    /**
     * ja : 睦月
     * zh_cn : 睦月
     */

    private MultiLanguageEntry name;
    private String name_for_search;
    private int rarity;
    private int type;
    /**
     * aa : [7,29]
     * armor : [5,18]
     * asw : [16,39,52]
     * evasion : [4,17,24]
     * fire : [6,29]
     * hp : [13,24,17]
     * luck : [12,49]
     * range : 1
     * search : [4,17,24]
     * speed : 10
     * torpedo : [18,59]
     */

    private AttrEntity attr;
    private int slot;
    private int build_time;
    /**
     * blueprint : 0
     * cost : [15,15]
     * id_from : 0
     * id_to : 254
     * level : 20
     */

    private RemodelEntity remodel;
    private List<List<Integer>> equip;
    private List<Integer> modernization;
    private List<Integer> dismantling_res;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_illustrations() {
        return id_illustrations;
    }

    public void setId_illustrations(int id_illustrations) {
        this.id_illustrations = id_illustrations;
    }

    public MultiLanguageEntry getName() {
        return name;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }

    public String getName_for_search() {
        return name_for_search;
    }

    public void setName_for_search(String name_for_search) {
        this.name_for_search = name_for_search;
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

    public AttrEntity getAttr() {
        return attr;
    }

    public void setAttr(AttrEntity attr) {
        this.attr = attr;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getBuild_time() {
        return build_time;
    }

    public void setBuild_time(int build_time) {
        this.build_time = build_time;
    }

    public RemodelEntity getRemodel() {
        return remodel;
    }

    public void setRemodel(RemodelEntity remodel) {
        this.remodel = remodel;
    }

    public List<List<Integer>> getEquip() {
        return equip;
    }

    public void setEquip(List<List<Integer>> equip) {
        this.equip = equip;
    }

    public List<Integer> getModernization() {
        return modernization;
    }

    public void setModernization(List<Integer> modernization) {
        this.modernization = modernization;
    }

    public List<Integer> getDismantling_res() {
        return dismantling_res;
    }

    public void setDismantling_res(List<Integer> dismantling_res) {
        this.dismantling_res = dismantling_res;
    }

    public static class AttrEntity {
        private int range;
        private int speed;
        private List<Integer> aa;
        private List<Integer> armor;
        private List<Integer> asw;
        private List<Integer> evasion;
        private List<Integer> fire;
        private List<Integer> hp;
        private List<Integer> luck;
        private List<Integer> search;
        private List<Integer> torpedo;

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public List<Integer> getAa() {
            return aa;
        }

        public void setAa(List<Integer> aa) {
            this.aa = aa;
        }

        public List<Integer> getArmor() {
            return armor;
        }

        public void setArmor(List<Integer> armor) {
            this.armor = armor;
        }

        public List<Integer> getAsw() {
            return asw;
        }

        public void setAsw(List<Integer> asw) {
            this.asw = asw;
        }

        public List<Integer> getEvasion() {
            return evasion;
        }

        public void setEvasion(List<Integer> evasion) {
            this.evasion = evasion;
        }

        public List<Integer> getFire() {
            return fire;
        }

        public void setFire(List<Integer> fire) {
            this.fire = fire;
        }

        public List<Integer> getHp() {
            return hp;
        }

        public void setHp(List<Integer> hp) {
            this.hp = hp;
        }

        public List<Integer> getLuck() {
            return luck;
        }

        public void setLuck(List<Integer> luck) {
            this.luck = luck;
        }

        public List<Integer> getSearch() {
            return search;
        }

        public void setSearch(List<Integer> search) {
            this.search = search;
        }

        public List<Integer> getTorpedo() {
            return torpedo;
        }

        public void setTorpedo(List<Integer> torpedo) {
            this.torpedo = torpedo;
        }
    }

    public static class RemodelEntity {
        private int blueprint;
        private int id_from;
        private int id_to;
        private int level;
        private List<Integer> cost;

        public int getBlueprint() {
            return blueprint;
        }

        public void setBlueprint(int blueprint) {
            this.blueprint = blueprint;
        }

        public int getId_from() {
            return id_from;
        }

        public void setId_from(int id_from) {
            this.id_from = id_from;
        }

        public int getId_to() {
            return id_to;
        }

        public void setId_to(int id_to) {
            this.id_to = id_to;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public List<Integer> getCost() {
            return cost;
        }

        public void setCost(List<Integer> cost) {
            this.cost = cost;
        }
    }
}
