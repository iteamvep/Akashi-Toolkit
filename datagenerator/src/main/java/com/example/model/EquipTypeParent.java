package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipTypeParent {
    private int id;
    private MultiLanguageEntry name;
    private List<Integer> child;

    public EquipTypeParent(int id, MultiLanguageEntry name) {
        this.id = id;
        this.name = name;
        this.child = new ArrayList<>();
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

    public List<Integer> getChild() {
        return child;
    }

    public void setChild(List<Integer> child) {
        this.child = child;
    }
}
