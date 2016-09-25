package com.example;

import com.example.model.AttrEntity;
import com.example.model.NewShip;
import com.example.model.Ship2;
import com.example.model.ShipClass;
import com.example.model.Start2;
import com.example.network.RetrofitAPI;
import com.example.utils.TextUtils;
import com.example.utils.WanaKanaJava;
import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.utils.Utils.objectToJsonFile;

/**
 * Created by Rikka on 2016/9/17.
 */
public class ShipGenerator {
    private static WanaKanaJava wkj = new WanaKanaJava(false);
    private static Start2 start2;
    private static List<ShipClass> shipClassList = new ArrayList<>();

    static {
        try {
            start2 = new Gson().fromJson(new FileReader("datagenerator/api_start2.json"), Start2.class);
        } catch (FileNotFoundException ignored) {
        }
    }

    public static void main(String[] args) throws IOException {
        for (Ship2 ship : getList()) {
            NewShip apiShip = getAPIShip(ship.getId());

            ship.getName().setJa(ship.get日文名());
            ship.getName().setZh_cn(ship.get中文名());
            ship.setNameForSearch(getNameForSearch(ship));

            ship.setClassNum(apiShip.getClassNum());
            ship.setClassType(apiShip.getClassType());

            ship.setRarity(ship.get数据().get稀有度());
            ship.setBuildTime(ship.get获得().getBuildTime());

            ship.setBrokenResources(new int[]{ship.get解体().get燃料(), ship.get解体().get弹药(), ship.get解体().get铝(), ship.get解体().get铝()});
            ship.setModernizationBonus(new int[]{ship.get改修().get火力(), ship.get改修().get雷装(), ship.get改修().get对空(), ship.get改修().get装甲()});

            ship.setResourceConsume(new int[]{ship.get消耗().get燃料(), ship.get消耗().get弹药()});

            AttrEntity attr;
            attr = new AttrEntity();
            ship.setAttr(attr);
            attr.setHP(ship.get数据().get耐久().get(0));
            attr.setAA(ship.get数据().get对空().get(0));
            attr.setArmor(ship.get数据().get装甲().get(0));
            attr.setASW(ship.get数据().get对潜().get(0));
            attr.setEvasion(ship.get数据().get回避().get(0));
            attr.setFire(ship.get数据().get火力().get(0));
            attr.setLOS(ship.get数据().get索敌().get(0));
            attr.setLuck(ship.get数据().get运().get(0));
            attr.setTorpedo(ship.get数据().get雷装().get(0));
            attr.setRange(ship.get数据().get射程());
            attr.setSpeed(ship.get数据().get速力());

            attr = new AttrEntity();
            ship.setAttr_max(attr);

            attr.setAA(ship.get数据().get耐久().get(1) - ship.get数据().get耐久().get(0));
            attr.setAA(ship.get数据().get对空().get(1) - ship.get数据().get对空().get(0));
            attr.setArmor(ship.get数据().get装甲().get(1) - ship.get数据().get装甲().get(0));
            attr.setFire(ship.get数据().get火力().get(1) - ship.get数据().get火力().get(0));
            attr.setTorpedo(ship.get数据().get雷装().get(1) - ship.get数据().get雷装().get(0));
            attr.setLuck(ship.get数据().get运().get(1) - ship.get数据().get运().get(0));

            attr = new AttrEntity();
            ship.setAttr_99(attr);

            attr.setASW(ship.get数据().get对潜().get(1) - ship.get数据().get对潜().get(0));
            attr.setEvasion(ship.get数据().get回避().get(1) - ship.get数据().get回避().get(0));
            attr.setLOS(ship.get数据().get索敌().get(1) - ship.get数据().get索敌().get(0));

            Ship2.RemodelEntity remodel = new Ship2.RemodelEntity();
            remodel.setToId(apiShip.getAfter_ship_id());
            remodel.setLevel(apiShip.getAfter_lv());
            remodel.setCost(new int[]{ship.getAfter_bull(), ship.getAfter_fuel()});
            ship.setRemodel(remodel);
        }

        for (Ship2 ship : getList()) {
            for (Start2.ApiMstShipupgradeEntity entity : start2.getApi_mst_shipupgrade()) {
                if (entity.getApi_current_ship_id() == ship.getId()) {
                    if (entity.getApi_drawing_count() >= 1) {
                        ship.getRemodel().setRequireBlueprint(true);
                    }

                    if (entity.getApi_catapult_count() >= 1) {
                        ship.getRemodel().setCatapult(true);
                    }
                }
            }

            if (ship.getRemodel().getToId() == 0) {
                continue;
            }

            Ship2 to = getList().getById(ship.getRemodel().getToId());
            if (to.getRemodel().getFromId() == 0) {
                to.getRemodel().setFromId(ship.getId());
            }
        }

        for (Ship2 ship : getList()) {
            if (ship.getClassType() == 0) {
                Ship2 s = ship;
                while (s.getRemodel().getFromId() != 0) {
                    s = getList().getById(s.getRemodel().getFromId());
                }

                ship.setClassNum(s.getClassNum());
                ship.setClassType(s.getClassType());
            }
            addToShipClassList(shipClassList, ship.getClassType(), ship.get中文名());
        }

        addExtraEquipType();

        for (Ship2 ship : getList()) {
            if (ship.getClassType() == 0 || ship.getClassNum() == 0) {
                System.out.println(ship.get中文名());
            }
        }

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        String str = gson.toJson(getList());

        objectToJsonFile(str, "app/src/main/assets/Ship.json");
        objectToJsonFile(shipClassList, "app/src/main/assets/ShipClass.json");
    }

