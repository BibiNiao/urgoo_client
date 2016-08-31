package com.urgoo.domain;

import java.util.ArrayList;

/**
 * Created by duanfei on 2016/6/14.
 */
public class myordercontentBean {
    private String cnName;
    private String organization;
    private String advanceId;
    private String enName;
    private String countryName;
    private String workYear;
    private String state;
    private String message;
    private String userIcon;
    private String status;
    private String historyCount;
    private String lastTime;
    private String goodField;
    private String hxCode;
    private String counselorId;
    private String isPotentialClient;
    private String ratio;
    private String advancedTime;
    private String closedTime;
    private ArrayList<TimeBean> advanceDetailList;
    private ArrayList<TimeConBean> advanceDetail;
    private ArrayList<DetailTimeBean> advanceDetailTime;

    public String getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(String mClosedTime) {
        closedTime = mClosedTime;
    }

    public String getAdvancedTime() {
        return advancedTime;
    }

    public void setAdvancedTime(String mAdvancedTime) {
        advancedTime = mAdvancedTime;
    }

    public String getIsPotentialClient() {
        return isPotentialClient;
    }

    public void setIsPotentialClient(String mIsPotentialClient) {
        isPotentialClient = mIsPotentialClient;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String mRatio) {
        ratio = mRatio;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String mCounselorId) {
        counselorId = mCounselorId;
    }

    public String getHxCode() {
        return hxCode;
    }

    public void setHxCode(String mHxCode) {
        hxCode = mHxCode;
    }

    public String getGoodField() {
        return goodField;
    }

    public void setGoodField(String mGoodField) {
        goodField = mGoodField;
    }

    public myordercontentBean(String mCnName, String mOrganization, String mAdvanceId, String mEnName, String mCountryName,
                              String mWorkYear, String mState, String mMessage, String mUserIcon, String mStatus,
                              String mHistoryCount, String mLastTime, String mGoodField, String mhxCode, String mcounselorId
            , String misPotentialClient, String ratios, String advancedTimes, String closedTimes) {
        cnName = mCnName;
        organization = mOrganization;
        advanceId = mAdvanceId;
        enName = mEnName;
        countryName = mCountryName;
        workYear = mWorkYear;
        state = mState;
        message = mMessage;
        userIcon = mUserIcon;
        status = mStatus;
        historyCount = mHistoryCount;
        lastTime = mLastTime;
        goodField = mGoodField;
        hxCode = mhxCode;
        counselorId = mcounselorId;
        isPotentialClient = misPotentialClient;
        ratio = ratios;
        advancedTime = advancedTimes;
        closedTime = closedTimes;
    }

    public static class TimeBean {
        private String advanceDate;
        private String cnStartTime;
        private String otherStartTime;
        private String otherEndTime;
        private String advanceId;
        private String cnEndTime;
        private String advanceDetailId;
        private String advanceDateCn;

        @Override
        public String toString() {
            return "TimeBean{" +
                    "advanceDate='" + advanceDate + '\'' +
                    ", cnStartTime='" + cnStartTime + '\'' +
                    ", otherStartTime='" + otherStartTime + '\'' +
                    ", otherEndTime='" + otherEndTime + '\'' +
                    ", advanceId='" + advanceId + '\'' +
                    ", cnEndTime='" + cnEndTime + '\'' +
                    ", advanceDetailId='" + advanceDetailId + '\'' +
                    ", advanceDateCn='" + advanceDateCn + '\'' +
                    '}';
        }


        public String getAdvanceDateCn() {
            return advanceDateCn;
        }

        public void setAdvanceDateCn(String mAdvanceDateCn) {
            advanceDateCn = mAdvanceDateCn;
        }

        public String getAdvanceId() {
            return advanceId;
        }

