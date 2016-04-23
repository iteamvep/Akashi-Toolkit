package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/4/23.
 */
public class EnemyShip {

    /**
     * id : 501
     * name : {"ja":"駆逐イ級","zh_cn":"駆逐イ級"}
     */

    private int id;
    /**
     * ja : 駆逐イ級
     * zh_cn : 駆逐イ級
     */

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
