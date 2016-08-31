package com.urgoo.domain;

import java.util.ArrayList;

/**
 * Created by duanfei on 2016/6/14.
 */
public class myorderconComBean {
    private String lastTime;
    private String message;
    private String hxCode;
    private String counselorId;
    private String goodField;
    private String isPotentialClient;
    private String cnName;
    private String organization;
    private String advanceId;
    private String enName;
    private String countryName;
    private String workYear;
    private String state;
    private String historyCount;
    private String status;
    private String userIcon;
    private String isAdvanceRelation;
    private String ratio;
    private String advancedTime;
    private String closedTime;
    private String reason;
    private ArrayList<ComBean> advanceDetailList;

    public String getIsAdvanceRelation() {
        return isAdvanceRelation;
    }

    public void setIsAdvanceRelation(String mIsAdvanceRelation) {
        isAdvanceRelation = mIsAdvanceRelation;
    }

    public myorderconComBean(String mLastTime, String mMessage, String mHxCode, String mCounselorId,
                             String mGoodField, String mIsPotentialClient, String mCnName, String mOrganization,
                             String mAdvanceId, String mEnName, String mCountryName, String mWorkYear,
                             String mState, String mHistoryCount, String mStatus, String muserIcon, String misAdvanceRelation
            , String ratios, String advancedTimes, String closedTimes,String reasons) {
        lastTime = mLastTime;
        message = mMessage;
        hxCode = mHxCode;
        counselorId = mCounselorId;
        goodField = mGoodField;
        isPotentialClient = mIsPotentialClient;
        cnName = mCnName;
        organization = mOrganization;
        advanceId = mAdvanceId;
        enName = mEnName;
        countryName = mCountryName;
        workYear = mWorkYear;
        state = mState;
        historyCount = mHistoryCount;
        status = mStatus;
        userIcon = muserIcon;
        isAdvanceRelation = misAdvanceRelation;
        ratio = ratios;
        advancedTime = advancedTimes;
        closedTime = closedTimes;
        reason = reasons;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String mReason) {
        reason = mReason;
    }

    @Override
    public String toString() {
        return "myorderconComBean{" +
                "lastTime='" + lastTime + '\'' +
                ", message='" + message + '\'' +
                ", hxCode='" + hxCode + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", goodField='" + goodField + '\'' +
                ", isPotentialClient='" + isPotentialClient + '\'' +
                ", cnName='" + cnName + '\'' +
                ", organization='" + organization + '\'' +
                ", advanceId='" + advanceId + '\'' +
                ", enName='" + enName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", workYear='" + workYear + '\'' +
                ", state='" + state + '\'' +
                ", historyCount='" + historyCount + '\'' +
                ", status='" + status + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", advanceDetailList=" + advanceDetailList +
                ", isAdvanceRelation=" + isAdvanceRelation +
                '}';
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String mRatio) {
        ratio = mRatio;
    }

    public String getAdvancedTime() {
        return advancedTime;
    }

    public void setAdvancedTime(String mAdvancedTime) {
        advancedTime = mAdvancedTime;
    }

    public String getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(String mClosedTime) {
        closedTime = mClosedTime;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String mUserIcon) {
        userIcon = mUserIcon;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String mLastTime) {
        lastTime = mLastTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mMessage) {
        message = mMessage;
    }

    public String getHxCode() {
        return hxCode;
    }

    public void setHxCode(String mHxCode) {
        hxCode = mHxCode;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String mCounselorId) {
        counselorId = mCounselorId;
    }

    public String getGoodField() {
        return goodField;
    }

    public void setGoodField(String mGoodField) {
        goodField = mGoodField;
    }

    public String getIsPotentialClient() {
        return isPotentialClient;
    }

    public void setIsPotentialClient(String mIsPotentialClient) {
        isPotentialClient = mIsPotentialClient;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String mCnName) {
        cnName = mCnName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String mOrganization) {
        organization = mOrganization;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String mCountryName) {
        countryName = mCountryName;
    }

    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String mWorkYear) {
        workYear = mWorkYear;
    }

    public String getState() {
        return state;
    }

    public void setState(String mState) {
        state = mState;
    }

    public String getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(String mHistoryCount) {
        historyCount = mHistoryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String mStatus) {
        status = mStatus;
    }

    public static class ComBean {
        private String advanceDate;
        private String cnStartTime;
        private String otherStartTime;
        private String otherEndTime;
        private String advanceId;
        private String cnEndTime;
        private String advanceDetailId;
        private String advanceDateCn;
        private String status;

        @Override
        public String toString() {
            return "ComBean{" +
                    "advanceDate='" + advanceDate + '\'' +
                    ", cnStartTime='" + cnStartTime + '\'' +
                    ", otherStartTime='" + otherStartTime + '\'' +
                    ", otherEndTime='" + otherEndTime + '\'' +
                    ", advanceId='" + advanceId + '\'' +
                    ", cnEndTime='" + cnEndTime + '\'' +
                    ", advanceDetailId='" + advanceDetailId + '\'' +
                    ", advanceDateCn='" + advanceDateCn + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String mStatus) {
            status = mStatus;
        }

        public String getAdvanceDateCn() {
            return advanceDateCn;
        }

        public void setAdvanceDateCn(String mAdvanceDateCn) {
            advanceDateCn = mAdvanceDateCn;
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

        public String getCnEndTime() {
            return cnEndTime;
        }

        public void setCnEndTime(String mCnEndTime) {
            cnEndTime = mCnEndTime;
        }

        public String getAdvanceDetailId() {
            return advanceDetailId;
        }

        public void setAdvanceDetailId(String mAdvanceDetailId) {
            advanceDetailId = mAdvanceDetailId;
        }
    }


}
