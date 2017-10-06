package com.example.list;

import com.example.model.Item;

import java.util.List;

/**
 * Created by Rikka on 2016/4/16.
 */
public class ItemList {
    public static Item findByName(String name, List<Item> list) {
        for (Item item :
                list) {
            if (item.getName().getJa().equals(name)
                    || item.getName().getZh_cn().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
