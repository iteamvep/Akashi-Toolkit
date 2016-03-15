package rikka.akashitoolkit.model;

/**
 * Created by Rikka on 2016/3/15.
 */
public class CheckUpdate {

    /**
     * versionCode : 4
     * versionName : 0.0.4-alpha
     * url : http://www.minamion.com/Akashi/akashitoolkit-alpha-3.apk
     * change : 测试
     测试测试
     QAQ
     */

    private int versionCode;
    private String versionName;
    private String url;
    private String change;

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getUrl() {
        return url;
    }

    public String getChange() {
        return change;
    }
}
