package com.example;

import com.example.list.ShipForm;
import com.example.model.MultiLanguageEntry;
import com.example.model.NewShip;
import com.example.model.ShipClass;
import com.example.utils.TextUtils;
import com.example.utils.WanaKanaJava;
import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.utils.Utils.objectToJsonFile;
import static com.example.list.ShipForm.*;

/**
 * Created by Rikka on 2016/6/28.
 */
public class ShipGenerator {
    private static WanaKanaJava wkj = new WanaKanaJava(false);

    public static void main(String[] args) throws IOException {
        List<NewShip> list = getShipList();

        for (NewShip item : list) {
            item.setName(new MultiLanguageEntry());
            item.getName().setJa(item.getNameJP());
            item.getName().setZh_cn(item.getNameCN());

            item.setNameForSearch(getNameForSearch(item));
        }

        // 不要名字为空或者带有“纪念日”的
        List<NewShip> list2 = list
                .stream()
                .filter(item -> !TextUtils.isEmpty(item.getNameJP()) && !item.getNameJP().contains("記念日"))
                .collect(Collectors.toList());

        for (NewShip item : list2) {
            String[] row = ShipForm.getRowById(item.getId());
            if (row == null) {
                System.out.println("表格中不存在 " + item.getNameJP() + " " + item.getId());
                continue;
            }

            // 不是敌舰
            if (item.getId() < 500) {
                item.setBuildTime(item.getStats().getBuild_time());
                item.setBrokenResources(item.getStats().getBroken().stream().mapToInt(i->i).toArray());
                item.setModernizationBonus(item.getStats().getPow_up().stream().mapToInt(i->i).toArray());
                item.setResourceConsume(new int[]{item.getStats().getFuel_max(), item.getStats().getBull_max()});

                NewShip.RemodelEntity remodel = new NewShip.RemodelEntity();
                item.setRemodel(remodel);
                remodel.setLevel(row[COLUMN_REMODEL_LEVEL]);
                remodel.setCost(row[COLUMN_REMODEL_AMMO], row[COLUMN_REMODEL_FUEL]);
                remodel.setFromId(row[COLUMN_REMODEL_FROM]);
                remodel.setToId(row[COLUMN_REMODEL_AFTER]);
                remodel.setRequireBlueprint(row[COLUMN_REMODEL_BLUEPRINT]);

                NewShip.AttrEntity attr = new NewShip.AttrEntity();
                item.setAttr(attr);
                attr.setHP(row[COLUMN_HP], row[COLUMN_HP_WEDDING]);
                attr.setAA(row[COLUMN_AA], row[COLUMN_AA_MAX]);
                attr.setFirePower(row[COLUMN_FIRE], row[COLUMN_FIRE_MAX]);
                attr.setTorpedo(row[COLUMN_TORPEDO], row[COLUMN_TORPEDO_MAX]);
                attr.setArmor(row[COLUMN_ARMOR], row[COLUMN_ARMOR_MAX]);
                attr.setLuck(row[COLUMN_LUCK], row[COLUMN_LUCK_MAX]);
                attr.setASW(row[COLUMN_ASW], row[COLUMN_ASW_MAX], row[COLUMN_ASW_MAX]);
                attr.setLOS(row[COLUMN_LOS], row[COLUMN_LOS_MAX], row[COLUMN_LOS_MAX]);
                attr.setEvasion(row[COLUMN_EVASION], row[COLUMN_EVASION_MAX], row[COLUMN_EVASION_MAX]);
                attr.setSpeed(row[COLUMN_SPEED]);
                attr.setRange(row[COLUMN_RANGE]);
            } else {
                NewShip.AttrEntity attr = new NewShip.AttrEntity();
                item.setAttr(attr);
                attr.setHP(row[COLUMN_HP]);
                attr.setAA(row[COLUMN_AA]);
                attr.setFirePower(row[COLUMN_FIRE]);
                attr.setTorpedo(row[COLUMN_TORPEDO]);
                attr.setArmor(row[COLUMN_ARMOR]);
                //attr.setLuck(row[COLUMN_LUCK], row[COLUMN_LUCK_MAX]);
                //attr.setASW(row[COLUMN_ASW], row[COLUMN_ASW_MAX], row[COLUMN_ASW_MAX]);
                //attr.setLOS(row[COLUMN_LOS], row[COLUMN_LOS_MAX], row[COLUMN_LOS_MAX]);
                //attr.setEvasion(row[COLUMN_EVASION], row[COLUMN_EVASION_MAX], row[COLUMN_EVASION_MAX]);
                attr.setSpeed(row[COLUMN_SPEED]);
                attr.setRange(row[COLUMN_RANGE]);

                // TODO 用来搜索的名字需要改
                if (!row[COLUMN_READING].equals("-"))
                    item.getName().setJa(String.format("%s %s", item.getNameJP(), row[COLUMN_READING]));
            }

            NewShip.EquipEntity equip = new NewShip.EquipEntity();
            item.setEquip(equip);
            equip.setSlots(row[COLUMN_SLOT_COUNT]);
            equip.setIds(row[COLUMN_EQUIP_1], row[COLUMN_EQUIP_2], row[COLUMN_EQUIP_3], row[COLUMN_EQUIP_4], row[COLUMN_EQUIP_5]);
            equip.setSpaces(row[COLUMN_SLOT_MAX_1], row[COLUMN_SLOT_MAX_2], row[COLUMN_SLOT_MAX_3], row[COLUMN_SLOT_MAX_4], row[COLUMN_SLOT_MAX_5]);
        }

        // 按id从小到大排序
        Collections.sort(list2, (o1, o2) -> o1.getId() - o2.getId());

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        makeShipClassList(list2);

        String str = gson.toJson(list2).replace("\"name2\"", "\"name\"");

        objectToJsonFile(str, "app/src/main/assets/Ship.json");
        objectToJsonFile(shipClassList, "app/src/main/assets/ShipClass.json");
    }

