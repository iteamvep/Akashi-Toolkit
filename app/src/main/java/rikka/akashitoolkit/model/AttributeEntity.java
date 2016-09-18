package rikka.akashitoolkit.model;

import android.support.annotation.Nullable;

/**
 * Created by Rikka on 2016/7/27.
 */
public class AttributeEntity {

    private int range;
    private int speed;
    private int aa;
    private int armor;
    private int asw;
    private int evasion;
    private int fire;
    private int hp;
    private int luck;
    private int los;
    private int torpedo;
    private int bomb;
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

    public AttributeEntity plus(@Nullable AttributeEntity o) {
        if (o == null) {
            return this;
        }

        AttributeEntity n = new AttributeEntity();

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
}
