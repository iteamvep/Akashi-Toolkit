package rikka.akashitoolkit.event;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.model.LocaleMultiLanguageEntry;
import rikka.akashitoolkit.model.MultiLanguageEntry;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/8/12.
 */
public class Event extends ArrayList<Event.Container> {

    public static final int API_VERSION = 1;

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_GALLERY = 1;
    public static final int TYPE_CONTENT = 2;
    public static final int TYPE_URL = 3;
    public static final int TYPE_VOICE = 4;

    public static class Container {

        private int type;
        private Object object; // LinkedTreeMap

        private boolean only_play;
        private boolean only_china;
        private boolean not_safe; // Google: 不可以，这很色情

        private int min_api;
        private int max_api;

        public int getType() {
            return type;
        }

        public Object getObject() {
            return object;
        }

        /**
         * 根据 API 版本，是否是 Play 版 / 国内版决定是否显示
         *
         * @return 是否显示
         */
        public boolean shouldShow() {
            return !((max_api != 0 && API_VERSION > max_api) || (min_api != 0 && API_VERSION < min_api)) && !(only_china && FlavorsUtils.isPlay()) && !(only_play && !FlavorsUtils.isPlay()) && !(not_safe && FlavorsUtils.shouldSafeCheck());
        }
    }

    public interface OnItemParseListener {
        void onItemParse(int type, Object object);
    }

    public void parse(@Nullable OnItemParseListener listener) {
        Gson gson = new Gson();
        for (Container container : this) {
            Object object = null;
            String json = gson.toJson(container.getObject());

            if (!container.shouldShow()) {
                //Log.d("Event", "skipped: " + json);
                continue;
            }

            boolean error = false;
            switch (container.getType()) {
                case TYPE_TITLE:
                case TYPE_CONTENT:
                    object = gson.fromJson(json, Event.Title.class);
                    break;
                case TYPE_GALLERY:
                    object = gson.fromJson(json, Event.Gallery.class);

                    Event.Gallery _data = (Event.Gallery) object;

                    List<String> urls = new ArrayList<>();
                    for (String url : _data.getUrls()) {
                        if (!url.startsWith("http")) {
                            urls.add(Utils.getKCWikiFileUrl(url));
                        } else {
                            urls.add(url);
                        }
                    }
                    _data.getUrls().clear();
                    _data.getUrls().addAll(urls);
                    break;
                case TYPE_URL:
                    object = gson.fromJson(json, Event.Url.class);
                    break;
                default:
                    Log.e("Event", "unhandled: " + json);
                    error = true;
            }

            if (!error && listener != null) {
                listener.onItemParse(container.getType(), object);
            }
        }
    }

    public static class BaseObject {
        private LocaleMultiLanguageEntry title;
        private LocaleMultiLanguageEntry summary;
        private LocaleMultiLanguageEntry content;

        public MultiLanguageEntry getTitle() {
            if (title == null) {
                title = new LocaleMultiLanguageEntry();
            }

            return title;
        }

        public LocaleMultiLanguageEntry getSummary() {
            if (summary == null) {
                summary = new LocaleMultiLanguageEntry();
            }

            return summary;
        }

        public MultiLanguageEntry getContent() {
            if (content == null) {
                content = new LocaleMultiLanguageEntry();
            }

            return content;
        }
    }

    /**
     * 标题
     */
    public static class Title extends BaseObject {

    }

    /**
     * 具有一个按钮可以打开一个链接
     */
    public static class Url extends BaseObject {
        private String url;
        private LocaleMultiLanguageEntry url_text;

        public String getUrl() {
            return url;
        }

        public MultiLanguageEntry getUrlText() {
            return url_text;
        }
    }

    /**
     * 图片们
     */
    public static class Gallery extends BaseObject {
        List<String> urls;
        // TODO support multi language ?
        List<String> names;
        List<Integer> ids;
        int action_type;

        public List<String> getUrls() {
            return urls;
        }

        public List<String> getNames() {
            return names;
        }

        public List<Integer> getIds() {
            return ids;
        }

        public int getActionType() {
            return action_type;
        }
    }

    /**
     * 语音们
     */
    public static class Voices extends BaseObject {
        private List<Voice> voices;

        public static class Voice extends BaseObject {
            private LocaleMultiLanguageEntry type;
            private List<ShipVoice> voice;

            public LocaleMultiLanguageEntry getType() {
                return type;
            }

            public List<ShipVoice> getVoice() {
                return voice;
            }
        }

        public List<Voice> getVoices() {
            return voices;
        }
    }
}
