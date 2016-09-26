package rikka.akashitoolkit.support;

import java.util.HashMap;
import java.util.Map;

import rikka.akashitoolkit.BuildConfig;

/**
 * Created by etby on 16-3-15.
 */
public class ApiConstParam {
    /**
     * 转发twitter的正文语言
     */
    public static final class TwitterContentLanguage {
        /**
         * 日文和中文
         */
        public static final int JP_AND_ZH = 0;
        public static final int ONLY_JP = 1;
        public static final int ONLY_ZH = 2;
    }

    public static final class Language {
        public static final int ZH_CN = 0;
        public static final int JA = 1;
        public static final int EN = 2;
        public static final int ZH_TW = 3;
    }

    public static final class Message {
        public static final int NOT_DISMISSIBLE = 1 << 0;
        public static final int HTML_CONTENT = 1 << 1;
        public static final int ACTION_VIEW_BUTTON = 1 << 2;
        public static final int COUNT_DOWN = 1 << 3;
    }

    public static Map<String, Long> DATA_JSON_VERSION = new HashMap<String, Long>() {
        @Override
        public Long get(Object key) {
            return containsKey(key) ? super.get(key) : BuildConfig.TIMESTAMP;
        }
    };

    static {
        DATA_JSON_VERSION.put("EquipImprovement.json", BuildConfig.TIMESTAMP);
        DATA_JSON_VERSION.put("Equip.json", BuildConfig.TIMESTAMP);
        DATA_JSON_VERSION.put("EquipType.json", BuildConfig.TIMESTAMP);

        DATA_JSON_VERSION.put("Ship.json", BuildConfig.TIMESTAMP);
        DATA_JSON_VERSION.put("ShipType.json", BuildConfig.TIMESTAMP);

        DATA_JSON_VERSION.put("Item.json", BuildConfig.TIMESTAMP);

        DATA_JSON_VERSION.put("Map.json", BuildConfig.TIMESTAMP);
        DATA_JSON_VERSION.put("MapDetail.json", BuildConfig.TIMESTAMP);
        DATA_JSON_VERSION.put("MapType.json", BuildConfig.TIMESTAMP);

        DATA_JSON_VERSION.put("Quest.json", BuildConfig.TIMESTAMP);
    }

    public static Map<String, String> DATA_JSON_NAME = new HashMap<>();

    static {
        DATA_JSON_NAME.put("EquipImprovement.json", "改修数据");
        DATA_JSON_NAME.put("Equip.json", "装备数据");
        DATA_JSON_NAME.put("EquipType.json", "装备类型数据");

        DATA_JSON_NAME.put("Ship.json", "舰娘数据");
        DATA_JSON_NAME.put("ShipType.json", "舰娘类型数据");

        DATA_JSON_NAME.put("Item.json", "物品数据");

        DATA_JSON_NAME.put("Map.json", "带路数据");
        DATA_JSON_NAME.put("MapType.json", "海图类型数据");
        DATA_JSON_NAME.put("MapDetail.json", "海图数据");

        DATA_JSON_NAME.put("Quest.json", "任务数据");
    }
}
