package com.example.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Rikka on 2016/7/22.
 */
public class EquipImprovement {
    private int id;
    private List<Integer> upgrade_to;
    private Map<Integer, List<Integer>> data;

    public EquipImprovement(int id, List<Integer> upgrade_to, Map<Integer, List<Integer>> data) {
        this.id = id;
        this.upgrade_to = upgrade_to;
        this.data = data;
    }

    public EquipImprovement(int id, Map<Integer, List<Integer>> data) {
        this.id = id;
        this.data = data;
    }
}
