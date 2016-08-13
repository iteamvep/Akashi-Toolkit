package rikka.akashitoolkit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/8/12.
 */
public class Event extends ArrayList<Event.Container> {

    public static class Container {

        private int type;
        private Object object;

        public int getType() {
            return type;
        }

        public Object getObject() {
            return object;
        }
    }

    public static class Title {

        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }

    public static class Url {

        private String title;
        private String content;
        private String url;
        private String url_text;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }

        public String getUrlText() {
            return url_text;
        }
    }

    public static class Gallery {

        private String title;
        private String summary;
        private String content;

        List<String> urls;
        List<String> names;

        public String getTitle() {
            return title;
        }

        public String getSummary() {
            return summary;
        }

        public String getContent() {
            return content;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }
    }

    public static class Maps {

        private String title;
        private List<Integer> ids;

        public String getTitle() {
            return title;
        }

        public List<Integer> getIds() {
            return ids;
        }
    }
}
