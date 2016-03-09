package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/3/9.
 */
public class PreferenceChangedAction {
    private String key;

    public PreferenceChangedAction(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
