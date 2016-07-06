package com.example;

import com.example.model.MultiLanguageEntry;
import com.example.model.NewEquip;
import com.example.network.RetrofitAPI;
import com.example.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by Rikka on 2016/7/4.
 */
public class EquipGenerator {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://zh.kcwiki.moe/")
                .build();

        RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
        /*ResponseBody body = service.getPage("模块:舰娘装备数据改", "raw").execute().body();
        Reader is = body.charStream();*/

        Reader reader = new FileReader(new File("datagenerator/equip.lua"));

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
                .replace("\t", "")
                .replace("{}", "null")
                .replace("},\n}", "}\n}")

                .replaceAll("\\{\\[\"燃料\"]=(\\d+),\\[\"弹药\"]=(\\d+),\\[\"钢材\"]=(\\d+),\\[\"铝\"]=(\\d+)}", "[$1,$2,$3,$4]")
                .replaceAll("\\{\\[\"废弃燃料\"]=(\\d+),\\[\"废弃弹药\"]=(\\d+),\\[\"废弃钢材\"]=(\\d+),\\[\"废弃铝\"]=(\\d+)}", "[$1,$2,$3,$4]")

                .replaceAll("\\[\"([\\d\\]]+)\"\\]=\\{", "{\n[\"id\"]=\"$1\",")
                .replaceAll("\\[\"([^]]+)\"\\]=", "\"$1\":")
                .replaceAll("\"([^\"]+)\":\\{([^:=\\}]+)\\}", "\"$1\":[$2]")

                .replaceAll("\\{\"开发\":\\[(\\d+),(\\d+)],\"改修\":\\[(\\d+),(\\d+)],\"装备数\":(\\d+),\"装备\":\"(\\d+)\"}", "[$1,$2,$3,$4,$5,\"$6\"]")
                .replaceAll("\\{\"开发\":\\[(\\d+),(\\d+)],\"改修\":\\[(\\d+),(\\d+)],\"装备数\":(\\d+)}", "[$1,$2,$3,$4,$5]")
                .replaceAll("\\{\"装备\":\"(\\d+)\",\"等级\":(\\d+)}", "[\"$1\",$2]")

                .replace("\"日期\":{", "\"日期\":[")
                .replace("\"日\":", "")
                .replace("\"一\":", "")
                .replace("\"二\":", "")
                .replace("\"三\":", "")
                .replace("\"四\":", "")
                .replace("\"五\":", "")
                .replaceAll("\"六\":(\\[.+])\n},", "$1\n],")

                .replace("{\n{\n", "[\n{\n")
                .replace("}\n}\n}", "}\n}\n]")

                .replace("\"无\"", "0")
                .replace("\"?\"", "0")
                .replace("\"短\"", "1")
                .replace("\"中\"", "2")
                .replace("\"长\"", "3")
                .replace("\"超长\"", "4");

        Gson gson = new GsonBuilder()
                .create();

        List<NewEquip> list = gson.fromJson(new StringReader(str), new TypeToken<List<NewEquip>>() {
        }.getType());

        for (NewEquip item : list) {
            item.setRarity(item.get稀有度().length());
            item.setName(new MultiLanguageEntry());
            item.getName().setZh_cn(item.getNameCN());
            item.getName().setJa(item.getNameJP());

            if (item.get装备改修() != null && item.get装备改修2() != null) {
                item.get装备改修().setShips();
                item.get装备改修2().setShips();
                item.setImprovements(new NewEquip.ImprovementEntity[]{item.get装备改修(), item.get装备改修2()});
            } else if (item.get装备改修() != null) {
                item.get装备改修().setShips();
                item.setImprovements(new NewEquip.ImprovementEntity[]{item.get装备改修()});
            }

            File file = new File("datagenerator/data/equips/" + item.getNameCN().replace("/", "_") + ".txt");
            if (!file.exists()) {
                try {
                    ResponseBody body = service.getPage(item.getNameCN(), "raw").execute().body();
                    Utils.writeStreamToFile(body.byteStream(), file.getPath());
                    //System.out.println();
                } catch (Exception ignored) {
                    System.out.print(item.getNameCN());
                    System.out.println(" 炸裂了");
                    continue;
                }
            }

            String s = Utils.streamToString(new FileInputStream(file))
                    .replaceAll("\\|([^\\|]+) =", "\\|$1=");

            Pattern r;
            Matcher m;

            r = Pattern.compile("\\|图鉴说明原文=([^\\|}]+)");
            m = r.matcher(s);
            if (m.find()) {
                item.getIntroduction().setJa(a(m.group(1)));
            }

            r = Pattern.compile("\\|图鉴说明译文=([^\\|}]+)");
            m = r.matcher(s);
            if (m.find()) {
                item.getIntroduction().setZh_cn(a(m.group(1)));
            }
        }

