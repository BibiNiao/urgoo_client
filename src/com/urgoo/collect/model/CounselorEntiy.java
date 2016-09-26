package com.urgoo.collect.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by bb on 2016/7/15.
 */
public class CounselorEntiy implements Parcelable {
    private String userIcon;
    private String cnName;
    private String school;
    private String enName;
    private String slogan;
    private String habitualResidence;
    private String counselorId;
    private List<String> serviceTypeArray;
    private List<String> tagArray;

    protected CounselorEntiy(Parcel in) {
        userIcon = in.readString();
        cnName = in.readString();
        school = in.readString();
        enName = in.readString();
        slogan = in.readString();
        habitualResidence = in.readString();
        counselorId = in.readString();
        serviceTypeArray = in.createStringArrayList();
        tagArray = in.createStringArrayList();
    }

    public static final Creator<CounselorEntiy> CREATOR = new Creator<CounselorEntiy>() {
        @Override
        public CounselorEntiy createFromParcel(Parcel in) {
            return new CounselorEntiy(in);
        }

        @Override
        public CounselorEntiy[] newArray(int size) {
            return new CounselorEntiy[size];
        }
    };

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getHabitualResidence() {
        return habitualResidence;
    }

    public void setHabitualResidence(String habitualResidence) {
        this.habitualResidence = habitualResidence;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    public List<String> getServiceTypeArray() {
        return serviceTypeArray;
    }

    public void setServiceTypeArray(List<String> serviceTypeArray) {
        this.serviceTypeArray = serviceTypeArray;
    }

    public List<String> getTagArray() {
        return tagArray;
    }

    public void setTagArray(List<String> tagArray) {
        this.tagArray = tagArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userIcon);
        dest.writeString(cnName);
        dest.writeString(school);
        dest.writeString(enName);
        dest.writeString(slogan);
        dest.writeString(habitualResidence);
        dest.writeString(counselorId);
        dest.writeStringList(serviceTypeArray);
        dest.writeStringList(tagArray);
    }
}
