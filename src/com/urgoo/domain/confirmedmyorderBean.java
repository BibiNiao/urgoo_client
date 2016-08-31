package com.urgoo.domain;

import java.util.ArrayList;

/**
 * Created by duanfei on 2016/6/16.
 */
public class confirmedmyorderBean {
    private String advanceDate;
    private String cnStartTime;
    private String cnName;
    private String otherStartTime;
    private String otherEndTime;
    private String advanceId;
    private String enName;
    private String cnEndTime;
    private String state;
    private String userIcon;
    private String status;

    @Override
    public String toString() {
        return "confirmedmyorderBean{" +
                "advanceDate='" + advanceDate + '\'' +
                ", cnStartTime='" + cnStartTime + '\'' +
                ", cnName='" + cnName + '\'' +
                ", otherStartTime='" + otherStartTime + '\'' +
                ", otherEndTime='" + otherEndTime + '\'' +
                ", advanceId='" + advanceId + '\'' +
                ", enName='" + enName + '\'' +
                ", cnEndTime='" + cnEndTime + '\'' +
                ", state='" + state + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(String mAdvanceDate) {
        advanceDate = mAdvanceDate;
    }

    public String getCnStartTime() {
        return cnStartTime;
    }

    public void setCnStartTime(String mCnStartTime) {
        cnStartTime = mCnStartTime;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String mCnName) {
        cnName = mCnName;
    }

    public String getOtherStartTime() {
        return otherStartTime;
    }

    public void setOtherStartTime(String mOtherStartTime) {
        otherStartTime = mOtherStartTime;
    }

    public String getOtherEndTime() {
        return otherEndTime;
    }

    public void setOtherEndTime(String mOtherEndTime) {
        otherEndTime = mOtherEndTime;
    }

    public String getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(String mAdvanceId) {
        advanceId = mAdvanceId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String mEnName) {
        enName = mEnName;
    }

    public String getCnEndTime() {
        return cnEndTime;
    }

    public void setCnEndTime(String mCnEndTime) {
        cnEndTime = mCnEndTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String mState) {
        state = mState;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String mUserIcon) {
        userIcon = mUserIcon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String mStatus) {
        status = mStatus;
    }
}
