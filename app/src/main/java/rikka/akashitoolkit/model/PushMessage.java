package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/11/24.
 */

public class PushMessage {

    private MultiLanguageEntry title;
    private MultiLanguageEntry message;
    private int id;
    private String uri;

    public MultiLanguageEntry getTitle() {
        return title;
    }

    public MultiLanguageEntry getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }
}
