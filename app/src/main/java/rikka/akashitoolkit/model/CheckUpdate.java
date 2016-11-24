package rikka.akashitoolkit.model;

import java.util.List;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.utils.FlavorsUtils;

/**
 * Created by Rikka on 2016/3/15.
 */

// TODO 改为和 Event 那个一样的？
public class CheckUpdate {

    public static final int API_VERSION = 2;

    /**
     * versionCode : 7
     * versionName : 0.0.7-alpha
     * url : http://
     * change : - 增加装备改修模块
     - 增加统计 (LeanCloud)
     */

    private UpdateEntity update;
    /**
     * title : 标题
     * message : message
     * type : 0
     */

    private List<MessagesEntity> messages;

    private List<DataEntity> data;

    public void setUpdate(UpdateEntity update) {
        this.update = update;
    }

    public void setMessages(List<MessagesEntity> messages) {
        this.messages = messages;
    }

    public UpdateEntity getUpdate() {
        return update;
    }

    public List<MessagesEntity> getMessages() {
        return messages;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public class DataEntity {
        String filename;
        long version;
        String data;

        public String getName() {
            return filename;
        }

        public void setName(String name) {
            this.filename = name;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class UpdateEntity {
        private int versionCode;
        private String versionName;
        private String url;
        private String url2;
        private LocaleMultiLanguageEntry change;

        public int getVersionCode() {
            return versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public String getUrl() {
            return url;
        }

        public String getUrl2() {
            return url2;
        }

        public MultiLanguageEntry getChange() {
            return change;
        }
    }

    public static class MessagesEntity {

        private boolean only_play;
        private boolean only_china;

        private int min_api;
        private int max_api;

        private int min_version;
        private int max_version;

        /**
         * 根据 API 版本，是否是 Play 版 / 国内版决定是否显示
         *
         * @return 是否显示
         */
        public boolean shouldShow() {
            return !((max_api != 0 && BuildConfig.VERSION_CODE > max_api) || (min_version != 0 && BuildConfig.VERSION_CODE < min_version)) && !((max_api != 0 && API_VERSION > max_api) || (min_api != 0 && API_VERSION < min_api)) && !(only_china && FlavorsUtils.isPlay()) && !(only_play && !FlavorsUtils.isPlay());
        }

        private LocaleMultiLanguageEntry title;
        private LocaleMultiLanguageEntry message;
        private int type;
        private boolean show_first;
        private String link;
        private LocaleMultiLanguageEntry action_name;
        private int time;
        private List<String> images;

        public long getId() {
            return (title.getZhCN() + message.getZhCN()).hashCode();
        }

        public MultiLanguageEntry getTitle() {
            return title;
        }

        public MultiLanguageEntry getMessage() {
            return message;
        }

        public int getType() {
            return type;
        }

        public boolean isShowFirst() {
            return show_first;
        }

        public String getLink() {
            return link;
        }

        public MultiLanguageEntry getActionName() {
            return action_name;
        }

        public int getTime() {
            return time;
        }

        public List<String> getImages() {
            return images;
        }
    }
}
