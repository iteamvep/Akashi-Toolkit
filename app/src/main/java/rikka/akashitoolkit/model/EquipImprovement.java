package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovement extends BaseDataModel {

    /**
     * name : 12.7cm連装砲
     * id : 2
     * type : 小口径主砲
     * icon : 1
     * secretary : [{"name":"無","day":[true,true,true,true,true,true,true]}]
     */

    /**
     * name : 無
     * day : [true,true,true,true,true,true,true]
     */

    private List<SecretaryEntity> secretary;


    public void setSecretary(List<SecretaryEntity> secretary) {
        this.secretary = secretary;
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
