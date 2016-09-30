package com.example;

import com.example.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rikka on 2016/7/24.
 */
public class SeasonalGalleryGenerator {

    public static void main(String[] args) {
        List<Gallery> galleryList = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.equals("</gallery>")) {
                break;
            }

            if (!line.equals("<gallery>")) {
                parse(galleryList, line);
            }
        }

        print(galleryList);
    }

    private static Pattern GALLERY_PATTERN = Pattern.compile("([^\\|]+)\\|(.+)");
    private static Pattern GALLERY_PATTERN2 = Pattern.compile("([^\\|]+)");

    public static void parse(List<Gallery> list, String line) {
        Matcher m = GALLERY_PATTERN.matcher(line/*.replace("File:", "文件:")*/);

        if (m.find()) {
            list.add(new Gallery(
                    m.group(1).replace("File:", "").replace("文件:", "").trim(),
                    m.group(2).replaceAll("\\[\\[.+\\|(.+)]]", "$1").replace(" ", "").trim()));
        } else {
            m = GALLERY_PATTERN2.matcher(line);
            if (m.find()) {
                list.add(new Gallery(m.group(1), null));
            }
        }
    }

    public static void print(List<Gallery> list) {
        System.out.println("$data = null;");
        System.out.println("$data['type'] = $TYPE_GALLERY;");
        System.out.println("$data['not_safe'] = true;");
        System.out.println("$data['object']['title']['zh_cn'] = \"title\";");
        //System.out.println("$data['object']['summary'] = \"summary\";");
        //System.out.println("$data['object']['content'] = \"content\";");
        System.out.println("$data['object']['urls'] = array(");
        for (Gallery entry : list) {
            System.out.println("\"" + entry.url + "\",");
        }
        System.out.println(");");

        if (list.size() > 0 && !TextUtils.isEmpty(list.get(0).name)) {
            System.out.println("$data['object']['names'] = array(");
            for (Gallery entry : list) {
                System.out.println("\"" + entry.name + "\",");
            }
            System.out.println(");");
        } else {
            System.out.println("$data['object']['names'] = null;");
        }

        System.out.println("array_push($json, $data);");
    }

    private static class Gallery {
        public String url;
        public String name;

        public Gallery(String url, String name) {
            this.url = url;
            this.name = name;
        }
    }
}
