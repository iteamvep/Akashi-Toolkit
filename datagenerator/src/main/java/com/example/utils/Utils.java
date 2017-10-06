package com.example.utils;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rikka on 2016/3/26.
 */
public class Utils {
    public static String streamToString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(is));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append('\n');
        }
        in.close();

        return response.toString();
    }

    public static File writeStreamToFile(InputStream inputStream, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(path);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void objectToJsonFile(Object element, String outName) {
        objectToJsonFile(new Gson(), element, outName);
    }

    public static void objectToJsonFile(Gson gson, Object element, String outName) {
        objectToJsonFile(gson.toJson(element), outName);
    }

    public static void objectToJsonFile(String element, String outName) {
        InputStream is = new ByteArrayInputStream(element.getBytes());

        File file = new File(outName);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }

        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(outName);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int stringToInt(String in) {
        try {
            if (in.contains("+")) {
                return Integer.parseInt(in.replace(" ", "").replace("+", ""));
            } else {
                return Integer.parseInt(in.replace(" ", ""));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static InputStream getUrlStream(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        return con.getInputStream();
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(String.format("%02x", anArray));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getKCWikiFileUrl(String fileName) {
        return getKCWikiFileUrl("upload.kcwiki.org", fileName);
    }

    public static String getKCWikiFileUrl(String host, String fileName) {
        String md5 = Utils.md5(fileName);
        if (md5 == null) {
            return null;
        }

        String a = md5.substring(0, 1);
        String b = md5.substring(0, 2);

        return String.format("http://%s/commons/%s/%s/%s", host, a, b, fileName);
    }
}
