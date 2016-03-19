package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/3/19.
 */
public class MessageReadStatus {

    /**
     * versionCode : 7
     * messageId : [-1,0,1]
     */

    private int versionCode;
    private List<Integer> messageId;

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setMessageId(List<Integer> messageId) {
        this.messageId = messageId;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public List<Integer> getMessageId() {
        return messageId;
    }
}
