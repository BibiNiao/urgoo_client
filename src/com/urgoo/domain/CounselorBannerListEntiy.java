package com.urgoo.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bb on 2016/7/14.
 */
public class CounselorBannerListEntiy implements Parcelable {
    /**
     * 图片
     */
    private String picUrl;
    /**
     * 图标描述
     */
    private String des;
    /**
     * 访问类型
     */
    private String type;

    private CounselorBannerListEntiy(Parcel in) {
        picUrl = in.readString();
        des = in.readString();
        type = in.readString();
    }

    public static final Creator<CounselorBannerListEntiy> CREATOR = new Creator<CounselorBannerListEntiy>() {
        @Override
        public CounselorBannerListEntiy createFromParcel(Parcel in) {
            return new CounselorBannerListEntiy(in);
        }

        @Override
        public CounselorBannerListEntiy[] newArray(int size) {
            return new CounselorBannerListEntiy[size];
        }
    };

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picUrl);
        dest.writeString(des);
        dest.writeString(type);
    }
}
