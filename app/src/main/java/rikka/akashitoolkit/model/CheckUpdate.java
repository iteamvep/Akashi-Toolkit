package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/3/15.
 */
public class CheckUpdate {

    /**
     * versionCode : 7
     * versionName : 0.0.7-alpha
     * url : http://www.minamion.com/Akashi/akashitoolkit-alpha-7.apk
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
        private String change;

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setChange(String change) {
            this.change = change;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public String getUrl() {
            return url;
        }

        public String getChange() {
            return change;
        }
    }

    public static class MessagesEntity {
        private String title;
        private String message;
        private int type;
        private String link;
        private String action_name;
        private int time;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setAction_name(String action_name) {
            this.action_name = action_name;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public int getType() {
            return type;
        }

        public String getLink() {
            return link;
        }

        public String getAction_name() {
            return action_name;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
