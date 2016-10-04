package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipTypeParent {
    private int id;
    private MultiLanguageEntry name;
    private List<Integer> child;

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
