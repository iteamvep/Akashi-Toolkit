package com.example.list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rikka on 2016/6/30.
 */
public class ShipForm {
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_READING = 3;

    public static final int COLUMN_REMODEL_FROM = 5;
    public static final int COLUMN_REMODEL_AFTER = 6;
    public static final int COLUMN_REMODEL_LEVEL = 7;
    public static final int COLUMN_REMODEL_AMMO = 8;
    public static final int COLUMN_REMODEL_FUEL = 9;
    public static final int COLUMN_REMODEL_BLUEPRINT = 10;

    public static final int COLUMN_HP = 11;
    public static final int COLUMN_HP_MAX = 12;
    public static final int COLUMN_HP_WEDDING = 13;
    public static final int COLUMN_FIRE = 14;
    public static final int COLUMN_FIRE_MAX = 15;
    public static final int COLUMN_TORPEDO = 16;
    public static final int COLUMN_TORPEDO_MAX = 17;
    public static final int COLUMN_AA = 18;
    public static final int COLUMN_AA_MAX = 19;
    public static final int COLUMN_ARMOR = 20;
    public static final int COLUMN_ARMOR_MAX = 21;
    public static final int COLUMN_ASW = 22;
    public static final int COLUMN_ASW_MAX = 23;
    public static final int COLUMN_ASW_MAX2 = 24;
    public static final int COLUMN_EVASION = 25;
    public static final int COLUMN_EVASION_MAX = 26;
    public static final int COLUMN_EVASION_MAX2 = 27;
    public static final int COLUMN_LOS = 28;
    public static final int COLUMN_LOS_MAX = 29;
    public static final int COLUMN_LOS_MAX2 = 30;
    public static final int COLUMN_LUCK = 31;
    public static final int COLUMN_LUCK_MAX = 32;

    public static final int COLUMN_SPEED = 33;
    public static final int COLUMN_RANGE = 34;

    public static final int COLUMN_SLOT_COUNT = 36;
    public static final int COLUMN_SLOT_MAX_1 = 37;
    public static final int COLUMN_SLOT_MAX_2 = 38;
    public static final int COLUMN_SLOT_MAX_3 = 39;
    public static final int COLUMN_SLOT_MAX_4 = 40;
    public static final int COLUMN_SLOT_MAX_5 = 41;
    public static final int COLUMN_EQUIP_1 = 42;
    public static final int COLUMN_EQUIP_2 = 43;
    public static final int COLUMN_EQUIP_3 = 44;
    public static final int COLUMN_EQUIP_4 = 45;
    public static final int COLUMN_EQUIP_5 = 46;

    private static ShipForm instance;
    private static List<String[]> sList;

    public ShipForm() throws FileNotFoundException {
        sList = new ArrayList<>();

        InputStream is = new FileInputStream(new File("datagenerator/ships.csv"));
        Scanner sc = new Scanner(is);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.charAt(0) <= '9' && line.charAt(0) >= '0')
                sList.add(line.split(","));
        }
    }

    private static void instance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ShipForm();
        }
    }

    public static String[] getRowById(int id) throws FileNotFoundException {
        instance();

        for (String[] row : sList) {
            if (row[0].equals(Integer.toString(id))) {
                return row;
            }
        }
        return null;
    }
}
