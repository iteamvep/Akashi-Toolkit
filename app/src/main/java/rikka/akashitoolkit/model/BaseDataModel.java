package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/5/12.
 */
public class BaseDataModel {
    private int id;
    private boolean bookmarked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
