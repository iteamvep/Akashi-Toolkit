package com.example;

import com.example.list.ShipForm;
import com.example.model.MultiLanguageEntry;
import com.example.model.NewShip;
import com.example.model.ShipClass;
import com.example.network.RetrofitAPI;
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

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

            if (item.getStats().getFirepower() != null) {
                NewShip.AttrEntity attr = item.getAttr();
                NewShip.AttrEntityMax attr_max = item.getAttrMax();

                attr.setArmor(item.getStats().getArmor().get(0));
                attr_max.setArmor(item.getStats().getArmor().get(1) - item.getStats().getArmor().get(0));

                attr.setFirepower(item.getStats().getFirepower().get(0));
                attr_max.setFirepower(item.getStats().getFirepower().get(1) - item.getStats().getFirepower().get(1));

                attr.setTorpedo(item.getStats().getTorpedo().get(0));
                attr_max.setTorpedo(item.getStats().getTorpedo().get(1) - item.getStats().getTorpedo().get(0));

                attr.setAA(item.getStats().getAA().get(0));
                attr_max.setAA(item.getStats().getAA().get(1) - item.getStats().getAA().get(0));

                attr.setLuck(item.getStats().getLuck().get(0));
                attr_max.setLuck(item.getStats().getLuck().get(1) - item.getStats().getLuck().get(0));

                attr.setSpeed(item.getStats().getSpeed());
                attr.setRange(item.getStats().getRange());
            }

            //System.out.println(item.getStats() + "  " + item.getStats().getFirepower());

            item.setNameForSearch(getNameForSearch(item));
        }

        // 不要名字为空或者带有“纪念日”的
        List<NewShip> list2 = list
                .stream()
                .filter(item -> !TextUtils.isEmpty(item.getNameJP()) && !item.getNameJP().contains("記念日") && !item.getNameJP().contains("梅雨") && !item.getNameJP().contains("携帯"))
                .collect(Collectors.toList());

        for (NewShip item : list2) {
            int[] row = ShipForm.getIntRowById(item.getId());
            String[] row_string = ShipForm.getRowById(item.getId());

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

                if (item.getAttr().getHP() == 0) {
                    NewShip.AttrEntity attr = item.getAttr();
                    NewShip.AttrEntityMax attr_max = item.getAttrMax();
                    NewShip.AttrEntity99 attr99 = item.getAttr99();
                    NewShip.AttrEntity99 attr155 = item.getAttr155();

                    attr.setHP(row[COLUMN_HP]);
                    attr155.setHP(row[COLUMN_HP_WEDDING] - row[COLUMN_HP]);

                    attr.setAA(row[COLUMN_AA]);
                    attr_max.setAA(row[COLUMN_AA_MAX] - row[COLUMN_AA]);

                    attr.setFirepower(row[COLUMN_FIRE]);
                    attr_max.setFirepower(row[COLUMN_FIRE_MAX] - row[COLUMN_FIRE]);

                    attr.setTorpedo(row[COLUMN_TORPEDO]);
                    attr_max.setTorpedo(row[COLUMN_TORPEDO_MAX] - row[COLUMN_TORPEDO]);

                    attr.setArmor(row[COLUMN_ARMOR]);
                    attr_max.setArmor(row[COLUMN_ARMOR_MAX] - row[COLUMN_ARMOR]);

                    attr.setLuck(row[COLUMN_LUCK]);
                    attr_max.setLuck(row[COLUMN_LUCK_MAX] - row[COLUMN_LUCK]);

                    attr.setSpeed(row[COLUMN_SPEED]);
                    attr.setRange(row[COLUMN_RANGE]);
                }

                NewShip.AttrEntity attr = item.getAttr();
                NewShip.AttrEntity99 attr99 = item.getAttr99();
                NewShip.AttrEntity99 attr155 = item.getAttr155();

                attr.setHP(row[COLUMN_HP]);
                attr155.setHP(row[COLUMN_HP_WEDDING] - row[COLUMN_HP]);

                attr.setASW(row[COLUMN_ASW]);
                attr99.setASW(row[COLUMN_ASW_MAX] - row[COLUMN_ASW]);
                attr155.setASW(row[COLUMN_ASW_MAX2] - row[COLUMN_ASW_MAX]);

                attr.setLOS(row[COLUMN_LOS]);
                attr99.setLOS(row[COLUMN_LOS_MAX] - row[COLUMN_LOS]);
                attr155.setLOS(row[COLUMN_LOS_MAX2] - row[COLUMN_LOS_MAX]);

                attr.setEvasion(row[COLUMN_EVASION]);
                attr99.setEvasion(row[COLUMN_EVASION_MAX] - row[COLUMN_EVASION]);
                attr155.setEvasion(row[COLUMN_EVASION_MAX2] - row[COLUMN_EVASION_MAX]);
            } else {
                if (item.getAttr().getHP() == 0) {
                    NewShip.AttrEntity attr = item.getAttr();
                    attr.setHP(row[COLUMN_HP]);
                    attr.setAA(row[COLUMN_AA]);
                    attr.setFirepower(row[COLUMN_FIRE]);
                    attr.setTorpedo(row[COLUMN_TORPEDO]);
                    attr.setArmor(row[COLUMN_ARMOR]);
                    //attr.setLuck(row[COLUMN_LUCK], row[COLUMN_LUCK_MAX]);
                    //attr.setASW(row[COLUMN_ASW], row[COLUMN_ASW_MAX], row[COLUMN_ASW_MAX]);
                    //attr.setLOS(row[COLUMN_LOS], row[COLUMN_LOS_MAX], row[COLUMN_LOS_MAX]);
                    //attr.setEvasion(row[COLUMN_EVASION], row[COLUMN_EVASION_MAX], row[COLUMN_EVASION_MAX]);
                    attr.setSpeed(row[COLUMN_SPEED]);
                    attr.setRange(row[COLUMN_RANGE]);
                }

                // TODO 用来搜索的名字需要改
                if (row_string != null && !row_string[COLUMN_READING].equals("-"))
                    item.getName().setJa(String.format("%s %s", item.getNameJP(), row_string[COLUMN_READING]));
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

        addExtraEquipType(list2);

        String str = gson.toJson(list2).replace("\"name2\"", "\"name\"");

        objectToJsonFile(str, "app/src/main/assets/Ship.json");
        objectToJsonFile(shipClassList, "app/src/main/assets/ShipClass.json");
    }

    private static void addExtraEquipType(List<NewShip> list) {

        findByName("大和", list).getExtraEquipType().add(38); // 大口径主砲(II)
        findByName("大和改", list).getExtraEquipType().add(38);
        findByName("武藏", list).getExtraEquipType().add(38);
        findByName("武藏改", list).getExtraEquipType().add(38);
        findByName("长门改", list).getExtraEquipType().add(38);
        findByName("陆奥改", list).getExtraEquipType().add(38);

        findByName("波拉改", list).getExtraEquipType().add(11); // 水上爆撃機
        findByName("扎拉改", list).getExtraEquipType().add(11);

        findByName("波拉改", list).getExtraEquipType().add(45); // 水上戦闘機
        findByName("扎拉改", list).getExtraEquipType().add(45);

        findByName("霞改二乙", list).getExtraEquipType().add(13); // 大型電探

        findByName("霞改二", list).getExtraEquipType().add(34); // 司令部施設

        findByName("阿武隈", list).getExtraEquipType().add(22); // 特殊潜航艇

        findByName("阿武隈", list).getExtraEquipType().add(24); // 上陸用舟艇
        findByName("江风改二", list).getExtraEquipType().add(24);
        findByName("大潮改二", list).getExtraEquipType().add(24);
        findByName("霞改二", list).getExtraEquipType().add(24);
        findByName("Верный", list).getExtraEquipType().add(24);
        findByName("朝潮改二丁", list).getExtraEquipType().add(24);
        findByName("霞改二乙", list).getExtraEquipType().add(24);
        findByName("睦月改二", list).getExtraEquipType().add(24);
        findByName("如月改二", list).getExtraEquipType().add(24);
        findByName("皋月改二", list).getExtraEquipType().add(24);

        findByName("朝潮改二丁", list).getExtraEquipType().add(46); // 特型内火艇
        findByName("阿武隈", list).getExtraEquipType().add(46);
        findByName("霞改二乙", list).getExtraEquipType().add(46);
        findByName("霞改二", list).getExtraEquipType().add(46);
        findByName("大潮改二", list).getExtraEquipType().add(46);
        findByName("Верный", list).getExtraEquipType().add(46);
        findByName("皋月改二", list).getExtraEquipType().add(46);

        findByName("Верный", list).getExtraEquipType().add(46); // 追加装甲(中型)
    }

    private static List<NewShip> getShipList() throws IOException {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kcwiki.moe")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitAPI.ShipService service = retrofit.create(RetrofitAPI.ShipService.class);
        return service.getDetail().execute().body();
        /*return new Gson().fromJson(
                new FileReader(new File("datagenerator/ships_detail.json")),
                new TypeToken<List<NewShip>>() {}.getType());*/
    }

    private static String getNameForSearch(NewShip item) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(item.getYomi())) {
            sb.append(item.getNameJP())
                    .append(',')
                    .append(item.getYomi())
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
                if (cur.getRemodel() == null) {
                    System.out.println(cur.getNameCN());
                } else {
                    while (cur.getRemodel().getFromId() != 0) {
                        cur = findById(cur.getRemodel().getFromId(), list);
                    }
                }

                ship.setClassType(cur.getClassType());
                ship.setClassNum(cur.getClassNum());

                //System.out.println(ship.getName().getZh_cn() + " 舰级为空 设定为了和 " + cur.getName().getZh_cn() + " 一样的类型");
            }
        }

        for (NewShip ship : list) {
            if (ship.getId() > 500) {
                break;
            }

            if (ship.getClassType() == 0) {
                //System.out.println(ship.getName().getZh_cn() + " 没有类型");
            } else if (!isShipClassExist(shipClassList, ship.getClassType())) {
                //System.out.println(ship.getName().getZh_cn() + " 没有一号舰");
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

    public static NewShip findByName(String name, List<NewShip> ship) {
        for (NewShip item : ship) {
            if (name.equals(item.getNameCN()) || name.equals(item.getNameJP())) {
                return item;
            }
        }
        throw new IllegalArgumentException(name + " not found");
    }
}
