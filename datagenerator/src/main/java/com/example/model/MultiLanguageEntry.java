package com.example.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Rikka on 2016/4/16.
 */
public class MultiLanguageEntry {
    @Expose private String zh_cn;
    @Expose
    private String zh_tw;
    @Expose private String ja;
    @Expose
    private String en;

    public MultiLanguageEntry() {
        zh_cn = "";
        ja = "";
        en = "";
        zh_tw = "";
    }

    public MultiLanguageEntry(String name) {
        zh_cn = name;
        zh_tw = name;
        ja = name;
        en = name;
    }

    public MultiLanguageEntry(String zh_cn, String ja, String en) {
        this.zh_cn = zh_cn;
        this.ja = ja;
        this.en = en;
    }

    public MultiLanguageEntry(String zh_cn, String zh_tw, String ja, String en) {
        this.zh_cn = zh_cn;
        this.zh_tw = zh_tw;
        this.ja = ja;
        this.en = en;
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

    public String getZh_tw() {
        return zh_tw;
    }

    public void setZh_tw(String zh_tw) {
        this.zh_tw = zh_tw;
    }

    @Override
    public String toString() {
        return "{" +
                "zh_cn='" + zh_cn + '\'' +
                ", ja='" + ja + '\'' +
                '}';
    }
}