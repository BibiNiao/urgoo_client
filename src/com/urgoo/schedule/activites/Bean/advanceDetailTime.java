package com.urgoo.schedule.activites.Bean;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class advanceDetailTime {

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
        return "advanceDetailTime{" +
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
}
