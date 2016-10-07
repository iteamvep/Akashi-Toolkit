package com.example;

import com.example.model.Expedition;
import com.example.network.RetrofitAPI;
import com.example.utils.Utils;
import com.spreada.utils.chinese.ZHConverter;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import static com.example.utils.Utils.getUrlStream;
import static com.example.utils.Utils.objectToJsonFile;

/**
 * Created by Rikka on 2016/6/15.
 */
public class ExpeditionGenerator {
    private static List<Expedition> list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        /*System.out.println("getInputStream..");
        String originStr = Utils.streamToString(getUrlStream("https://zh.kcwiki.moe/index.php?title=远征列表&action=raw"));
        System.out.println("finished..");*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zh.kcwiki.moe/")
                .build();

        RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
        ResponseBody body = service.getPage("远征列表", "raw").execute().body();
        Reader reader = body.charStream();

        StringBuilder sb = new StringBuilder();
        for (int c = reader.read(); c != -1; c = reader.read()) {
            sb.append((char) c);
        }
        String originStr = sb.toString();

        originStr = originStr.replaceAll("<span style=\"color:#f00\">([^<]*)</span>", "<b>$1</b>").replaceAll("\\s*=\\s*", "=");
        originStr = originStr.replace("<b>请参见</b><br />[[经验值和头衔#远征32:远洋练习航海经验|'''远征32相关''']]", "(基础经验值＋僚舰加成) ×等级补正");
        getReward(originStr);
        getRequire(originStr);

        for (Expedition e : list) {
            System.out.println(e);
        }

        for (Expedition e : list) {
            e.getName().setZh_tw(ZHConverter.toTC(e.getName().getZh_cn()));
        }

        objectToJsonFile(list, "app/src/main/assets/Expedition.json");
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
            item.setType(getType(item.getId()));

            item.getName().setJa(m.group(2).replace("<b>","").replace("</b>",""));
            item.getName().setZh_cn(m.group(3).replace("<b>","").replace("</b>",""));
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

    private static int getType(int id) {
        if (id <= 8)
            return 0;
        else if (id <= 16)
            return 1;
        else if (id <= 24)
            return 2;
        else if (id <= 32)
            return 3;
        else
            return 4;
    }

    private static void getRequire(String originStr) throws IOException {
        Pattern r = Pattern.compile("\\{\\{远征需求表\\|编号 =(\\d+)\\|日文名字 =.+\\|中文名字 =.+\\|耗时 =.+\\|舰队总等级 =(\\d*)\\|旗舰等级 =(\\d*)\\|最低舰娘数 =(\\d*)\\|必要舰娘 =(.*)\\|输送桶 =(.*)\\|燃料消耗 =(-?\\d*).*\\|弹药消耗 =(-?\\d*)}}".replace(" ", ""));
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
            item.getRequire().getConsumeString().add(m.group(7));
            item.getRequire().getConsumeString().add(m.group(8));
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

        return s.replaceAll("(\\D*)(\\d+)(\\D*)/(\\D*)(\\d*.?\\d+)(\\D*)", "$1$2$3<br>$4$5/h$6");
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
