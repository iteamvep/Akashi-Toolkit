package com.example;

import com.example.model.ShipVoice;
import com.example.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.utils.Utils.getUrlStream;

/**
 * 从 zh.kcwiki.moe/wiki/季节性/2016年梅雨季节 得到给app对应部分的语音部分的内容
 */
public class SeasonalVoice {
    private static Map<String, String> MAP = new HashMap<>();

    static {
        MAP.put("舰娘名字", "scene");
        MAP.put("日文台词", "jp");
        MAP.put("中文译文", "zh");
        MAP.put("档名", "url");
    }

    public static void main(String[] args) throws IOException {
        List<ShipVoice> list = new ArrayList<>();
        for (String lineStr : getLines()) {
            try {
                String[] kv = lineStr.split("\\|");

                if (kv.length < 1) {
                    System.out.println("Skipped line: " + lineStr);
                    continue;
                }

                ShipVoice item = new ShipVoice();

                for (String text : kv) {
                    String temp[] = text.split("=");
                    if (temp.length < 2) {
                        continue;
                    }
                    parse(item, temp[0], temp[1]);
                }

                list.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // php 里面的内容..
        for (ShipVoice item : list) {
            System.out.println("$data_voice['type'] = $TYPE_VOICE;");
            System.out.println("$data_voice['zh'] = \"" + item.getZh() + "\";");
            System.out.println("$data_voice['jp'] = \"" + item.getJp() + "\";");
            System.out.println("$data_voice['url'] = \"" + Utils.getKCWikiFileUrl("upload.kcwiki.moe", item.getUrl() + ".mp3") + "\";");
            System.out.println("$data_voice['text'] = \"" + item.getScene() + "\";");
            System.out.println("array_push($data['content'], $data_voice);");
            System.out.println();
        }
    }

    private static void parse(ShipVoice item, String key, String value) throws NoSuchFieldException, IllegalAccessException {
        Class cls = item.getClass();
        key = MAP.get(key);
        if (key != null) {
            Field field = cls.getDeclaredField(key);
            if (field != null) {
                field.setAccessible(true);
                field.set(item, value);
            }
        }
    }

    private static List<String> getLines() throws IOException {
        System.out.println("getInputStream..");
        InputStream is = getUrlStream("http://zh.kcwiki.moe/index.php?title=%E5%AD%A3%E8%8A%82%E6%80%A7/2016%E5%B9%B4%E6%A2%85%E9%9B%A8%E5%AD%A3%E8%8A%82&action=raw");
        String originStr = Utils.streamToString(is).replace(" ", "");
        System.out.println("finished..");
        originStr = originStr.replaceAll("\n", "").replaceAll("\r", "");

        Pattern r = Pattern.compile("\\{\\{台词翻译表\\|.+?\\|舰娘名字=(.+?)\\|日文台词=(.+?)\\|中文译文=(.+?)\\}\\}");
        Matcher m = r.matcher(originStr);

        List<String> lineStrList = new ArrayList<>();

        while (m.find()) {
            lineStrList.add(m.group().replace("{{", "").replace("}}", ""));
        }

        return lineStrList;
    }
}
