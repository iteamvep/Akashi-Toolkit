package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/4/9.
 */
public class Map {

    /**
     * sea : 1
     * area : 1
     * map : 1-1
     * airforce : [[0,0,0]]
     * recommend : ["任意"]
     * condition : ["无"]
     * other : ["任意1~2艘以上舰船。"]
     */

    private int sea;
    private int area;
    private String map;
    private List<List<Integer>> airforce;
    private List<String> recommend;
    private List<String> condition;
    private List<String> other;

    public int getSea() {
        return sea;
    }

    public void setSea(int sea) {
        this.sea = sea;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public List<List<Integer>> getAirforce() {
        return airforce;
    }

    public void setAirforce(List<List<Integer>> airforce) {
        this.airforce = airforce;
    }

    public List<String> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<String> recommend) {
        this.recommend = recommend;
    }

    public List<String> getCondition() {
        return condition;
    }

    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    public List<String> getOther() {
        return other;
    }

    public void setOther(List<String> other) {
        this.other = other;
    }
}