    private static ShipList sShipList;

    private static ShipList getList() throws IOException {
        if (sShipList != null) {
            return sShipList;
        }
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zh.kcwiki.moe/")
                .build();

        RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
        ResponseBody body = service.getPage("模块:舰娘数据", "raw").execute().body();
        Reader reader = body.charStream();*/
        Reader reader = new FileReader("datagenerator/ship.lua");

        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (int c = reader.read(); c != -1; c = reader.read()) {
            if ((char) c == '{') {
                count++;
            }

            if (count > 0) {
                sb.append((char) c);
            }

            if ((char) c == '}') {
                count--;
            }
        }

        String str = sb.toString()
                .substring(2);

        reader = new StringReader(str);
        sb = new StringBuilder();
        boolean skipSpace = true;
        for (int c = reader.read(); c != -1; c = reader.read()) {
            if (c == '"') {
                skipSpace = !skipSpace;
            }

            if (c != ' ' || !skipSpace) {
                sb.append((char) c);
            }
        }

        str = sb.toString()
                .replace("\r\n", "\n")
                .replace("\r", "")
                .replace("\t", "")

                .replaceAll("\\[\"(\\d+a?)\"]=\\{", "{\n[\"id\"]=\"$1\",")
                .replaceAll("\\[\"([^]]+)\"\\]=", "\"$1\":")
                .replaceAll("\"([^\"]+)\":\\{([^:=\\}]+)\\}", "\"$1\":[$2]");

        if (str.charAt(str.length() - 3) == ',') {
            str = "[" + str.substring(1, str.length() - 3) + "\n]";
        } else {
            str = "[" + str.substring(1, str.length() - 1) + "]";
        }

        str = str.replace("\"id\"", "\"wiki_id\"")
                .replace("\"ID\"", "\"id\"")
                .replace("\"舰种\"", "\"stype\"")

                .replace("\"装备\"", "\"equip\"")
                .replace("\"格数\"", "\"slots\"")
                .replace("\"搭载\"", "\"space\"")
                .replace("\"初期装备\"", "\"id\"")

                .replace("\"画师\"", "\"painter\"")
                .replace("\"声优\"", "\"cv\"")

                .replace("\"获得\"", "\"get\"")
                .replace("\"掉落\"", "\"drop\"")
                //.replace("\"改造\"", "\"remodel2\"")
                .replace("\"建造\"", "\"build\"")
                .replace("\"时间\"", "\"build_time\"");

        str = str.replace("\"space\":{}", "\"space\":[]")
                .replace("\"id\":{}", "\"id\":[]");

        sShipList = new Gson().fromJson(str, ShipList.class);
        return sShipList;
    }

    private static List<NewShip> sApiShipList;

