package com.example;

import com.example.model.*;
import com.example.list.*;
import com.example.utils.Utils;
import com.spreada.utils.chinese.ZHConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.utils.Utils.*;

public class QuestGenerator {
    public static void main(String[] args) throws IOException {
        List<Quest> list = new ArrayList<>();
        for (String lineStr : getLines()) {
            try {
                String[] kv = lineStr.split("\\|");

                if (kv.length < 1) {
                    System.out.println("Skipped line: " + lineStr);
                    continue;
                }

                Quest item = new Quest();
                item.setId(list.size());
                item.setNewMission(isNew);

                for (String text : kv) {
                    String temp[] = text.split("=");
                    String value = "";
                    if (temp.length == 2) {
                        value = temp[1].trim();
                    } else if (temp.length > 2) {
                        for (int i = 1; i < temp.length; i++) {
                            value = value + temp[i];
                        }
                    }
                    parse(item, temp[0].trim(), value);
                }

                if (isEnd) {
                    break;
                }

                list.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Quest quest : list) {
            if (quest.getUnlock() == null)
                continue;

            for (String id : quest.getUnlock()) {
                Quest before = QuestList.findByCode(id, list);

                if (before == null) {
                    continue;
                }

                List<String> after = before.getAfter();
                if (after == null) {
                    after = new ArrayList<>();
                    before.setAfter(after);
                }

                boolean shouldAdd = true;
                for (String code : after) {
                    if (code.equals(quest.getCode())) {
                        shouldAdd = false;
                        break;
                    }
                }

                if (shouldAdd)
                    after.add(quest.getCode());
            }
        }
            
        
        for (Quest item : list) {
            item.getContent().setZh_tw(ZHConverter.toTC(item.getContent().getZh_cn()));
            item.getTitle().setZh_tw(ZHConverter.toTC(item.getTitle().getZh_cn()));
        }

        objectToJsonFile(list, "L:/NetBeans/NetBeansProjects/Akashi-Toolkit/src/json/Quest.json");
        objectToJsonFile(mItemList, "L:/NetBeans/NetBeansProjects/Akashi-Toolkit/src/json/Item.json");
    }

    private static List<String> getLines() throws IOException {
        System.out.println("getInputStream..");
        String originStr = Utils.streamToString(getUrlStream("https://zh.kcwiki.org/index.php?title=%E4%BB%BB%E5%8A%A1&action=raw"));
        System.out.println("finished..");
        originStr = originStr.replaceAll("\n", "").replaceAll("\r", "");

        Pattern r = Pattern.compile("\\{\\{任务表\\|(.*?备注.*?)\\}\\}");
        Matcher m = r.matcher(originStr);

        List<String> lineStrList = new ArrayList<>();

        while (m.find()) {
            String text = m.group(1);
            Matcher m2;
            m2 = Pattern.compile("\\[\\[([^]]+)\\|([^]]+)\\]\\]").matcher(text);

            while (m2.find()) {
                text = text.replace(m2.group(), "[[" + m2.group(1) + "&&&" + m2.group(2) + "]]");
            }

            m2 = Pattern.compile("\\{\\{([^{|]+)\\|([^}|]+)\\}\\}").matcher(text);

            while (m2.find()) {
                text = text.replace(m2.group(), "{{" + m2.group(1) + "&&&" + m2.group(2) + "}}");
            }

            lineStrList.add(text);
        }

        return lineStrList;
    }

    private static boolean isNew = true;
    private static boolean isEnd = false;
    private static int mCurPeriod = 0;

    private static String PERIOD_D_STR = "Bd1";
    private static int PERIOD_D = 1;
    private static String PERIOD_W_STR = "Bw1";
    private static int PERIOD_W = 2;
    private static String PERIOD_M_STR = "Bm1";
    private static int PERIOD_M = 3;

    private static void parse(Quest quest, String key, String value) {
        switch (key) {
            case "编号":
                quest.setCode(value);
                if (value.equals("A1")) {
                    isNew = false;
                    quest.setNewMission(false);
                }

                if (value.equals("MB01")) {
                    isEnd = true;
                    break;
                }

                if (value.equals(PERIOD_D_STR)) {
                    mCurPeriod = PERIOD_D;
                }
                if (value.equals(PERIOD_W_STR)) {
                    mCurPeriod = PERIOD_W;
                }
                if (value.equals(PERIOD_M_STR)) {
                    mCurPeriod = PERIOD_M;
                }
                quest.setPeriod(mCurPeriod);

                char type = value.charAt(0);
                if (value.charAt(0) == 'W') {
                    type = value.charAt(1);
                }

                quest.setType(type - 'A' + 1);

                System.out.println(value);

                break;
            case "前置":
            case "前置2":
            case "前置3":
                if (value.equals("bm1")) {
                    value = "Bm1";
                }
                if (value.length() > 0) {
                    quest.getUnlock().add(value);
                }
                break;
            case "中文任务名字":
                quest.getTitle().setZh_cn(value);
                break;
            case "日文任务名字":
                quest.getTitle().setJa(value);
                break;
            case "日文任务说明":
                quest.getContent().setJa(value);
                break;
            case "中文任务说明":
                quest.getContent().setZh_cn(simpleParse(value));
                break;
            case "燃料":
                quest.getReward().getResource().add(stringToInt(value));
                break;
            case "弹药":
                quest.getReward().getResource().add(stringToInt(value));
                break;
            case "钢铁":
                quest.getReward().getResource().add(stringToInt(value));
                break;
            case "铝":
                quest.getReward().getResource().add(stringToInt(value));
                break;
            case "备注":
                quest.setNote(simpleParse(value));
                break;
            case "奖励":
                parseReward(quest, value);
                break;
            default:
                break;
        }
    }

    private static String simpleParse(String in) {
        return in.replaceAll("\\[\\[\\d\\-\\d&&&", "<b>").replace("[[", "<b>").replace("]]", "</b>");
    }

    private static List<Item> mItemList = new ArrayList<>();

    private static void parseReward(Quest quest, String value) {
        value = value.replace("<br />", "<br/>");
        value = value.replace("<br/>", "\n");
        value = value.replace("[[试制甲板用弹射器]]", "{{试制甲板用弹射器}}");
        value = value.replace("{{菱饼}}", "{{菱饼}}*1");
        value = value.replace("[[试制61cm六连装(酸素)鱼雷]]", "[[试制61cm六连装(酸素)鱼雷]]*1");
        value = value.replace("[[试制35.6cm三连装炮]]", "[[试制35.6cm三连装炮]]*1");
        value = value.replace("[[试制35.6cm三连装炮]]*1*1", "[[试制35.6cm三连装炮]]*1");
        value = value.replace("[[大发动艇]]", "[[大发动艇]]*1");
        value = value.replace("[[大发动艇]]*1*1", "[[大发动艇]]*1");
        value = value.replace("[[九六式陆攻]]", "[[九六式陆攻]]*1");
        value = value.replace("[[九六式陆攻]]*1*1", "[[九六式陆攻]]*1");
        value = value.replace("[[一式陆攻]]", "[[一式陆攻]]*1");
        value = value.replace("[[一式陆攻]]*1*1", "[[一式陆攻]]*1");
        value = value.replace("[[一式陆攻二二型甲]]", "[[一式陆攻二二型甲]]*1");
        value = value.replace("[[一式陆攻二二型甲]]*1*1", "[[一式陆攻二二型甲]]*1");

        Pattern r;
        Matcher m;
        List<Integer> list;

        // Equip
        r = Pattern.compile("\\[\\[([^#\\]]+)\\]\\]\\*(\\d+)");
        m = r.matcher(value);

        list = new ArrayList<>();
        while (m.find()) {
            value = value.replace(m.group(), "");

            String name = m.group(1).replace('（', '(').replace('）', ')').trim();
            NewEquip equip = EquipList.findByName(name);
            if (equip != null) {
                list.add(equip.getId());
                list.add(stringToInt(m.group(2)));
                //System.out.println("Equip: " + name + "*" + m.group(2));
            } else {
                System.out.println("Equip not found: " + m.group(1));
            }
        }

        quest.getReward().setEquip(list);

        // Ship
        r = Pattern.compile("\\[\\[文件.+&&&link(.+)\\]\\]");
        m = r.matcher(value);

        list = new ArrayList<>();
        while (m.find()) {
            value = value.replace(m.group(), "");

            Ship item = ShipList.findByName(m.group(1).trim());
            if (item != null) {
                list.add(item.getId());
                //System.out.println("Ship: " + item.getName().getZh_cn());
            }
        }

        quest.getReward().setShip(list);

        // Other
        r = Pattern.compile("\\{\\{([^}]+)\\}\\}\\*(\\d+)");
        m = r.matcher(value);

        list = new ArrayList<>();
        while (m.find()) {
            value = value.replace(m.group(), "");

            String name = m.group(1).trim();
            Item item = ItemList.findByName(name, mItemList);
            if (item == null) {
                mItemList.add(new Item(name, mItemList.size()));
                //System.out.println("Other: " + name + "*" + m.group(2));
                item = ItemList.findByName(name, mItemList);
            }

            list.add(item.getId());
            list.add(stringToInt(m.group(2)));
        }

        quest.getReward().setItem(list);

        value = value.replace(" ", "");
        if (value.length() > 1) {
            value = value.replace("\n", "").replace("&&&", "|").replace("'''", "");
            quest.getReward().setStr(value);
            System.out.println("rest to parse " + value);
        }
    }
}