package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/3/23.
 */
public class Item {

    /**
     * id : 1
     * stars : 1
     * rarity : 1
     * subType : 101
     * icon : 56
     * attr : {"range":1,"fire":1,"aa":1,"acc":0,"as":0,"dodge":0,"torpedo":0,"armor":0,"Search":0,"bomb":0}
     * get : {"rank":"2014.6.1.5,2014.7.1.500,2014.8.1.500,2014.9.21.500,2015.5.21.100,2016.2.21.100","quest":"1040.1,2027.1","event":"2015.春.E1（丙）.1"}
     * remark : {"zh":"装备后可进行开幕雷击","ja":"装備時開幕雷撃"}
     */

    private int id;
    private int stars;
    private int rarity;
    private int subType;
    private int icon;
    private String name;
    /**
     * range : 1
     * fire : 1
     * aa : 1
     * acc : 0
     * as : 0
     * dodge : 0
     * torpedo : 0
     * armor : 0
     * Search : 0
     * bomb : 0
     */

    private AttrEntity attr;
    /**
     * rank : 2014.6.1.5,2014.7.1.500,2014.8.1.500,2014.9.21.500,2015.5.21.100,2016.2.21.100
     * quest : 1040.1,2027.1
     * event : 2015.春.E1（丙）.1
     */

    private GetEntity get;
    /**
     * zh : 装备后可进行开幕雷击
     * ja : 装備時開幕雷撃
     */

    private RemarkEntity remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttrEntity getAttr() {
        return attr;
    }

    public void setAttr(AttrEntity attr) {
        this.attr = attr;
    }

    public GetEntity getGet() {
        return get;
    }

    public void setGet(GetEntity get) {
        this.get = get;
    }

    public RemarkEntity getRemark() {
        return remark;
    }

    public void setRemark(RemarkEntity remark) {
        this.remark = remark;
    }

    public static class AttrEntity {
        private int range;
        private int fire;
        private int aa;
        private int acc;
        private int as;
        private int dodge;
        private int torpedo;
        private int armor;
        private int search;
        private int bomb;

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getFire() {
            return fire;
        }

        public void setFire(int fire) {
            this.fire = fire;
        }

        public int getAa() {
            return aa;
        }

        public void setAa(int aa) {
            this.aa = aa;
        }

        public int getAcc() {
            return acc;
        }

        public void setAcc(int acc) {
            this.acc = acc;
        }

        public int getAs() {
            return as;
        }

        public void setAs(int as) {
            this.as = as;
        }

        public int getDodge() {
            return dodge;
        }

        public void setDodge(int dodge) {
            this.dodge = dodge;
        }

        public int getTorpedo() {
            return torpedo;
        }

        public void setTorpedo(int torpedo) {
            this.torpedo = torpedo;
        }

        public int getArmor() {
            return armor;
        }

        public void setArmor(int armor) {
            this.armor = armor;
        }

        public int getSearch() {
            return search;
        }

        public void setSearch(int search) {
            this.search = search;
        }

        public int getBomb() {
            return bomb;
        }

        public void setBomb(int bomb) {
            this.bomb = bomb;
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

    public static class RemarkEntity {
        private String zh;
        private String ja;

        public String getZh() {
            return zh;
        }

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getJa() {
            return ja;
        }

        public void setJa(String ja) {
            this.ja = ja;
        }
    }
}

