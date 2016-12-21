package com.example.model;

/**
 * Created by Rikka on 2016/10/4.
 */

public class APIEquipType {

    private int show_flg;
    private String name;
    private int id;
    private String chinese_name;

    public APIEquipType(int show_flg, String name, int id, String chinese_name) {
        this.show_flg = show_flg;
        this.name = name;
        this.id = id;
        this.chinese_name = chinese_name;
    }

    public int getShowFlag() {
        return show_flg;
    }

    public void setShowFlag(int show_flg) {
        this.show_flg = show_flg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChineseName() {
        return chinese_name;
    }

    public void setChinesName(String chinese_name) {
        this.chinese_name = chinese_name;
    }
}
