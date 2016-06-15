package com.example;

import com.example.model.Expedition;
import com.example.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.utils.Utils.getUrlStream;
import static com.example.utils.Utils.objectToJsonFile;

/**
 * Created by Rikka on 2016/6/15.
 */
public class ExpeditionGenerator {
    private static List<Expedition> list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("getInputStream..");
        String originStr = Utils.streamToString(getUrlStream("http://zh.kcwiki.moe/index.php?title=远征列表&action=raw"));
        System.out.println("finished..");

        originStr = originStr.replaceAll("<span style=\"color:#f00\">([^<]*)</span>", "<b>$1</b>").replaceAll("\\s*=\\s*", "=");
        originStr = originStr.replace("<b>请参见</b><br />[[经验值和头衔#远征32:远洋练习航海经验|'''远征32相关''']]", "(基础经验值＋僚舰加成) ×等级补正");
        getReward(originStr);
        getRequire(originStr);

        for (Expedition e : list) {
            System.out.println(e);
        }

        objectToJsonFile(list, "app/src/main/assets/Expdition.json");
    }

    private static void getReward(String originStr) throws IOException {
        Pattern r = Pattern.compile("\\{\\{远征报酬表\\|编号 =(\\d+)\\|日文名字 =(.+)\\|中文名字 =(.+)\\|耗时 =(.*\\d+:\\d+.*)\\|提督经验值 =(\\d*)\\|舰娘经验值 =(.*)\\|燃料 =(.*)\\|弹药 =(.*)\\|钢铁 =(.*)\\|铝 =([^\\|\\}]*)(\\|奖励 = ([^\\|]*))?(\\|大成功奖励 = (.*))?\\}\\}".replace(" ", ""));
        Matcher m = r.matcher(originStr);

        while (m.find()) {
            boolean skip = false;
            for (Expedition i :
                    list) {
                if (i.getId() == Integer.parseInt(m.group(1))) {
                    skip = true;
                    break;
                }
            }

            if (skip) {
                continue;
            }

            Expedition item = new Expedition();

            item.setId(m.group(1));
            item.getName().setJa(m.group(2));
            item.getName().setZh_cn(m.group(3));
            item.setTime(getTime(m.group(4)));
            item.setTimeString(m.group(4));
            item.getReward().setPlayerXP(m.group(5));
            item.getReward().setShipXP(m.group(6));
            item.getReward().getResourceString().add(rewardResource(m.group(7)));
            item.getReward().getResourceString().add(rewardResource(m.group(8)));
            item.getReward().getResourceString().add(rewardResource(m.group(9)));
            item.getReward().getResourceString().add(rewardResource(m.group(10)));
            item.getReward().getResource().add(toInt(m.group(7).split("/")[0]));
            item.getReward().getResource().add(toInt(m.group(8).split("/")[0]));
            item.getReward().getResource().add(toInt(m.group(9).split("/")[0]));
            item.getReward().getResource().add(toInt(m.group(10).split("/")[0]));
            item.getReward().setAward(remove(m.group(11)));
            item.getReward().setAward(remove(m.group(12)));

            list.add(item);
        }
    }

    private static void getRequire(String originStr) throws IOException {
        Pattern r = Pattern.compile("\\{\\{远征需求表\\|编号 =(\\d+)\\|日文名字 =.+\\|中文名字 =.+\\|耗时 =.+\\|舰队总等级 =(\\d*)\\|旗舰等级 =(\\d*)\\|最低舰娘数 =(\\d*)\\|必要舰娘 =(.*)\\|输送桶 =(.*)\\|燃料消耗 =(-?\\d*)\\|弹药消耗 =(-?\\d*)}}".replace(" ", ""));
        Matcher m = r.matcher(originStr);

        while (m.find()) {
            Expedition item = null;
            for (Expedition i :
                    list) {
                if (i.getId() == Integer.parseInt(m.group(1))) {
                    item = i;
                }
            }

            if (item == null) {
                throw new RuntimeException("wtf " + m.group());
            }

            item.getRequire().setTotalLevel(toInt(m.group(2)));
            item.getRequire().setFlagshipLevel(toInt(m.group(3)));
            item.getRequire().setMinShips(toInt(m.group(4)));
            item.getRequire().setEssentialShip(brToSpace(m.group(5)));
            item.getRequire().setBucket(brToSpace(m.group(6)));
            item.getRequire().getConsume().add(toInt(m.group(7)));
            item.getRequire().getConsume().add(toInt(m.group(8)));
        }
    }

    public static int toInt(String in) {
        try {
            if (in.contains("+")) {
                return Integer.parseInt(in.replace(" ", "").replace("+", ""));
            }
            if (in.contains("-")) {
                return -Integer.parseInt(in.replace(" ", "").replace("-", ""));
            } else {
                return Integer.parseInt(in.replace(" ", ""));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static float toFloat(String in) {
        try {
            if (in.contains("+")) {
                return Float.parseFloat(in.replace(" ", "").replace("+", ""));
            }
            if (in.contains("-")) {
                return -Float.parseFloat(in.replace(" ", "").replace("-", ""));
            } else {
                return Float.parseFloat(in.replace(" ", ""));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private static String brToSpace(String s) {
        return s.replace("<br />", " ").replace("<br/>", " ").replace("<br>", " ");
    }


    private static String rewardResource(String s) {
        if (s == null) {
            return null;
        }

        return s.replaceAll("(\\D*)(\\d+)(\\D*)/(\\D*)(\\d+.?\\d+)(\\D*)", "$1$2$3\n$4$5/h$6");
    }

    private static String remove(String s) {
        if (s == null) {
            return null;
        }

        return s.replace("{{", "").replace("}}", "").replaceAll("(.*)<b>(.*)</b>", "<b>$1$2</b>");
    }

    private static int getTime(String value) {
        Pattern p = Pattern.compile("(\\d+):(\\d+)");
        Matcher m = p.matcher(value);
        if (m.find()) {
            return Integer.parseInt(m.group(1)) * 60 + Integer.parseInt(m.group(2));
        }
        throw new RuntimeException("wtf");
    }
}