        // 敌舰数据
        reader = service.getPage("深海栖舰装备", "raw").execute().body().charStream();
        sb = new StringBuilder();
        for (int c = reader.read(); c != -1; c = reader.read()) {
            sb.append((char) c);
        }
        str = sb.toString()
                .replace("=短\"", "1")
                .replace("=中\"", "2")
                .replace("=长\"", "3")
                .replace("=超长\"", "4");

        Pattern r;
        Matcher m;
        r = Pattern.compile("\\{\\{深海装备列表\\|[^\\}]+}}");
        m = r.matcher(str);
        while (m.find()) {
            Matcher m2;

            NewEquip equip = new NewEquip();
            list.add(equip);

            str = m.group().replace("\n", "");

            m2 = Pattern.compile("编号=(\\d+)").matcher(str);
            if (m2.find())
                equip.setId(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("中文装备名字=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getName().setZh_cn(m2.group(1));

            m2 = Pattern.compile("日文装备名字=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getName().setJa(m2.group(1));

            m2 = Pattern.compile("图标=(\\d+)").matcher(str);
            if (m2.find()) {
                int type = Utils.stringToInt(m2.group(1));
                equip.setTypes(new int[]{type, type, type, type});
            }

            m2 = Pattern.compile("等级=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.setRarity(m2.group(1).length());

            m2 = Pattern.compile("射程=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setRange(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("火力=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setFirepower(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("装甲=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setArmor(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("雷装=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setTorpedo(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("回避=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setEvasion(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("对空=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setAA(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("对潜=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setASW(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("索敌=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setLOS(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("爆装=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setBombing(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("命中=([^\\|]+)").matcher(str);
            if (m2.find())
                equip.getAttr().setAccuracy(Utils.stringToInt(m2.group(1)));

            m2 = Pattern.compile("备注=([^\\|}]+)").matcher(str);
            if (m2.find())
                equip.setRemark(m2.group(1));
        }

        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        str = gson.toJson(list)
                .replace("\"备注\"", "\"remark\"")
                .replace("\"属性\"", "\"attr\"")
                .replace("\"射程\"", "\"range\"")
                .replace("\"对空\"", "\"aa\"")
                .replace("\"装甲\"", "\"armor\"")
                .replace("\"对潜\"", "\"asw\"")
                .replace("\"回避\"", "\"evasion\"")
                .replace("\"火力\"", "\"fire\"")
                .replace("\"索敌\"", "\"los\"")
                .replace("\"雷装\"", "\"torpedo\"")
                .replace("\"爆装\"", "\"bomb\"")
                .replace("\"命中\"", "\"accuracy\"")
                .replace("\"废弃\"", "\"broken\"")
                .replace("\"改修备注\"", "\"remark\"")
                .replace("\"资源消费\"", "\"cost\"")
                .replace("\"初期消费\"", "\"item\"")
                .replace("\"中段消费\"", "\"item2\"")
                .replace("\"更新消费\"", "\"item3\"")
                .replace("\"更新装备\"", "\"upgrade\"")
                .replace("\"类别\"", "\"type\"");

        Utils.objectToJsonFile(str, "app/src/main/assets/Equip.json");
    }

    private static String a(String s) {
        if (s.contains("<poem>")) {
            return s.replace("<poem>", "")
                    .replace("</poem>", "")
                    .trim();
        } else {
            return s.replace("\n", "")
                    .replace("<br/>", "\n")
                    .replace("<br>", "\n")
                    .replace("<br />", "\n")
                    .trim();
        }
    }
}
