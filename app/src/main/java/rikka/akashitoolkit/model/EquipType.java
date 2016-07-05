package rikka.akashitoolkit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rikka on 2016/3/24.
 */
public class EquipType {

    /**
     * id : 1
     * parent : 火炮 / 强化弹
     * name : 小口径主炮
     */

    private int id;
    private int patent_id;
    @SerializedName("parent")
    private String parent_name;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatentId() {
        return patent_id;
    }

    public void setPatentId(int patent_id) {
        this.patent_id = patent_id;
    }

    public String getParentName() {
        return parent_name;
    }

    public void setParentName(String parent) {
        this.parent_name = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
