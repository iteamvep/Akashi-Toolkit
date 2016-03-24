package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/3/24.
 */
public class ItemType {

    /**
     * id : 1
     * parent : 火炮 / 强化弹
     * name : 小口径主炮
     */

    private int id;
    private String parent;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
