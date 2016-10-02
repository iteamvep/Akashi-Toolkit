package rikka.akashitoolkit.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.ShipList;

/**
 * Created by Rikka on 2016/7/29.
 */
public class Fleet {
    @Expose
    String name;
    @Expose
    List<Ship> ships;
    int level;
    int fuel;
    int ammo;
    double[] aa;
    float los;
    boolean low_speed;

    public String getName() {
        return name;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public double[] getAA() {
        return aa;
    }

    public void setAA(double[] aa) {
        this.aa = aa;
    }

    public float getLOS() {
        return los;
    }

    public void setLOS(float los) {
        this.los = los;
    }

    public boolean isLowSpeed() {
        return low_speed;
    }

    public void setLowSpeed(boolean low_speed) {
        this.low_speed = low_speed;
    }

    public static class Ship {
        @Expose
        int id;
        @Expose
        int level;
        @Expose
        List<Equip> equips;
        rikka.akashitoolkit.model.Ship ship;
        AttributeEntity attr;
        double[] aa;
        int[] slots;

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public List<Equip> getEquips() {
            return equips;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setEquips(List<Equip> equips) {
            this.equips = equips;
        }

        public rikka.akashitoolkit.model.Ship getShip() {
            return ship;
        }

        public void setShip(rikka.akashitoolkit.model.Ship ship) {
            this.ship = ship;
        }

        public AttributeEntity getAttr() {
            return attr;
        }

        public void setAttr(AttributeEntity attr) {
            this.attr = attr;
        }

        public double[] getAA() {
            return aa;
        }

        public void setAA(double[] aa) {
            this.aa = aa;
        }

        public int[] getSlots() {
            return slots;
        }

        public void setSlots(int[] slots) {
            this.slots = slots;
        }

        public static class Equip {
            @Expose
            int id;
            @Expose
            int star;
            @Expose
            int rank;
            rikka.akashitoolkit.model.Equip equip;

            public int getId() {
                return id;
            }

            public int getStar() {
                return star;
            }

            public int getRank() {
                return rank;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public rikka.akashitoolkit.model.Equip getEquip() {
                return equip;
            }

            public void setEquip(rikka.akashitoolkit.model.Equip equip) {
                this.equip = equip;
            }

            public boolean init(Context context) {
                rikka.akashitoolkit.model.Equip equip = EquipList.findItemById(context, getId());
                if (equip == null) {
                    setId(0);
                    return false;
                }

                setEquip(equip);

                if (!equip.isRankupable()) {
                    rank = 0;
                }

                if (!equip.isImprovable()) {
                    star = 0;
                }

                rank = rank < 0 ? 0 : rank;
                rank = rank > 7 ? 7 : rank;

                star = star < 0 ? 0 : star;
                star = star > 10 ? 10 : star;

                return true;
            }

            @Override
            public String toString() {
                return "Equip{" +
                        "id=" + id +
                        '}';
            }
        }

        /**
         * 初始化
         *
         * @param context Context
         * @return 是否有效 (在舰娘id在数据中找不到时返回 false)
         */

        public boolean init(Context context) {
            rikka.akashitoolkit.model.Ship ship = ShipList.findItemById(context, getId());
            // remove ship that not exist
            if (ship == null) {
                return false;
            }

            setShip(ship);
            setSlots(ship.getEquip().getSpace());

            // level should be 1 to 155
            if (getLevel() < 1) {
                setLevel(1);
            } else if (getLevel() > 155) {
                setLevel(155);
            }

            // check equips
            int slots = ship.getEquip().getSlots();
            if (getEquips() == null) {
                setEquips(new ArrayList<Fleet.Ship.Equip>());
                for (int i = 0; i < slots; i++) {
                    getEquips().add(new Fleet.Ship.Equip());
                }
                return true;
            }

            List<Fleet.Ship.Equip> equipList = new ArrayList<>();
            for (Fleet.Ship.Equip e : getEquips()) {
                if (equipList.size() >= slots) {
                    break;
                }

                if (e.init(context) && e.getEquip() != null && getShip().getShipType().canEquip(e.getEquip())) {
                    equipList.add(e);
                } else {
                    equipList.add(new Fleet.Ship.Equip());
                }
            }

            // make sure list size equip slots
            for (int i = equipList.size(); i < slots; i++) {
                equipList.add(new Fleet.Ship.Equip());
            }

            getEquips().clear();
            getEquips().addAll(equipList);
            setEquips(equipList);

            return true;
        }

        /**
         * 计算属性和制空
         * 在内部数据发生变化事调用来重新计算
         */
        public void calc() {
            setAttr(new AttributeEntity());
            setAttr(getShip().getAttribute().plus(getShip().getAttributeMax()));

            double aa = 0;
            double min_aa = 0, max_aa = 0;
            for (int i = 0; i < getEquips().size(); i++) {
                Equip e = getEquips().get(i);
                if (e.getId() == 0) {
                    continue;
                }
                setAttr(getAttr().plus(e.getEquip().getAttr()));

                if (!e.getEquip().isCarrierBasedAircraft() && !e.getEquip().isSeaplane()) {
                    continue;
                }

                int level = e.getRank();
                // (面版对空) × √(搭载数) + 熟练度加成
                if (e.getEquip().isCarrierBasedAircraft()) {
                    aa += Math.sqrt(getSlots()[i]) * (e.getEquip().getAttr().getAA() + e.getStar() * 0.2);
                } else {
                    aa += Math.sqrt(getSlots()[i]) * e.getEquip().getAttr().getAA();
                }
                aa += aircraftLevelBonus.get(e.getEquip().getTypes()[2])[level];
                min_aa = Math.floor(aa + Math.sqrt(aircraftExpTable[level] / 10));
                max_aa = Math.floor(aa + Math.sqrt(aircraftExpTable[level + 1] / 10));
            }

            setAA(new double[]{min_aa, max_aa});
        }
    }

    public void init(Context context) {
        if (TextUtils.isEmpty(getName())) {
            setName(context.getString(R.string.fleet_default_title));
        }

        if (getShips() == null) {
            setShips(new ArrayList<Ship>());
            return;
        }

        List<Fleet.Ship> shipList = new ArrayList<>();
        for (Fleet.Ship s : getShips()) {
            if (shipList.size() >= 6) {
                return;
            }

            if (s.init(context)) {
                shipList.add(s);
            }
        }

        getShips().clear();
        getShips().addAll(shipList);

        calc();
    }

    /**
     * 计算属性和制空等
     * 在内部数据发生变化事调用来重新计算
     */
    public void calc() {
        int level = 0, fuel = 0, ammo = 0;
        double min_aa = 0, max_aa = 0;
        for (Fleet.Ship s : getShips()) {
            s.calc();

            min_aa += s.getAA()[0];
            max_aa += s.getAA()[1];

            level += s.getLevel();
            fuel += s.getShip().getResourceConsume()[0];
            ammo += s.getShip().getResourceConsume()[1];

            if (s.getShip().getAttribute().getSpeed() < 10) {
                setLowSpeed(true);
            }
        }
        setLevel(level);
        setFuel(fuel);
        setAmmo(ammo);
        setAA(new double[]{min_aa, max_aa});
    }

    private static int[] aircraftExpTable;
    private static java.util.Map<Integer, int[]> aircraftLevelBonus;

    public final static String[] equipRank;

    static {
        aircraftExpTable = new int[]{0, 10, 25, 40, 55, 70, 85, 100, 121};

        aircraftLevelBonus = new HashMap<>();
        aircraftLevelBonus.put(6, new int[]{0, 0, 2, 5, 9, 14, 14, 22, 22});    // 艦上戦闘機
        aircraftLevelBonus.put(7, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});        // 艦上爆撃機
        aircraftLevelBonus.put(8, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});        // 艦上攻撃機
        aircraftLevelBonus.put(11, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0});       // 水上爆撃機
        aircraftLevelBonus.put(45, new int[]{0, 0, 2, 5, 9, 14, 14, 22, 22});   // 水上戦闘機

        equipRank = new String[]{"", "|", "||", "|||", "\\", "\\\\", "\\\\\\", ">>"};
    }

}
