package com.urgoo.domain;

import java.util.ArrayList;

/**
 * Created by duanfei on 2016/6/14.
 */
public class NowMakeBean {
    private String lastTime;
    private String isPotentialClient;
    private String cnName;
    private String organization;
    private String enName;
    private String count;
    private String countryName;
    private String workYear;
    private String userIcon;
    private String goodField;
    private String advancedTime;
    private String historyCount;
    private String closedTime;
    private String ratio;
    private String today;

    public String getToday() {
        return today;
    }

    public void setToday(String mToday) {
        today = mToday;
    }

    public String getAdvancedTime() {
        return advancedTime;
    }

    public void setAdvancedTime(String mAdvancedTime) {
        advancedTime = mAdvancedTime;
    }

    public String getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(String mHistoryCount) {
        historyCount = mHistoryCount;
    }

    public String getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(String mClosedTime) {
        closedTime = mClosedTime;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String mRatio) {
        ratio = mRatio;
    }

    public String getGoodField() {
        return goodField;
    }

    public void setGoodField(String mGoodField) {
        goodField = mGoodField;
    }

    private ArrayList<AdvanceBean> advanceDateList;

    public NowMakeBean(String today,String lastTime, String goodField, String isPotentialClient, String cnName, String advancedTime,
                       String enName, String count, String countryName, String historyCount, String closedTime, String userIcon, String ratio) {
        this.today=today;
        this.lastTime = lastTime;
        this.goodField = goodField;
        this.isPotentialClient = isPotentialClient;
        this.cnName = cnName;
        this.advancedTime = advancedTime;
        this.enName = enName;
        this.count = count;
        this.countryName = countryName;
        this.historyCount = historyCount;
        this.closedTime = closedTime;
        this.userIcon = userIcon;
        this.ratio = ratio;

    }

    @Override
    public String toString() {
        return "NowMakeBean{" +
                "lastTime='" + lastTime + '\'' +
                ", isPotentialClient='" + isPotentialClient + '\'' +
                ", cnName='" + cnName + '\'' +
                ", organization='" + organization + '\'' +
                ", enName='" + enName + '\'' +
                ", count='" + count + '\'' +
                ", countryName='" + countryName + '\'' +
                ", workYear='" + workYear + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", today='" + today + '\'' +
                '}';
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String mLastTime) {
        lastTime = mLastTime;
    }

    public String getIsPotentialClient() {
        return isPotentialClient;
    }

    public void setIsPotentialClient(String mIsPotentialClient) {
        isPotentialClient = mIsPotentialClient;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String mOrganization) {
        organization = mOrganization;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String mCnName) {
        cnName = mCnName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String mCount) {
        count = mCount;
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

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String mUserIcon) {
        userIcon = mUserIcon;
    }

    public ArrayList<AdvanceBean> getAdvanceDateList() {
        return advanceDateList;
    }

    public void setAdvanceDateList(ArrayList<AdvanceBean> mAdvanceDateList) {
        advanceDateList = mAdvanceDateList;
    }

    public static class AdvanceBean {
        private String date;
        private String advanceDate;
        private String weekName;
        private String dataYanlong;
        private String busyDay;
        private ArrayList<ListBean> timeList;

        @Override
        public String toString() {
            return "AdvanceBean{" +
                    "date='" + date + '\'' +
                    ", advanceDate='" + advanceDate + '\'' +
                    ", weekName='" + weekName + '\'' +
                    ", timeList=" + timeList +
                    ", dataYanlong=" + dataYanlong +
                    ", busyDay=" + busyDay +
                    '}';
        }

        public String getBusyDay() {
            return busyDay;
        }

        public void setBusyDay(String mBusyDay) {
            busyDay = mBusyDay;
        }

        public String getDataYanlong() {
            return dataYanlong;
        }

        public void setDataYanlong(String mDataYanlong) {
            dataYanlong = mDataYanlong;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String mDate) {
            date = mDate;
        }

        public String getAdvanceDate() {
            return advanceDate;
        }

        public void setAdvanceDate(String mAdvanceDate) {
            advanceDate = mAdvanceDate;
        }

        public String getWeekName() {
            return weekName;
        }

        public void setWeekName(String mWeekName) {
            weekName = mWeekName;
        }

        public ArrayList<ListBean> getTimeList() {
            return timeList;
        }

        public void setTimeList(ArrayList<ListBean> mTimeList) {
            timeList = mTimeList;
        }

        public static class ListBean {
            private String advanceTimeId;
            private String cnStartTime;
            private String otherStartTime;
            private String otherEndTime;
            private String cnEndTime;
            private String type;

            @Override
            public String toString() {
                return "ListBean{" +
                        "advanceTimeId='" + advanceTimeId + '\'' +
                        ", cnStartTime='" + cnStartTime + '\'' +
                        ", otherStartTime='" + otherStartTime + '\'' +
                        ", otherEndTime='" + otherEndTime + '\'' +
                        ", cnEndTime='" + cnEndTime + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }

            public String getAdvanceTimeId() {
                return advanceTimeId;
            }

            public void setAdvanceTimeId(String mAdvanceTimeId) {
                advanceTimeId = mAdvanceTimeId;
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

            public String getType() {
                return type;
            }

            public void setType(String mType) {
                type = mType;
            }
        }
    }
}
