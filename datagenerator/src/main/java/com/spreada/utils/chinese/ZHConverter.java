package com.spreada.utils.chinese;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class ZHConverter {


    private Properties charMap = new Properties();
    private Set<String> conflictingSets = new HashSet<>();

    public static final int TRADITIONAL = 0;
    public static final int SIMPLIFIED = 1;
    private static final int NUM_OF_CONVERTERS = 2;
    private static final ZHConverter[] converters = new ZHConverter[NUM_OF_CONVERTERS];
    private static final String[] propertyFiles = new String[2];

    static {
        propertyFiles[TRADITIONAL] = "L:\\NetBeans\\NetBeansProjects\\Akashi-Toolkit\\src\\properties/zh2Hant.properties";
        propertyFiles[SIMPLIFIED] = "L:\\NetBeans\\NetBeansProjects\\Akashi-Toolkit\\src\\properties/zh2Hans.properties";
    }

    /**
     * @param converterType 0 for traditional and 1 for simplified
     * @return
     */
    public static ZHConverter getInstance(int converterType) {

        if (converterType >= 0 && converterType < NUM_OF_CONVERTERS) {

            if (converters[converterType] == null) {
                synchronized (ZHConverter.class) {
                    if (converters[converterType] == null) {
                        converters[converterType] = new ZHConverter(propertyFiles[converterType]);
                    }
                }
            }
            return converters[converterType];

        } else {
            throw new IllegalArgumentException("");
        }
    }

    public static String toTC(String text) {
        return convert(text, TRADITIONAL);
    }

    public static String toSC(String text) {
        return convert(text, SIMPLIFIED);
    }

    public static String convert(String text, int converterType) {
        ZHConverter instance = getInstance(converterType);
        return instance.convert(text);
    }


    private ZHConverter(String propertyFile) {

        InputStream is = null;
        try {
            is = new FileInputStream(new File(propertyFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //is = getClass().getResourceAsStream(propertyFile);

        if (is != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is));
                charMap.load(reader);
            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        initializeHelper();
    }

    private void initializeHelper() {
        Map<String, Integer> stringPossibilities = new HashMap<>();
        Iterator iter = charMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (key.length() >= 1) {

                for (int i = 0; i < (key.length()); i++) {
                    String keySubstring = key.substring(0, i + 1);
                    if (stringPossibilities.containsKey(keySubstring)) {
                        Integer integer = stringPossibilities.get(keySubstring);
                        stringPossibilities.put(keySubstring, integer + 1);

                    } else {
                        stringPossibilities.put(keySubstring, 1);
                    }

                }
            }
        }

        iter = stringPossibilities.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (stringPossibilities.get(key) > 1) {
                conflictingSets.add(key);
            }
        }
    }

    public String convert(String in) {
        StringBuilder outString = new StringBuilder();
        StringBuilder stackString = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {

            char c = in.charAt(i);
            String key = "" + c;
            stackString.append(key);

            if (conflictingSets.contains(stackString.toString())) {
            } else if (charMap.containsKey(stackString.toString())) {
                outString.append(charMap.get(stackString.toString()));
                stackString.setLength(0);
            } else {
                CharSequence sequence = stackString.subSequence(0, stackString.length() - 1);
                stackString.delete(0, stackString.length() - 1);
                flushStack(outString, new StringBuilder(sequence));
            }
        }

        flushStack(outString, stackString);

        return outString.toString();
    }


    private void flushStack(StringBuilder outString, StringBuilder stackString) {
        while (stackString.length() > 0) {
            if (charMap.containsKey(stackString.toString())) {
                outString.append(charMap.get(stackString.toString()));
                stackString.setLength(0);

            } else {
                outString.append("").append(stackString.charAt(0));
                stackString.delete(0, 1);
            }

        }
    }


    String parseOneChar(String c) {

        if (charMap.containsKey(c)) {
            return (String) charMap.get(c);

        }
        return c;
    }


}
