package com.example;

import com.example.list.EquipList;
import com.example.model.APIShipType;
import com.example.model.AttrEntity;
import com.example.model.NewEquip;
import com.example.model.NewShip;
import com.example.model.Ship;
import com.example.model.Ship2;
import com.example.model.ShipClass;
import com.example.model.Start2;
import com.example.network.RetrofitAPI;
import com.example.utils.TextUtils;
import com.example.utils.Utils;
import com.example.utils.WanaKanaJava;
import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spreada.utils.chinese.ZHConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.utils.Utils.objectToJsonFile;

/**
 * Created by Rikka on 2016/9/17.
 */
public class ShipGenerator {

    private static final boolean USE_ONLINE_DATA = false;

    private static WanaKanaJava wkj = new WanaKanaJava(false);
    private static Start2 start2;
    private static List<ShipClass> shipClassList = new ArrayList<>();

    static {
        if (USE_ONLINE_DATA) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.kcwiki.moe")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitAPI.Start2Service service = retrofit.create(RetrofitAPI.Start2Service.class);
            try {
                start2 = service.get().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                start2 = new Gson().fromJson(new FileReader("datagenerator/api_start2.json"), Start2.class);
            } catch (FileNotFoundException ignored) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        for (Ship2 ship : getList()) {
            NewShip apiShip = getAPIShip(ship.getId());

            ship.getName().setJa(ship.get日文名().trim());
            ship.getName().setZh_cn(ship.get中文名().trim());
            ship.getName().setEn(getRomaji(ship).trim());
            ship.setNameForSearch(getNameForSearch(ship).trim());

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

        // 从 api_start2 中取得特殊的改造
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
            if (ship.getClassType() != 0) {
                addToShipClassList(shipClassList, ship.getClassType(), ship.get中文名());
            }
        }

        // 临时补充数据中暂无的
        getList().getByName("松风").setClassType(66);
        getList().getByName("松风").setClassNum(4);
        getList().getByName("松风改").setClassType(66);
        getList().getByName("松风改").setClassNum(4);

        getList().getByName("藤波").setClassType(38);
        getList().getByName("藤波").setClassNum(11);
        getList().getByName("藤波改").setClassType(38);
        getList().getByName("藤波改").setClassNum(11);

        shipClassList.add(new ShipClass("巡潜甲型改2", 999));

        getList().getByName("伊13").setClassType(999);
        getList().getByName("伊13").setClassNum(13);
        getList().getByName("伊13改").setClassType(999);
        getList().getByName("伊13改").setClassNum(13);

        getList().getByName("伊14").setClassType(999);
        getList().getByName("伊14").setClassNum(14);
        getList().getByName("伊14改").setClassType(999);
        getList().getByName("伊14改").setClassNum(14);

        for (Ship2 ship : getList()) {
            if (ship.getClassType() == 0) {
                System.out.println("No class: " + ship.get中文名());
            }
        }

        addExtraEquipType();

        for (Ship2 ship : getList()) {
            if (ship.getClassType() == 0 || ship.getClassNum() == 0) {
                System.out.println("No class / class num: " + ship.get中文名());
            }
        }

        addEnemyShip();

        for (Ship2 item : getList()) {
            item.getName().setZh_tw(ZHConverter.toTC(item.getName().getZh_cn()));
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
        Reader reader;
        if (USE_ONLINE_DATA) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://zh.kcwiki.moe/")
                    .build();

            RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
            ResponseBody body = service.getPage("模块:舰娘数据", "raw").execute().body();
            reader = body.charStream();
        } else {
            reader = new FileReader("datagenerator/ship.lua");
        }

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

        if (str.contains("\"Mist01")) {
            str = str.substring(0, str.indexOf("\"Mist01") - 1)
                    + str.substring(str.indexOf("\"wiki_id\":\"257a\"") - 2);
        }

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
        if (USE_ONLINE_DATA) {
            Gson gson = new GsonBuilder()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.kcwiki.moe")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RetrofitAPI.ShipService service = retrofit.create(RetrofitAPI.ShipService.class);
            return service.getDetail().execute().body();
        } else {
            return new Gson().fromJson(
                    new FileReader(new File("datagenerator/ships_detail.json")),
                    new TypeToken<List<NewShip>>() {
                    }.getType());
        }
    }

