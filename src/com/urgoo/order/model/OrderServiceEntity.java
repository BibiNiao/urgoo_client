package com.urgoo.order.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bb on 2016/8/12.
 */
public class OrderServiceEntity implements Parcelable {
    private String serviceName;
    private String enName;
    private String servicePrice;
    private String serviceLife;
    private String grade;
    private String type;
    private String counselorId;
    private String orderCode;
    private String userIcon;
    private String orderTime;
    private String serviceId;

    protected OrderServiceEntity(Parcel in) {
        serviceName = in.readString();
        enName = in.readString();
        servicePrice = in.readString();
        serviceLife = in.readString();
        grade = in.readString();
        type = in.readString();
        counselorId = in.readString();
        orderCode = in.readString();
        serviceId = in.readString();
        userIcon = in.readString();
        orderTime = in.readString();
    }

    public static final Creator<OrderServiceEntity> CREATOR = new Creator<OrderServiceEntity>() {
        @Override
        public OrderServiceEntity createFromParcel(Parcel in) {
            return new OrderServiceEntity(in);
        }

        @Override
        public OrderServiceEntity[] newArray(int size) {
            return new OrderServiceEntity[size];
        }
    };

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String mUserIcon) {
        userIcon = mUserIcon;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String mOrderTime) {
        orderTime = mOrderTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceLife() {
        return serviceLife;
    }

    public void setServiceLife(String serviceLife) {
        this.serviceLife = serviceLife;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceName);
        dest.writeString(enName);
        dest.writeString(servicePrice);
        dest.writeString(serviceLife);
        dest.writeString(grade);
        dest.writeString(type);
        dest.writeString(counselorId);
        dest.writeString(orderCode);
        dest.writeString(serviceId);
        dest.writeString(userIcon);
        dest.writeString(orderTime);
    }

    @Override
    public String toString() {
        return "OrderServiceEntity{" +
                "serviceName='" + serviceName + '\'' +
                ", enName='" + enName + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", serviceLife='" + serviceLife + '\'' +
                ", grade='" + grade + '\'' +
                ", type='" + type + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}
