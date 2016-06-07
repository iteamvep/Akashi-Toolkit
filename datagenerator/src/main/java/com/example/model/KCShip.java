package com.example.model;

/**
 * Created by sdlds on 2016/5/9.
 */
public class KCShip {
    private String name;
    private int ctype;
    private int cnum;

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public int getCnum() {
        return cnum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