    private static String getRomaji(Ship2 item) {
        StringBuilder sb = new StringBuilder();
        if (item.get日文名().charAt(0) <= 'Z') {
            sb.append(item.get日文名().replace("改", ""));
        } else if (!TextUtils.isEmpty(item.get假名())) {
            sb.append(wkj.toRomaji(item.get假名()));
            sb.setCharAt(0, (char) (sb.charAt(0) + ('A' - 'a')));
        }

        if (item.get中文名().contains("改")) {
            sb.append(" Kai");
        }
        if (item.get中文名().contains("二")) {
            sb.append(" Ni");
        }
        if (item.get中文名().contains("甲")) {
            sb.append(" A");
        }
        if (item.get中文名().contains("乙")) {
            sb.append(" B");
        }
        if (item.get中文名().contains("丙")) {
            sb.append(" C");
        }
        if (item.get中文名().contains("丁")) {
            sb.append(" D");
        }
        return sb.toString();
    }
    private static String getNameForSearch(Ship2 item) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(item.get假名())) {
            sb.append(item.get日文名())
                    .append(',')
                    .append(item.get假名())
                    .append(',')
                    .append(getRomaji(item) /*wkj.toRomaji(item.get假名())*/);
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

        getList().getByName("翔鹤改二甲").getExtraEquipType().add(57); // 喷气战斗轰炸机
        getList().getByName("瑞鹤改二甲").getExtraEquipType().add(57);
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
                case "雪风":
                    shipClass.setName("阳炎级");
                    break;
                case "大井":
                    shipClass.setName("球磨级");
                    break;
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
                case "塔斯特司令官改":
                    shipClass.setName("C.Teste级");
                    break;
                case "萨拉托加":
                    shipClass.setName("列克星敦级");
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

    private static Pattern ENEMY_LINE = Pattern.compile("\\{\\{深海栖姬单条列表\\n  \\|(编号=[^}]*)}}");
    private static Pattern ENEMY_LINE_START = Pattern.compile("\\{\\{深海栖姬列表\\|日文名=(.+)\\|中文名=(.+)}}");

    private static void addEnemyShip() throws IOException {
        //getList()
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zh.kcwiki.moe/")
                .build();

        RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
        ResponseBody body = service.getPage("深海栖舰列表", "raw").execute().body();
        String str = body.string();

        Map<String, String> map = new HashMap<>();
        Matcher m;
        m = ENEMY_LINE_START.matcher(str);
        while (m.find()) {
            map.put(m.group(2).trim(), m.group(1).trim());
        }

        m = ENEMY_LINE.matcher(str);
        while (m.find()) {
            String line = m.group(1).replace("\n", "").replace("\r", "");

            if (line.contains("编号=mist")) {
                continue;
            }

            Ship2 ship = new Ship2();
            getList().add(ship);
            ship.setAttr(new AttrEntity());
            ship.setAttr_max(new AttrEntity());

            String[] groups = line.split("\\|");
            for (String kv : groups) {
                String key, value;
                String[] s = kv.split("=");
                key = s[0];
                if (s.length > 1) {
                    value = s[1];
                } else {
                    value = "";
                }
                parseEnemyShip(ship, map, key.trim(), value.trim());

                if (ship.getId() != 0 && ship.getType() == 0) {
                    NewShip newship = getAPIShip(ship.getId());
                    ship.setType(newship.getType());
                    ship.setEquip(new Ship.EquipEntity());
                    ship.getEquip().setSlots(newship.getStats().getSlot_num());
                    ship.getEquip().setId(new int[ship.getEquip().getSlots()]);
                    ship.getEquip().setSpace(new int[ship.getEquip().getSlots()]);
                }
            }
        }
    }

    private static void parseEnemyShip(Ship2 ship, Map<String, String> map, String key, String value) {
        switch (key) {
            case "编号":
                ship.setId(Utils.stringToInt(value));
                ship.setWikiId(value);
                break;
            case "名字":
                ship.getName().setZh_cn(value);
                ship.getName().setJa(map.get(value));
                break;
            case "级别":
                ship.getName().setZh_cn(ship.getName().getZh_cn() + " " + value);
                ship.getName().setJa(ship.getName().getJa() + " " + value);
                break;

            case "耐久":
                ship.getAttr().setHP(Utils.stringToInt(value));
                break;

            case "火力":
                ship.getAttr().setFire(Utils.stringToInt(value));
                break;
            case "火力2":
                ship.getAttrMax().setFire(Utils.stringToInt(value) - ship.getAttr().getFirepower());
                break;

            case "雷装":
                ship.getAttr().setTorpedo(Utils.stringToInt(value));
                break;
            case "雷装2":
                ship.getAttrMax().setTorpedo(Utils.stringToInt(value) - ship.getAttr().getTorpedo());
                break;

            case "对空":
                ship.getAttr().setAA(Utils.stringToInt(value));
                break;
            case "对空2":
                ship.getAttrMax().setAA(Utils.stringToInt(value) - ship.getAttr().getAA());
                break;

            case "装甲":
                ship.getAttr().setArmor(Utils.stringToInt(value));
                break;
            case "装甲2":
                ship.getAttrMax().setArmor(Utils.stringToInt(value) - ship.getAttr().getArmor());
                break;

            case "运":
                ship.getAttr().setLuck(Utils.stringToInt(value));
                break;

            case "射程":
                switch (value) {
                    case "短":
                        ship.getAttr().setRange(1);
                        break;
                    case "中":
                        ship.getAttr().setRange(2);
                        break;
                    case "长":
                        ship.getAttr().setRange(3);
                        break;
                    case "超长":
                        ship.getAttr().setRange(4);
                        break;
                }
                break;

            case "装备1":
            case "装备2":
            case "装备3":
            case "装备4":
            case "装备5":
                if (TextUtils.isEmpty(value)) {
                    break;
                }

                int slot = key.charAt(2) - '1';
                NewEquip equip = EquipList.findByName(value);
                if (equip == null) {
                    int id = 0;
                    switch (value) {
                        case "22英寸鱼雷后期型":
                            id = 515;
                            break;
                        case "16英寸三连装炮(新)":
                            id = 568;
                            break;
                    }
                    ship.getEquip().getId()[slot] = id;
                } else {
                    if (slot < ship.getEquip().getId().length)
                        ship.getEquip().getId()[slot] = equip.getId();
                }
                break;

            case "搭载1":
            case "搭载2":
            case "搭载3":
            case "搭载4":
            case "搭载5":
                if (TextUtils.isEmpty(value)) {
                    break;
                }

                int slot2 = key.charAt(2) - '1';
                if (slot2 < ship.getEquip().getSpace().length)
                    ship.getEquip().getSpace()[slot2] = Utils.stringToInt(value.replace("?", ""));
                break;
        }
    }
}
