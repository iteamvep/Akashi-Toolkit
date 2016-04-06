package rikka.akashitoolkit.staticdata;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionList {
    public static class Expedition {
        private int index;
        private int type;
        private int time;
        private String[] cost;
        private String[] award;
        private String name;
        private String flagShipLevel;
        private String totalShipLevel;
        private String require;

        public Expedition() {
            cost = new String[2];
            award = new String[4];
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFlagShipLevel() {
            return flagShipLevel;
        }

        public void setFlagShipLevel(String flagShipLevel) {
            this.flagShipLevel = flagShipLevel;
        }

        public String getTotalShipLevel() {
            return totalShipLevel;
        }

        public void setTotalShipLevel(String totalShipLevel) {
            this.totalShipLevel = totalShipLevel;
        }

        public String getAward(int id) {
            return award[id];
        }

        public void setAward(int id, String award) {
            this.award[id] = award;
        }

        public String getCost(int id) {
            return cost[id];
        }

        public void setCost(int id, String cost) {
            this.cost[id] = cost;
        }

        public String getRequire() {
            return require;
        }

        public void setRequire(String require) {
            this.require = require;
        }
    }

    private static final String XML_NAME = "Expedition.xml";

    private static List<Expedition> sList;

    public static synchronized List<Expedition> get(Context context) {
        if (sList == null) {
            long time = System.currentTimeMillis();
            loadAll(context);
            Log.d("ExpeditionList", String.format("Load list: %dms", System.currentTimeMillis() - time));

        }
        return sList;
    }

    private static void loadAll(Context context) {
        Resources resources = context.getResources();

        sList = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(resources.getAssets().open(XML_NAME), null);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType != XmlPullParser.START_TAG) {
                    eventType = xpp.next();
                    continue;
                }

                if (!xpp.getName().equals("item")) {
                    eventType = xpp.next();
                    continue;
                }

                Expedition item = new Expedition();
                sList.add(item);

                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                    String attrName = xpp.getAttributeName(i);
                    String attrValue = xpp.getAttributeValue(i);

                    switch (attrName) {
                        case "index":
                            int index = Integer.parseInt(attrValue);
                            item.setIndex(index);
                            break;
                        case "name":
                            item.setName(attrValue);
                            break;
                        case "mapName":
                            item.setType(Integer.parseInt(attrValue));
                            break;

                        case "time":
                            item.setTime(Integer.parseInt(attrValue));
                            break;

                        case "costRanLiao":
                            item.setCost(0, attrValue);
                            break;
                        case "costDanYao":
                            item.setCost(1, attrValue);
                            break;

                        case "flagShipLevel":
                            item.setFlagShipLevel(attrValue);
                            break;
                        case "totalShipLevel":
                            item.setTotalShipLevel(attrValue);
                            break;

                        case "awardRanLiao":
                            item.setAward(0, attrValue);
                            break;
                        case "awardDanYao":
                            item.setAward(1, attrValue);
                            break;
                        case "awardGang":
                            item.setAward(2, attrValue);
                            break;
                        case "awardLv":
                            item.setAward(3, attrValue);
                            break;

                        case "requireShip":
                            item.setRequire(attrValue);
                            break;
                    }
                }

                eventType = xpp.next();
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
