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
 * Created by Rikka on 2016/3/12.
 */
public class QuestList {
    public static class Quest {
        private int index;
        private int type;
        private int period;
        private String code;
        private String title;
        private String content;
        private String[] award;
        private String awardOther;
        private int unlockIndex;

        public Quest() {
            award = new String[4];
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAward(int id) {
            return award[id];
        }

        public void setAward(int id, String award) {
            this.award[id] = award;
        }

        public String getAwardOther() {
            return awardOther;
        }

        public void setAwardOther(String awardOther) {
            this.awardOther = awardOther;
        }

        public int getUnlockIndex() {
            return unlockIndex;
        }

        public void setUnlockIndex(int unlockIndex) {
            this.unlockIndex = unlockIndex;
        }
    }

    private static final String XML_NAME = "Quest.xml";

    private static List<Quest> sList;

    public static synchronized List<Quest> get(Context context) {
        if (sList == null) {
            loadAll(context);
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

                Quest item = new Quest();
                sList.add(item);

                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                    String attrName = xpp.getAttributeName(i);
                    String attrValue = xpp.getAttributeValue(i);

                    switch (attrName) {
                        case "index":
                            int index = Integer.parseInt(attrValue);
                            item.setIndex(index);

                            StringBuilder code = new StringBuilder();
                            code.append((char) ('A' + (index / 1000 - 1)));
                            switch (index % 1000 / 100) {
                                case 1:
                                    code.append('d');
                                    break;
                                case 2:
                                    code.append('w');
                                    break;
                                case 3:
                                    code.append('m');
                                    break;
                            }
                            code.append(Integer.toString(index % 100));
                            item.setCode(code.toString());
                            break;
                        case "type":
                            item.setType(Integer.parseInt(attrValue));
                            break;
                        case "period":
                            item.setPeriod(Integer.parseInt(attrValue));
                            break;

                        case "title":
                            item.setTitle(attrValue);
                            break;
                        case "content":
                            item.setContent(attrValue);
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

                        case "awardOther":
                            item.setAwardOther(attrValue);
                            break;

                        case "unlockIndex":
                            try {
                                item.setUnlockIndex(Integer.parseInt(attrValue));
                            } catch (Exception ignore) {

                            }

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
