package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/6/15.
 */
public class Expedition extends BaseDataModel {

    /**
     * id : 1
     * name : {"zh_cn":"练习航海","ja":"練習航海"}
     * time : 15
     * timeString : 00:15
     * reward : {"playerXP":"10","shipXP":"10","resourceString":["","30\n120/h","",""],"resource":[0,30,0,0]}
     * require : {"totalLevel":0,"flagshipLevel":1,"minShips":2,"essentialShip":"任意","bucket":"","consume":[-3,0]}
     */
    private MultiLanguageEntry name;

    private int time;
    private int type;
    private String timeString;
    /**
     * playerXP : 10
     * shipXP : 10
     * resourceString : ["","30\n120/h","",""]
     * resource : [0,30,0,0]
     */

    private RewardEntity reward;
    /**
     * totalLevel : 0
     * flagshipLevel : 1
     * minShips : 2
     * essentialShip : 任意
     * bucket :
     * consume : [-3,0]
     */

    private RequireEntity require;


    public MultiLanguageEntry getName() {
        return name;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public RewardEntity getReward() {
        return reward;
    }

    public void setReward(RewardEntity reward) {
        this.reward = reward;
    }

    public RequireEntity getRequire() {
        return require;
    }

    public void setRequire(RequireEntity require) {
        this.require = require;
    }

    public static class RewardEntity {
        private String playerXP;
        private String shipXP;
        private List<String> resourceString;
        private List<Integer> resource;
        private String award;
        private String award2;

        public String getPlayerXP() {
            return playerXP;
        }

        public void setPlayerXP(String playerXP) {
            this.playerXP = playerXP;
        }

        public String getShipXP() {
            return shipXP;
        }

        public void setShipXP(String shipXP) {
            this.shipXP = shipXP;
        }

        public List<String> getResourceString() {
            return resourceString;
        }

        public void setResourceString(List<String> resourceString) {
            this.resourceString = resourceString;
        }

        public List<Integer> getResource() {
            return resource;
        }

        public void setResource(List<Integer> resource) {
            this.resource = resource;
        }

        public String getAward() {
            return award;
        }

        public void setAward(String award) {
            this.award = award;
        }

        public String getAward2() {
            return award2;
        }

        public void setAward2(String award2) {
            this.award2 = award2;
        }
    }

    public static class RequireEntity {
        private int totalLevel;
        private int flagshipLevel;
        private int minShips;
        private String essentialShip;
        private String bucket;
        private List<Integer> consume;
        private List<String> consumeString;

        public int getTotalLevel() {
            return totalLevel;
        }

        public void setTotalLevel(int totalLevel) {
            this.totalLevel = totalLevel;
        }

        public int getFlagshipLevel() {
            return flagshipLevel;
        }

        public void setFlagshipLevel(int flagshipLevel) {
            this.flagshipLevel = flagshipLevel;
        }

        public int getMinShips() {
            return minShips;
        }

        public void setMinShips(int minShips) {
            this.minShips = minShips;
        }

        public String getEssentialShip() {
            return essentialShip;
        }

        public void setEssentialShip(String essentialShip) {
            this.essentialShip = essentialShip;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public List<Integer> getConsume() {
            return consume;
        }

        public void setConsume(List<Integer> consume) {
            this.consume = consume;
        }

        public List<String> getConsumeString() {
            return consumeString;
        }

        public void setConsumeString(List<String> consumeString) {
            this.consumeString = consumeString;
        }
    }
}
