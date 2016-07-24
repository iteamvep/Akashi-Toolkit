package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/4/30.
 */
public class Seasonal {

    private int type;
    private String title;
    private String summary;
    private String content;

    private Gallery gallery;

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public static class Gallery {
        List<String> urls;
        List<String> names;

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
}
