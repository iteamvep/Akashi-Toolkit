package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/4/19.
 */
public class Quest extends BaseDataModel {

    /**
     * id : 0
     * code : B63
     * newMission : true
     * unlock : ["A1"]
     * title : {"zh_cn":"製油所地帯を防衛せよ！","ja":"制油所地带防卫作战！"}
     * content : {"zh_cn":"水雷战队于制油所沿岸地带展开！歼灭同海域出没的敌舰队三次以上！","ja":"水雷戦隊を製油所地帯沿岸に展開！同海域に出没する敵艦隊を三回以上撃滅せよ！"}
     * reward : {"resource":[400,0,0,0],"ship":[1],"equip":[1],"item":[0,1,1,1],"str":""}
     * note : 1-3BossS胜3次即可(只含1轻巡且其他舰船均为驱逐舰即可完成)
     */
    private boolean highlight;

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    private String code;
    private boolean newMission;
    private int period;
    private int type;
    /**
     * zh_cn : 製油所地帯を防衛せよ！
     * ja : 制油所地带防卫作战！
     */

    private MultiLanguageEntry title;
    /**
     * zh_cn : 水雷战队于制油所沿岸地带展开！歼灭同海域出没的敌舰队三次以上！
     * ja : 水雷戦隊を製油所地帯沿岸に展開！同海域に出没する敵艦隊を三回以上撃滅せよ！
     */

    private MultiLanguageEntry content;
    /**
     * resource : [400,0,0,0]
     * ship : [1]
     * equip : [1]
     * item : [0,1,1,1]
     * str :
     */

    private RewardEntity reward;
    private String note;
    private List<String> unlock;
    private List<String> after;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isNewMission() {
        return newMission;
    }

    public void setNewMission(boolean newMission) {
        this.newMission = newMission;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MultiLanguageEntry getTitle() {
        return title;
    }

    public void setTitle(MultiLanguageEntry title) {
        this.title = title;
    }

    public MultiLanguageEntry getContent() {
        return content;
    }

    public void setContent(MultiLanguageEntry content) {
        this.content = content;
    }

    public RewardEntity getReward() {
        return reward;
    }

    public void setReward(RewardEntity reward) {
        this.reward = reward;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getUnlock() {
        return unlock;
    }

    public void setUnlock(List<String> unlock) {
        this.unlock = unlock;
    }

    public List<String> getAfter() {
        return after;
    }

    public void setAfter(List<String> after) {
        this.after = after;
    }

    public static class RewardEntity {
        private String str;
        private List<Integer> resource;
        private List<Integer> ship;
        private List<Integer> equip;
        private List<Integer> item;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public List<Integer> getResource() {
            return resource;
        }

        public void setResource(List<Integer> resource) {
            this.resource = resource;
        }

        public List<Integer> getShip() {
            return ship;
        }

        public void setShip(List<Integer> ship) {
            this.ship = ship;
        }

        public List<Integer> getEquip() {
            return equip;
        }

        public void setEquip(List<Integer> equip) {
            this.equip = equip;
        }

        public List<Integer> getItem() {
            return item;
        }

        public void setItem(List<Integer> item) {
            this.item = item;
        }
    }
}