    private static NewShip getAPIShip(int id) throws IOException {
        if (sApiShipList == null) {
            sApiShipList = getAPIShipList();
        }

        for (NewShip item : sApiShipList) {
            if (item.getId() == id) {
                return item;
            }
        }

        throw new NullPointerException("id " + id);
    }

    private static List<NewShip> getAPIShipList() throws IOException {
        /*Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kcwiki.moe")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitAPI.ShipService service = retrofit.create(RetrofitAPI.ShipService.class);
        return service.getDetail().execute().body();*/
        return new Gson().fromJson(
                new FileReader(new File("datagenerator/ships_detail.json")),
                new TypeToken<List<NewShip>>() {
                }.getType());
    }

    private static String getNameForSearch(Ship2 item) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(item.get假名())) {
            sb.append(item.get日文名())
                    .append(',')
                    .append(item.get假名())
                    .append(',')
                    .append(wkj.toRomaji(item.get假名()));
        }

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ',') {
            sb.append(',');
        }

        if (!TextUtils.isEmpty(item.get中文名())) {
            sb.append(item.get中文名())
                    .append(',');

            for (char c : item.get中文名().toCharArray()) {
                if (Pinyin.isChinese(c)) {
                    sb.append(Pinyin.toPinyin(c).toLowerCase());
                }
            }
        }

        return sb.toString();
    }

    private static void addExtraEquipType() throws IOException {

        getList().getByName("大和").getExtraEquipType().add(38); // 大口径主砲(II)
        getList().getByName("大和改").getExtraEquipType().add(38);
        getList().getByName("武藏").getExtraEquipType().add(38);
        getList().getByName("武藏改").getExtraEquipType().add(38);
        getList().getByName("长门改").getExtraEquipType().add(38);
        getList().getByName("陆奥改").getExtraEquipType().add(38);

        getList().getByName("波拉改").getExtraEquipType().add(11); // 水上爆撃機
        getList().getByName("扎拉改").getExtraEquipType().add(11);

        getList().getByName("波拉改").getExtraEquipType().add(45); // 水上戦闘機
        getList().getByName("扎拉改").getExtraEquipType().add(45);

        getList().getByName("霞改二乙").getExtraEquipType().add(13); // 大型電探

        getList().getByName("霞改二").getExtraEquipType().add(34); // 司令部施設

        getList().getByName("阿武隈").getExtraEquipType().add(22); // 特殊潜航艇

        getList().getByName("阿武隈").getExtraEquipType().add(24); // 上陸用舟艇
        getList().getByName("江风改二").getExtraEquipType().add(24);
        getList().getByName("大潮改二").getExtraEquipType().add(24);
        getList().getByName("霞改二").getExtraEquipType().add(24);
        getList().getByName("Верный").getExtraEquipType().add(24);
        getList().getByName("朝潮改二丁").getExtraEquipType().add(24);
        getList().getByName("霞改二乙").getExtraEquipType().add(24);
        getList().getByName("睦月改二").getExtraEquipType().add(24);
        getList().getByName("如月改二").getExtraEquipType().add(24);
        getList().getByName("皋月改二").getExtraEquipType().add(24);

        getList().getByName("朝潮改二丁").getExtraEquipType().add(46); // 特型内火艇
        getList().getByName("阿武隈").getExtraEquipType().add(46);
        getList().getByName("霞改二乙").getExtraEquipType().add(46);
        getList().getByName("霞改二").getExtraEquipType().add(46);
        getList().getByName("大潮改二").getExtraEquipType().add(46);
        getList().getByName("Верный").getExtraEquipType().add(46);
        getList().getByName("皋月改二").getExtraEquipType().add(46);

        getList().getByName("Верный").getExtraEquipType().add(46); // 追加装甲(中型)
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
                case "厌战":
                    shipClass.setName("伊丽莎白女王级");
                    break;
                default:
                    shipClass.setName(shipName + "级");
            }

            list.add(shipClass);
        }
    }

    private static class ShipList extends ArrayList<Ship2> {

        public Ship2 getById(int id) {
            for (Ship2 item : this) {
                if (item.getId() == id) {
                    return item;
                }
            }
            return null;
        }

        public Ship2 getByName(String name) {
            for (Ship2 item : this) {
                if (item.get中文名().equals(name) || item.get日文名().equals(name)) {
                    return item;
                }
            }
            return null;
        }
    }
}
