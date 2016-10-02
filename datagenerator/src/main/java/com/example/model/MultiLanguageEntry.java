package com.example.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Rikka on 2016/4/16.
 */
public class MultiLanguageEntry {
    @Expose private String zh_cn;
    @Expose private String ja;
    @Expose
    private String en;

    public MultiLanguageEntry() {
        zh_cn = "";
        ja = "";
        en = "";
    }

    public MultiLanguageEntry(String name) {
        zh_cn = name;
        ja = name;
        en = name;
    }

    public String getZh_cn() {
        return zh_cn;
    }

    public void setZh_cn(String zh_cn) {
        this.zh_cn = zh_cn;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @Override
    public String toString() {
        return "{" +
                "zh_cn='" + zh_cn + '\'' +
                ", ja='" + ja + '\'' +
                '}';
    }
}