package rikka.akashitoolkit.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/3/19.
 */
public class MessageReadStatus {

    public MessageReadStatus() {
        readMessageId = new ArrayList<>();
        ids = new ArrayList<>();
    }

    @Expose
    private int versionCode;
    @Expose
    private List<Long> readMessageId;
    @Expose
    private boolean pushIntro;

    private List<Long> ids;

    public void addId(long id) {
        ids.add(id);
    }

    public void addReadId(long id) {
        if (!readMessageId.contains(id)) {
            readMessageId.add(id);
        }
    }

    public void clearId() {
        ids.clear();
    }

    public void clearReadIdNotExisted() {
        List<Long> _messageId = new ArrayList<>();
        for (Long id : readMessageId) {
            if (ids.contains(id)) {
                _messageId.add(id);
            }
        }
        readMessageId.clear();
        readMessageId.addAll(_messageId);
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public boolean isIdRead(long id) {
        return readMessageId.contains(id);
    }

    //关掉显示Introduction our new push feature
    public boolean showPushIntro() {
        //return !pushIntro;
        return false;
    }

    public void setShowPushIntro(boolean show) {
        this.pushIntro = !show;
    }
}