    private static List<NewShip> getShipList() throws IOException {
        /*Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.kcwiki.moe")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitAPI.ShipService service = retrofit.create(RetrofitAPI.ShipService.class);
        return service.getDetail().execute().body();*/
        return new Gson().fromJson(
                new FileReader(new File("datagenerator/ships_detail.json")),
                new TypeToken<List<NewShip>>() {}.getType());
    }

    private static String getNameForSearch(NewShip item) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(item.getYomi())) {
            sb.append(item.getNameJP())
                    .append(',')
                    .append(wkj.toRomaji(item.getYomi()));
        }

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ',') {
            sb.append(',');
        }

        if (!TextUtils.isEmpty(item.getNameCN())) {
            sb.append(item.getNameCN())
                    .append(',');

            for (char c : item.getNameCN().toCharArray()) {
                if (Pinyin.isChinese(c)) {
                    sb.append(Pinyin.toPinyin(c).toLowerCase());
                }
            }
        }

        return sb.toString();
    }

    private static List<ShipClass> shipClassList = new ArrayList<>();

    private static void makeShipClassList(List<NewShip> list) {
        for (NewShip ship : list) {
            if (ship.getId() > 500) {
                break;
            }

            if (ship.getClassNum() == 1) {
                addToShipClassList(shipClassList, ship.getClassType(), ship.getName().getZh_cn());
            }

            if (ship.getClassNum() == 0) {
                NewShip cur = ship;
                while (cur.getRemodel().getFromId() != 0) {
                    cur = findById(cur.getRemodel().getFromId(), list);
                }

                ship.setClassType(cur.getClassType());
                ship.setClassNum(cur.getClassNum());

                System.out.println(ship.getName().getZh_cn() + " 舰级为空 设定为了和 " + cur.getName().getZh_cn() + " 一样的类型");
            }
        }

        for (NewShip ship : list) {
            if (ship.getId() > 500) {
                break;
            }

            if (ship.getClassType() == 0) {
                System.out.println(ship.getName().getZh_cn() + " 没有类型");
            } else if (!isShipClassExist(shipClassList, ship.getClassType())) {
                System.out.println(ship.getName().getZh_cn() + " 没有一号舰");
                addToShipClassList(shipClassList, ship.getClassType(), ship.getName().getZh_cn());
            }
        }
    }

    private static boolean isShipClassExist(List<ShipClass> list, int type) {
        for (ShipClass c :
                list) {
            if (c.getCtype() == type) {
                return true;
            }
        }
        return false;
    }

    private static void addToShipClassList(List<ShipClass> list, int type, String shipName) {
        if (!isShipClassExist(list, type)) {
            ShipClass shipClass = new ShipClass();
            shipClass.setCtype(type);

            switch (shipName) {
                case "Z1":
                    shipClass.setName("Zerstörer1934级");
                    break;
                case "Zara":
                case "Zara改":
                    shipClass.setName("扎拉级");
                    break;
                case "衣阿华":
                case "衣阿华改":
                    shipClass.setName("衣阿华级");
                    break;
                case "Bismarck":
                    shipClass.setName("俾斯麦级");
                    break;
                case "Graf Zeppelin":
                case "齐柏林":
                    shipClass.setName("齐柏林伯爵级");
                    break;
                case "Libeccio改":
                case "利伯齐奥":
                case "利伯齐奥改":
                    shipClass.setName("西北风级");
                    break;
                case "欧根亲王":
                case "Prinz Eugen":
                    shipClass.setName("希佩尔海军上将级");
                    break;
                case "Littorio":
                case "利托里奥":
                    shipClass.setName("维内托级");
                    break;
                case "伊168":
                    shipClass.setName("海大VI型");
                    break;
                case "伊58":
                    shipClass.setName("巡潜乙型改二");
                    break;
                case "伊19":
                    shipClass.setName("巡潜乙型");
                    break;
                case "伊8":
                    shipClass.setName("巡潜3型");
                    break;
                case "伊401":
                    shipClass.setName("潜特型");
                    break;
                case "U-511":
                    shipClass.setName("U型潜水艇IXC型");
                    break;
                case "丸输":
                    shipClass.setName("三式潜航输送艇");
                    break;
                case "秋津丸":
                    shipClass.setName("特种船丙型");
                    break;
                case "速吸":
                    shipClass.setName("风早级");
                    break;
                default:
                    shipClass.setName(shipName + "级");
            }

            list.add(shipClass);
            System.out.println("" + type + shipName);
        }
    }

    public static NewShip findById(int id, List<NewShip> ship) {
        for (NewShip item : ship) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
