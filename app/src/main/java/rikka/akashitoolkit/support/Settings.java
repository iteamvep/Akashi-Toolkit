package rikka.akashitoolkit.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Set;

/**
 * Created by Rikka on 2016/3/8.
 */
public class Settings {
    public static final String XML_NAME = "settings";

    public static final String NIGHT_MODE = "night_mode";
    public static final String TWITTER_COUNT = "twitter_count";
    public static final String TWITTER_AVATAR_URL = "twitter_avatar_url";
    public static final String TWITTER_LANGUAGE = "twitter_display_language";
    public static final String TWITTER_GRID_LAYOUT = "twitter_grid_layout";
    public static final String LAST_DRAWER_ITEM_ID = "last_drawer_item_id";
    public static final String UPDATE_CHECK_CHANNEL = "update_check_channel";
    public static final String QUEST_FILTER = "quest_filter";
    public static final String SHIP_FILTER = "ship_filter";
    public static final String SHIP_FINAL_VERSION = "ship_show_final_version";
    public static final String SHIP_SPEED = "ship_show_speed";
    public static final String SHIP_SORT = "ship_show_sort";

    public static final String APP_LANGUAGE = "app_language";

    public static final String DATA_LANGUAGE = "data_language";
    public static final String DATA_TITLE_LANGUAGE = "data_title_language";

    public static final String DOWNLOAD_WIFI_ONLY = "download_wifi_only";

    public static final String NOTIFICATION_SOUND = "notification_sound";
    public static final String NOTIFICATION_PRIORITY = "notification_priority";

    public static final String NAV_BAR_COLOR = "nav_bar_color";

    public static final String CACHE_MAX_SIZE = "cache_max_size";

    public static final String DEVELOPER = "developer";

    public static final String OPEN_IN_NEW_DOCUMENT = "open_in_new_document";
    public static final String SHOW_SHIP_BANNER = "show_ship_banner";

    public static final String SUBTITLE_VERSION = "subtitle_version";

    public static final String SHARE_NO_FOOTER = "share_no_footer";

    public static final String PUSH_TOPICS = "push_topics";

    private static BaseSetting sInstance;

    public static synchronized BaseSetting instance(Context context) {
        if (sInstance == null) {
            sInstance = new BaseSetting(context, XML_NAME);
        }

        return sInstance;
    }

    public static void init(Context context) {
        instance(context);
    }

    public static BaseSetting instance() {
        if (sInstance == null) {
            throw new RuntimeException("Call init() in Application onCreate()");
        }

        return sInstance;
    }
}
