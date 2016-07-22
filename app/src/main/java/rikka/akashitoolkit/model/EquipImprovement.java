package rikka.akashitoolkit.model;

import java.util.*;
import java.util.Map;

/**
 * Created by Rikka on 2016/3/17.
 */
public class EquipImprovement extends BaseDataModel {

    private List<Integer> upgrade_to;
    private java.util.Map<Integer, List<Integer>> data;

    public Map<Integer, List<Integer>> getData() {
        return data;
    }

    public void setData(Map<Integer, List<Integer>> data) {
        this.data = data;
    }

    public List<Integer> getUpgradeTo() {
        return upgrade_to;
    }

    public void setUpgradeTo(List<Integer> upgrade_to) {
        this.upgrade_to = upgrade_to;
    }
}
