package com.example.model;

import java.util.*;

public class Quest {
    public Quest() {
        setTitle(new MultiLanguageEntry());
        setContent(new MultiLanguageEntry());
        setReward(new RewardEntity());
        setUnlock(new ArrayList<>());
    }

    private int id;
    private String code;
    private boolean newMission;
    private List<String> unlock;
    private List<String> after;
    private int period;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isNewMission() {
        return newMission;
    }


    private MultiLanguageEntry title;

    private MultiLanguageEntry content;


    private RewardEntity reward;

    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setNewMission(boolean newMission) {
        this.newMission = newMission;
    }

    public boolean getNewMission() {
        return newMission;
    }

    public void setUnlock(List<String> unlock) {
        this.unlock = unlock;
    }

    public List<String> getUnlock() {
        return unlock;
    }

    public void setTitle(MultiLanguageEntry title) {
        this.title = title;
    }

    public MultiLanguageEntry getTitle() {
        return title;
    }

    public void setContent(MultiLanguageEntry content) {
        this.content = content;
    }

    public MultiLanguageEntry getContent() {
        return content;
    }

    public void setReward(RewardEntity reward) {
        this.reward = reward;
    }

    public RewardEntity getReward() {
        return reward;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public List<String> getAfter() {
        return after;
    }

    public void setAfter(List<String> after) {
        this.after = after;
    }

    public static class RewardEntity {
        private List<Integer> resource;
        private List<Integer> ship;
        private List<Integer> equip;
        private List<Integer> item;
        private String str;

        public RewardEntity() {
            resource = new ArrayList<Integer>();
            ship = new ArrayList<Integer>();
            equip = new ArrayList<Integer>();
            item = new ArrayList<Integer>();
            str = new String();
        }

        public void setResource(List<Integer> resource) {
            this.resource = resource;
        }

        public List<Integer> getResource() {
            return resource;
        }

        public void setShip(List<Integer> ship) {
            this.ship = ship;
        }

        public List<Integer> getShip() {
            return ship;
        }

        public void setEquip(List<Integer> equip) {
            this.equip = equip;
        }

        public List<Integer> getEquip() {
            return equip;
        }

        public void setItem(List<Integer> item) {
            this.item = item;
        }

        public List<Integer> getItem() {
            return item;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }
}