        public void setAdvanceId(String mAdvanceId) {
            advanceId = mAdvanceId;
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

    public static class TimeConBean {
        private String date;
        private String daojishi;
        private String cnStartTime;
        private String advanceDate;
        private String advanceDateCn;
        private String otherStartTime;
        private String cnName;
        private String shifoujinxing;
        private String otherEndTime;
        private String organization;
        private String advanceId;
        private String enName;
        private String cnEndTime;
        private String time;
        private String countryName;
        private String workYear;
        private String state;
        private String userIcon;
        private String status;
        private String historyCount;
        private String lastTime;
        private String message;
        private String goodField;
        private String hxCode;
        private String ratio;
        private String advancedTime;
        private String closedTime;

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

        public TimeConBean(String mDate, String mDaojishi, String mCnStartTime, String mAdvanceDate, String mOtherStartTime, String mCnName, String mShifoujinxing,
                           String mOtherEndTime, String mOrganization, String mAdvanceId, String mEnName, String mCnEndTime, String mTime, String mCountryName,
                           String mWorkYear, String mState, String mUserIcon, String mStatus, String mHistoryCount, String mLastTime, String mmessage,
                           String mgoodField, String mhxCode, String advanceDateCns, String ratios, String advancedTimes, String closedTimes) {
            date = mDate;
            daojishi = mDaojishi;
            cnStartTime = mCnStartTime;
            advanceDate = mAdvanceDate;
            otherStartTime = mOtherStartTime;
            cnName = mCnName;
            shifoujinxing = mShifoujinxing;
            otherEndTime = mOtherEndTime;
            organization = mOrganization;
            advanceId = mAdvanceId;
            enName = mEnName;
            cnEndTime = mCnEndTime;
            time = mTime;
            countryName = mCountryName;
            workYear = mWorkYear;
            state = mState;
            userIcon = mUserIcon;
            status = mStatus;
            historyCount = mHistoryCount;
            lastTime = mLastTime;
            message = mmessage;
            goodField = mgoodField;
            hxCode = mhxCode;
            advanceDateCn = advanceDateCns;
            ratio = ratios;
            advancedTime = advancedTimes;
            closedTime = closedTimes;
        }

        @Override
        public String toString() {
            return "TimeConBean{" +
                    "date='" + date + '\'' +
                    ", daojishi='" + daojishi + '\'' +
                    ", cnStartTime='" + cnStartTime + '\'' +
                    ", advanceDate='" + advanceDate + '\'' +
                    ", otherStartTime='" + otherStartTime + '\'' +
                    ", cnName='" + cnName + '\'' +
                    ", shifoujinxing='" + shifoujinxing + '\'' +
                    ", otherEndTime='" + otherEndTime + '\'' +
                    ", organization='" + organization + '\'' +
                    ", advanceId='" + advanceId + '\'' +
                    ", enName='" + enName + '\'' +
                    ", cnEndTime='" + cnEndTime + '\'' +
                    ", time='" + time + '\'' +
                    ", countryName='" + countryName + '\'' +
                    ", workYear='" + workYear + '\'' +
                    ", state='" + state + '\'' +
                    ", userIcon='" + userIcon + '\'' +
                    ", status='" + status + '\'' +
                    ", message='" + message + '\'' +
                    ", goodField='" + goodField + '\'' +
                    ", hxCode='" + hxCode + '\'' +
                    ", advanceDateCn='" + advanceDateCn + '\'' +
                    '}';
        }

        public String getAdvanceDateCn() {
            return advanceDateCn;
        }

        public void setAdvanceDateCn(String mAdvanceDateCn) {
            advanceDateCn = mAdvanceDateCn;
        }

        public String getHxCode() {
            return hxCode;
        }

        public void setHxCode(String mHxCode) {
            hxCode = mHxCode;
        }

        public String getGoodField() {
            return goodField;
        }

        public void setGoodField(String mGoodField) {
            goodField = mGoodField;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String mMessage) {
            message = mMessage;
        }

        public String getHistoryCount() {
            return historyCount;
        }

        public void setHistoryCount(String mHistoryCount) {
            historyCount = mHistoryCount;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String mLastTime) {
            lastTime = mLastTime;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String mDate) {
            date = mDate;
        }

        public String getDaojishi() {
            return daojishi;
        }

        public void setDaojishi(String mDaojishi) {
            daojishi = mDaojishi;
        }

        public String getCnStartTime() {
            return cnStartTime;
        }

        public void setCnStartTime(String mCnStartTime) {
            cnStartTime = mCnStartTime;
        }

        public String getAdvanceDate() {
            return advanceDate;
        }

        public void setAdvanceDate(String mAdvanceDate) {
            advanceDate = mAdvanceDate;
        }

        public String getOtherStartTime() {
            return otherStartTime;
        }

        public void setOtherStartTime(String mOtherStartTime) {
            otherStartTime = mOtherStartTime;
        }

        public String getCnName() {
            return cnName;
        }

        public void setCnName(String mCnName) {
            cnName = mCnName;
        }

        public String getShifoujinxing() {
            return shifoujinxing;
        }

        public void setShifoujinxing(String mShifoujinxing) {
            shifoujinxing = mShifoujinxing;
        }

        public String getOtherEndTime() {
            return otherEndTime;
        }

        public void setOtherEndTime(String mOtherEndTime) {
            otherEndTime = mOtherEndTime;
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

        public String getCnEndTime() {
            return cnEndTime;
        }

        public void setCnEndTime(String mCnEndTime) {
            cnEndTime = mCnEndTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String mTime) {
            time = mTime;
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

    public static class DetailTimeBean {

        private String advanceDate;
        private String cnStartTime;
        private String otherStartTime;
        private String otherEndTime;
        private String advanceId;
        private String cnEndTime;
        private String advanceDetailId;
        private String advanceDateCn;
        private String status;


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

        public String getAdvanceDateCn() {
            return advanceDateCn;
        }

        public void setAdvanceDateCn(String mAdvanceDateCn) {
            advanceDateCn = mAdvanceDateCn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String mStatus) {
            status = mStatus;
        }


        @Override
        public String toString() {
            return "DetailTimeBean{" +
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
    }

    public String getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(String mHistoryCount) {
        historyCount = mHistoryCount;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String mLastTime) {
        lastTime = mLastTime;
    }

    @Override
    public String toString() {
        return "myordercontentBean{" +
                "isPotentialClient='" + isPotentialClient + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", hxCode='" + hxCode + '\'' +
                ", goodField='" + goodField + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", historyCount='" + historyCount + '\'' +
                ", status='" + status + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", message='" + message + '\'' +
                ", state='" + state + '\'' +
                ", workYear='" + workYear + '\'' +
                ", countryName='" + countryName + '\'' +
                ", enName='" + enName + '\'' +
                ", advanceId='" + advanceId + '\'' +
                ", organization='" + organization + '\'' +
                ", cnName='" + cnName + '\'' +
                '}';
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String mMessage) {
        message = mMessage;
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
