package com.example.model;

/**
 * Created by Rikka on 2016/4/16.
 */
public class Item {
    private int id;
    private MultiLanguageEntry name;

    public Item(String name, int id) {
        this.name = new MultiLanguageEntry(name);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MultiLanguageEntry getName() {
        return name;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }
}
