package com.urgoo.counselor.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.urgoo.domain.ShareDetail;

import java.util.List;

/**
 * Created by bb on 2016/10/8.
 */
public class Counselor implements Parcelable {
    private String userIcon;
    private String enName;
    private String shareVedio;
    private String school;
    /**
     * 居住地
     */
    private String habitualResidence;
    private String counselorId;
    private com.urgoo.domain.ShareDetail shareDetail;
    /**
     * 学生评价
     */
    private String studentWords;
    /**
     * 星评价 除以2
     */
    private float starMark;
    /**
     * linkin地址
     */
    private String Linkedin;
    /**
     * 1是收藏 0没收藏
     */
    private String isAttention;
    /**
     * 资格认证
     */
    private transient List<String> orgnizationList;

    protected Counselor(Parcel in) {
        userIcon = in.readString();
        enName = in.readString();
        shareVedio = in.readString();
        school = in.readString();
        habitualResidence = in.readString();
        counselorId = in.readString();
        studentWords = in.readString();
        starMark = in.readFloat();
        Linkedin = in.readString();
        isAttention = in.readString();
        orgnizationList = in.createStringArrayList();
    }

    public static final Creator<Counselor> CREATOR = new Creator<Counselor>() {
        @Override
        public Counselor createFromParcel(Parcel in) {
            return new Counselor(in);
        }

        @Override
        public Counselor[] newArray(int size) {
            return new Counselor[size];
        }
    };

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getShareVedio() {
        return shareVedio;
    }

    public void setShareVedio(String shareVedio) {
        this.shareVedio = shareVedio;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    public com.urgoo.domain.ShareDetail getShareDetail() {
        return shareDetail;
    }

    public void setShareDetail(ShareDetail shareDetail) {
        this.shareDetail = shareDetail;
    }

    public String getStudentWords() {
        return studentWords;
    }

    public void setStudentWords(String studentWords) {
        this.studentWords = studentWords;
    }

    public float getStarMark() {
        return starMark;
    }

    public void setStarMark(float starMark) {
        this.starMark = starMark;
    }

    public String getLinkedin() {
        return Linkedin;
    }

    public void setLinkedin(String linkedin) {
        Linkedin = linkedin;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public List<String> getOrgnizationList() {
        return orgnizationList;
    }

    public void setOrgnizationList(List<String> orgnizationList) {
        this.orgnizationList = orgnizationList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userIcon);
        dest.writeString(enName);
        dest.writeString(shareVedio);
        dest.writeString(school);
        dest.writeString(habitualResidence);
        dest.writeString(counselorId);
        dest.writeString(studentWords);
        dest.writeFloat(starMark);
        dest.writeString(Linkedin);
        dest.writeString(isAttention);
        dest.writeStringList(orgnizationList);
    }
}
