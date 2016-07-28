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

            parse(galleryList, line);
        }

        print(galleryList);
    }

    private static Pattern GALLERY_PATTERN = Pattern.compile("文件:([^\\|]+)\\|(.+)");
    private static Pattern GALLERY_PATTERN2 = Pattern.compile("文件:([^\\|]+)");

    public static void parse(List<Gallery> list, String line) {
        Matcher m = GALLERY_PATTERN.matcher(line);

        if (m.find()) {
            list.add(new Gallery(m.group(1), m.group(2).replace("[[", "").replace("]]", "")));
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
        System.out.println("$data['title'] = \"title\";");
        System.out.println("$data['summary'] = \"summary\";");
        System.out.println("$data['content'] = \"content\";");
        System.out.println("$data['gallery']['urls'] = array(");
        for (Gallery entry : list) {
            System.out.println("\"" + entry.url + "\",");
        }
        System.out.println(");");

        if (list.size() > 0 && !TextUtils.isEmpty(list.get(0).name)) {
            System.out.println("$data['gallery']['names'] = array(");
            for (Gallery entry : list) {
                System.out.println("\"" + entry.name + "\",");
            }
            System.out.println(");");
        } else {
            System.out.println("$data['gallery']['names'] = null;");
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
