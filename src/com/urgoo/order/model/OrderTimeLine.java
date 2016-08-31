package com.urgoo.order.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by bb on 2016/8/12.
 */
public class OrderTimeLine implements Parcelable {
    private String workDayPercent;
    private String workDay;
    private List<PayTimeListEntity> payTimeList;

    public List<PayTimeListEntity> getPayTimeList() {
        return payTimeList;
    }

    public void setPayTimeList(List<PayTimeListEntity> mPayTimeList) {
        payTimeList = mPayTimeList;
    }

    public static Creator<OrderTimeLine> getCREATOR() {
        return CREATOR;
    }

    protected OrderTimeLine(Parcel in) {
        workDayPercent = in.readString();
        workDay = in.readString();
    }

    public static final Creator<OrderTimeLine> CREATOR = new Creator<OrderTimeLine>() {
        @Override
        public OrderTimeLine createFromParcel(Parcel in) {
            return new OrderTimeLine(in);
        }

        @Override
        public OrderTimeLine[] newArray(int size) {
            return new OrderTimeLine[size];
        }
    };

    public String getWorkDayPercent() {
        return workDayPercent;
    }

    public void setWorkDayPercent(String workDayPercent) {
        this.workDayPercent = workDayPercent;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workDayPercent);
        dest.writeString(workDay);
    }

    @Override
    public String toString() {
        return "OrderTimeLine{" +
                "workDayPercent='" + workDayPercent + '\'' +
                ", workDay='" + workDay + '\'' +
                ", payTimeList=" + payTimeList +
                '}';
    }
}
