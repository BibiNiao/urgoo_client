package com.urgoo.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanfei on 2016/6/14.
 */
public class tobeconfirmedmyorderBean {
    private String TobaBean;
    private String cnName;
    private String advanceId;
    private String enName;
    private String type;
    private String lastTime;
    private String historyCount;
    private String userIcon;
    private String status;
    private String state;



    private ArrayList<AdvanceBean> advanceDetailList;

    @Override
    public String toString() {
        return "tobeconfirmedmyorderBean{" +
                "state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", historyCount='" + historyCount + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", type='" + type + '\'' +
                ", enName='" + enName + '\'' +
                ", advanceId='" + advanceId + '\'' +
                ", cnName='" + cnName + '\'' +
                ", TobaBean='" + TobaBean + '\'' +
                '}';
    }


    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String mLastTime) {
        lastTime = mLastTime;
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

    public String getTobaBean() {
        return TobaBean;
    }

    public void setTobaBean(String mTobaBean) {
        TobaBean = mTobaBean;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String mCnName) {
        cnName = mCnName;
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

    public String getType() {
        return type;
    }

    public String getState() {
        return state;
    }

    public void setState(String mState) {
        state = mState;
    }

    public void setType(String mType) {
        type = mType;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String mUserIcon) {
        userIcon = mUserIcon;
    }

    public ArrayList<AdvanceBean> getAdvanceDetailList() {
        return advanceDetailList;
    }

    public void setAdvanceDetailList(ArrayList<AdvanceBean> mAdvanceDetailList) {
        advanceDetailList = mAdvanceDetailList;
    }

    public static class AdvanceBean {
        private String advanceDate;
        private String cnStartTime;
        private String otherStartTime;
        private String otherEndTime;
        private String cnEndTime;
        private String status;


        @Override
        public String toString() {
            return "AdvanceBean{" +
                    "advanceDate='" + advanceDate + '\'' +
                    ", cnStartTime='" + cnStartTime + '\'' +
                    ", otherStartTime='" + otherStartTime + '\'' +
                    ", otherEndTime='" + otherEndTime + '\'' +
                    ", cnEndTime='" + cnEndTime + '\'' +
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

    }
}
