package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/6/12.
 */
public class ChangeNavigationDrawerItemAction {
    private int item;

    public ChangeNavigationDrawerItemAction(int item) {
        this.item = item;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }
}
