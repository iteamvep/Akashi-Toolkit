package rikka.akashitoolkit.event;

import rikka.akashitoolkit.model.MultiLanguageEntry;

/**
 * Created by Rikka on 2016/8/12.
 */
public class EventMap {
    private int id;
    private int difficulty;
    private MultiLanguageEntry map_name;
    private MultiLanguageEntry battle_name;
    private MultiLanguageEntry battle_content;
    private String hp;
    private String reward;
    private String aa;

    public int getId() {
        return id;
    }
}
