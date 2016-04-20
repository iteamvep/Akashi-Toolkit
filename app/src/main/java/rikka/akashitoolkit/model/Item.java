package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/4/19.
 */
public class Item {
    private int id;

    private MultiLanguageEntry name;

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
