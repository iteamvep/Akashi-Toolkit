package rikka.akashitoolkit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rikka on 2016/3/7.
 */
public class Twitter implements Parcelable {

    private String id;
    private String img;
    private String jp;
    private String zh;
    private String date;
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.img);
        dest.writeString(this.jp);
        dest.writeString(this.zh);
        dest.writeString(this.date);
        dest.writeLong(this.timestamp);
    }

    public Twitter() {
    }

    protected Twitter(Parcel in) {
        this.id = in.readString();
        this.img = in.readString();
        this.jp = in.readString();
        this.zh = in.readString();
        this.date = in.readString();
        this.timestamp = in.readLong();
    }

    public static final Parcelable.Creator<Twitter> CREATOR = new Parcelable.Creator<Twitter>() {
        @Override
        public Twitter createFromParcel(Parcel source) {
            return new Twitter(source);
        }

        @Override
        public Twitter[] newArray(int size) {
            return new Twitter[size];
        }
    };
}
