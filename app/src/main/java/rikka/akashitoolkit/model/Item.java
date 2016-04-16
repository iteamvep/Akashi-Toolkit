package rikka.akashitoolkit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/3/26.
 */
public class Item {
    /**
     * id : 8
     * name : {"zh_cn":"35.6cm连装炮","ja":"35.6cm连装炮"}
     * introduction : {"zh_cn":"旧型的小型主炮。被旧型驱逐舰作为标准主炮所搭载。<br/>虽然装填和操作都需要人力完成，但由于其结构简单，经济性较高，也长期用于驱逐舰、海防舰等舰船上。<br />不适合用于对空射击。","ja":"旧型の小型砲です。旧型駆逐艦に標準的主砲として搭載されました。<br />装填・操砲も人力ですが、シンプルな構造で経済性も高く、駆逐艦や海防艦等の主砲として、長く使われました。<br />対空射撃には不向きです。"}
     * remark :
     * attr : {"fire":15,"aa":4,"range":3}
     * discard : [0,10,15,0]
     * rarity : 1
     * type : 0
     * icon : 0
     * shipLimit : [0,1,2,3]
     * improvement : {"secretary":[{"name":"夕立改二","day":[false,true,true,true,false,false,false]},{"name":"绫波改二","day":[false,true,true,true,false,false,false]}],"resource":{"base":[10,10,10,10],"item":[[7,9,5,7,1,2],[7,9,5,7,1,2]],"levelup":1}}
     */

    private int id;
    /**
     * zh_cn : 35.6cm连装炮
     * ja : 35.6cm连装炮
     */

    private MultiLanguageEntry name;
    /**
     * zh_cn : 旧型的小型主炮。被旧型驱逐舰作为标准主炮所搭载。<br/>虽然装填和操作都需要人力完成，但由于其结构简单，经济性较高，也长期用于驱逐舰、海防舰等舰船上。<br />不适合用于对空射击。
     * ja : 旧型の小型砲です。旧型駆逐艦に標準的主砲として搭載されました。<br />装填・操砲も人力ですが、シンプルな構造で経済性も高く、駆逐艦や海防艦等の主砲として、長く使われました。<br />対空射撃には不向きです。
     */

    private MultiLanguageEntry introduction;
    private String remark;
    /**
     * fire : 15
     * aa : 4
     * range : 3
     */

    private AttrEntity attr;
    private int rarity;
    private int type;
    private int subType;
    private int icon;
    /**
     * secretary : [{"name":"夕立改二","day":[false,true,true,true,false,false,false]},{"name":"绫波改二","day":[false,true,true,true,false,false,false]}]
     * resource : {"base":[10,10,10,10],"item":[[7,9,5,7,1,2],[7,9,5,7,1,2]],"levelup":1}
     */

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
        private int range;
        private int fire;
        private int aa;
        private int acc;
        private int asw;
        private int evasion;
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

        public int getAsw() {
            return asw;
        }

        public void setAsw(int asw) {
            this.asw = asw;
        }

        public int getEvasion() {
            return evasion;
        }

        public void setEvasion(int evasion) {
            this.evasion = evasion;
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
