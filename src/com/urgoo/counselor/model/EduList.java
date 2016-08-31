package com.urgoo.counselor.model;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class EduList {

    private int educationId;
    private String major;
    private String educationName;
    private String startTime;
    private String endTime;

    @Override
    public String toString() {
        return "EduList{" +
                "educationId=" + educationId +
                ", major='" + major + '\'' +
                ", educationName='" + educationName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public int getEducationId() {
        return educationId;
    }

    public void setEducationId(int mEducationId) {
        educationId = mEducationId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String mMajor) {
        major = mMajor;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String mEducationName) {
        educationName = mEducationName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String mStartTime) {
        startTime = mStartTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String mEndTime) {
        endTime = mEndTime;
    }
}
