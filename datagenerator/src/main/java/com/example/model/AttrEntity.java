package com.example.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Rikka on 2016/8/9.
 */
public class AttrEntity {

    @Expose
    private int range;
    @Expose
    private int speed;
    @Expose
    private int aa;
    @Expose
    private int armor;
    @Expose
    private int asw;
    @Expose
    private int evasion;
    @Expose
    private int fire;
    @Expose
    private int hp;
    @Expose
    private int luck;
    @Expose
    private int los;
    @Expose
    private int torpedo;
    @Expose
    private int bomb;
    @Expose
    private int accuracy;

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAA() {
        return aa;
    }

    public int getArmor() {
        return armor;
    }

    public int getASW() {
        return asw;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getFirepower() {
        return fire;
    }

    public int getHP() {
        return hp;
    }

    public int getLuck() {
        return luck;
    }

    public int getLOS() {
        return los;
    }

    public int getTorpedo() {
        return torpedo;
    }

    public int getBombing() {
        return bomb;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public AttrEntity plus(AttrEntity o) {
        if (o == null) {
            return this;
        }

        AttrEntity n = new AttrEntity();

        n.aa = aa + o.aa;
        n.armor = armor + o.aa;
        n.asw = asw + o.asw;
        n.evasion = evasion + o.evasion;
        n.fire = fire + o.fire;
        n.hp = hp + o.hp;
        n.luck = luck + o.luck;
        n.los = los + o.los;
        n.torpedo = torpedo + o.torpedo;
        n.bomb = bomb + o.bomb;
        n.accuracy = accuracy + o.accuracy;
        //n.speed = speed;
        //n.range = range > o.range ? range : o.range;

        return n;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setAA(int aa) {
        this.aa = aa;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setASW(int asw) {
        this.asw = asw;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public void setLOS(int los) {
        this.los = los;
    }

    public void setTorpedo(int torpedo) {
        this.torpedo = torpedo;
    }

    public void setBombing(int bomb) {
        this.bomb = bomb;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}
