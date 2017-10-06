package com.example.model;

/**
 * Created by sdlds on 2016/5/9.
 */
public class ShipClass {
    private String name;
    private int id;

    public ShipClass() {
    }

    public ShipClass(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setCtype(int ctype) {
        this.id = ctype;
    }

    public int getCtype() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
