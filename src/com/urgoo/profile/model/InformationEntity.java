package com.urgoo.profile.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by bb on 2016/9/8.
 */
public class InformationEntity implements Parcelable, Serializable {
    private String insertDatetime;
    /**
     * 1是已读 2是未读
     */
    private int unread;
    private String typeName;
    private int informationId;
    private String title;
    private int type;
    private String content;

    protected InformationEntity(Parcel in) {
        insertDatetime = in.readString();
        unread = in.readInt();
        typeName = in.readString();
        informationId = in.readInt();
        title = in.readString();
        type = in.readInt();
        content = in.readString();
    }

    public static final Creator<InformationEntity> CREATOR = new Creator<InformationEntity>() {
        @Override
        public InformationEntity createFromParcel(Parcel in) {
            return new InformationEntity(in);
        }

        @Override
        public InformationEntity[] newArray(int size) {
            return new InformationEntity[size];
        }
    };

    public String getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(String insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getInformationId() {
        return informationId;
    }

    public void setInformationId(int informationId) {
        this.informationId = informationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(insertDatetime);
        dest.writeInt(unread);
        dest.writeString(typeName);
        dest.writeInt(informationId);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeString(content);
    }
}
