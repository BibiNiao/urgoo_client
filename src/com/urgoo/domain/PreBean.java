package com.urgoo.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lijie on 2016/6/8.
 */
public class PreBean implements Serializable {

    private AdvanceCounselorInfoEntity advanceCounselorInfo;
    private ArrayList<AdvanceDateListEntity> advanceDateList;

    @Override
    public String toString() {
        return "PreBean{" +
                "advanceCounselorInfo=" + advanceCounselorInfo +
                ", advanceDateList=" + advanceDateList +
                '}';
    }

    public AdvanceCounselorInfoEntity getAdvanceCounselorInfo() {
        return advanceCounselorInfo;
    }

    public void setAdvanceCounselorInfo(AdvanceCounselorInfoEntity advanceCounselorInfo) {
        this.advanceCounselorInfo = advanceCounselorInfo;
    }

    public ArrayList<AdvanceDateListEntity> getAdvanceDateList() {
        return advanceDateList;
    }

    public void setAdvanceDateList(ArrayList<AdvanceDateListEntity> advanceDateList) {
        this.advanceDateList = advanceDateList;
    }

    public static class AdvanceCounselorInfoEntity {
        private String cnName;
        private String enName;
        private int count;
        private String countryName;
        private String userIcon;
        private String organization;
        private String workYear;

        @Override
        public String toString() {
            return "AdvanceCounselorInfoEntity{" +
                    "cnName='" + cnName + '\'' +
                    ", enName='" + enName + '\'' +
                    ", count=" + count +
                    ", countryName='" + countryName + '\'' +
                    ", userIcon='" + userIcon + '\'' +
                    ", organization='" + organization + '\'' +
                    ", workYear='" + workYear + '\'' +
                    '}';
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getWorkYear() {
            return workYear;
        }

        public void setWorkYear(String workYear) {
            this.workYear = workYear;
        }

        public String getCnName() {
            return cnName;
        }

        public void setCnName(String cnName) {
            this.cnName = cnName;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }
    }

    public static class AdvanceDateListEntity {
        private String date;
        private String advanceDate;
        private String weekName;
        private ArrayList<TimeList> timeList;

        @Override
        public String toString() {
            return "AdvanceDateListEntity{" +
                    "date='" + date + '\'' +
                    ", advanceDate='" + advanceDate + '\'' +
                    ", weekName='" + weekName + '\'' +
                    ", timeList=" + timeList +
                    '}';
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAdvanceDate() {
            return advanceDate;
        }

        public void setAdvanceDate(String advanceDate) {
            this.advanceDate = advanceDate;
        }

        public String getWeekName() {
            return weekName;
        }

        public void setWeekName(String weekName) {
            this.weekName = weekName;
        }

        public ArrayList<TimeList> getTimeList() {
            return timeList;
        }

        public void setTimeList(ArrayList<TimeList> timeList) {
            this.timeList = timeList;
        }
    }

    public static class TimeList {
        private String advanceTimeId;
        private String cnStartTime;
        private String cnEndTime;
        private String type;
        private String otherStartTime;
        private String otherEndTime;

        @Override
        public String toString() {
            return "TimeList{" +
                    "advanceTimeId='" + advanceTimeId + '\'' +
                    ", cnStartTime='" + cnStartTime + '\'' +
                    ", cnEndTime='" + cnEndTime + '\'' +
                    ", type='" + type + '\'' +
                    ", otherStartTime='" + otherStartTime + '\'' +
                    ", otherEndTime='" + otherEndTime + '\'' +
                    '}';
        }

        public String getAdvanceTimeId() {
            return advanceTimeId;
        }

        public void setAdvanceTimeId(String advanceTimeId) {
            this.advanceTimeId = advanceTimeId;
        }

        public String getCnStartTime() {
            return cnStartTime;
        }

        public void setCnStartTime(String cnStartTime) {
            this.cnStartTime = cnStartTime;
        }

        public String getCnEndTime() {
            return cnEndTime;
        }

        public void setCnEndTime(String cnEndTime) {
            this.cnEndTime = cnEndTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOtherStartTime() {
            return otherStartTime;
        }

        public void setOtherStartTime(String otherStartTime) {
            this.otherStartTime = otherStartTime;
        }

        public String getOtherEndTime() {
            return otherEndTime;
        }

        public void setOtherEndTime(String otherEndTime) {
            this.otherEndTime = otherEndTime;
        }
    }
}

