package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/6/15.
 */
public class Expedition {
    public Expedition() {
        setName(new MultiLanguageEntry());
        setRequire(new RequireEntity());
        setReward(new RewardEntity());
    }

    /*编号 =1|
      日文名字 =練習航海|
      中文名字 =练习航海|
      耗时 =00:15|
      提督经验值 =10|
      舰娘经验值 =10|
      燃料 =|
      弹药 =30/120|
      钢铁 =|
      铝 =
    */
    private int id;
    private MultiLanguageEntry name;
    private int time;
    private String timeString;
    private RewardEntity reward;
    private RequireEntity require;

    public static class RewardEntity {

        private String playerXP;
        private String shipXP;
        private List<String> resourceString;
        private List<Integer> resource;
        private String award;
        private String award2;

        public RewardEntity() {
            resourceString = new ArrayList<>();
            resource = new ArrayList<>();
        }

        public void setPlayerXP(String playerXP) {
            this.playerXP = playerXP;
        }

        public String getPlayerXP() {
            return playerXP;
        }

        public void setShipXP(String shipXP) {
            this.shipXP = shipXP;
        }

        public String getShipXP() {
            return shipXP;
        }

        public void setResourceString(List<String> resource) {
            this.resourceString = resource;
        }

        public List<String> getResourceString() {
            return resourceString;
        }

        public void setResource(List<Integer> resource) {
            this.resource = resource;
        }

        public List<Integer> getResource() {
            return resource;
        }

        public void setAward(String award) {
            this.award = award;
        }

        public void setAward2(String award2) {
            this.award2 = award2;
        }

        @Override
        public String toString() {
            return "{" +
                    "playerXP=" + playerXP +
                    ", shipXP=" + shipXP +
                    ", resourceString=" + resourceString +
                    ", resource=" + resource +
                    ", award=" + award +
                    ", award2=" + award2 +
                    '}';
        }
    }

    /*|编号 =1|
      日文名字 =練習航海|
      中文名字 =练习航海|
      耗时 =00:15|
      舰队总等级 =|
      旗舰等级 =1|
      最低舰娘数 =2|
      必要舰娘 =任意|
      输送桶 =|
      燃料消耗 =-3|
      弹药消耗 =
    */
    public static class RequireEntity {
        private int totalLevel;
        private int flagshipLevel;
        private int minShips;
        private String essentialShip;
        private String bucket;
        private List<Integer> consume;
        private List<String> consumeString;

        public RequireEntity() {
            consume = new ArrayList<>();
            consumeString = new ArrayList<>();
        }

        public void setTotalLevel(int totalLevel) {
            this.totalLevel = totalLevel;
        }

        public int getTotalLevel() {
            return totalLevel;
        }

        public void setFlagshipLevel(int flagshipLevel) {
            this.flagshipLevel = flagshipLevel;
        }

        public int getFlagshipLevel() {
            return flagshipLevel;
        }

        public void setMinShips(int minShips) {
            this.minShips = minShips;
        }

        public int getMinShips() {
            return minShips;
        }

        public void setEssentialShip(String essentialShip) {
            this.essentialShip = essentialShip;
        }

        public String getEssentialShip() {
            return essentialShip;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getBucket() {
            return bucket;
        }

        public void setComsume(List<Integer> consume) {
            this.consume = consume;
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

        @Override
        public String toString() {
            return "{" +
                    "totalLevel=" + totalLevel +
                    ", flagshipLevel=" + flagshipLevel +
                    ", minShips=" + minShips +
                    ", essentialShip='" + essentialShip + '\'' +
                    ", bucket=" + bucket +
                    ", consume=" + consume +
                    '}';
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public int getId() {
        return id;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }

    public MultiLanguageEntry getName() {
        return name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setReward(RewardEntity reward) {
        this.reward = reward;
    }

    public RewardEntity getReward() {
        return reward;
    }

    public void setRequire(RequireEntity require) {
        this.require = require;
    }

    public RequireEntity getRequire() {
        return require;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String toString() {
        return ("Expedition{" +
                "id=" + id +
                ", name=" + name +
                ", time=" + time +
                ", reward=" + reward +
                ", require=" + require +
                '}').replace("\n", "\\n");
    }
}
