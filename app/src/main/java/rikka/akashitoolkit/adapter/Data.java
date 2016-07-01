package rikka.akashitoolkit.adapter;

/**
 * Created by Rikka on 2016/7/1.
 */
public class Data {
    protected Object data;
    protected int type;
    protected long id;

    public Data(Object data, int type, long id) {
        this.data = data;
        this.type = type;
        this.id = id;
    }
}
