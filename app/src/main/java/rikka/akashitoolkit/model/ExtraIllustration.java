package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/4/26.
 */
public class ExtraIllustration {

    /**
     * id : 424
     * image : [["KanMusu224IllustTsuyu.png","KanMusu224DmgIllustTsuyu.png"]]
     */

    private int id;
    private List<String> image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
