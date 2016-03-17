package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/3/17.
 */
public class ItemImprovement {

    /**
     * name : 12.7cm連装砲
     * id : 2
     * type : 小口径主砲
     * icon : 1
     * secretary : [{"name":"無","day":[true,true,true,true,true,true,true]}]
     */

    private String name;
    private int id;
    private String type;
    private int icon;
    /**
     * name : 無
     * day : [true,true,true,true,true,true,true]
     */

    private List<SecretaryEntity> secretary;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setSecretary(List<SecretaryEntity> secretary) {
        this.secretary = secretary;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getIcon() {
        return icon;
    }

    public List<SecretaryEntity> getSecretary() {
        return secretary;
    }

    public static class SecretaryEntity {
        private String name;
        private List<Boolean> day;

        public void setName(String name) {
            this.name = name;
        }

        public void setDay(List<Boolean> day) {
            this.day = day;
        }

        public String getName() {
            return name;
        }

        public List<Boolean> getDay() {
            return day;
        }
    }
}
