package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/4/13.
 */
public class DataChangedAction {
    private String className;

    public DataChangedAction(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
