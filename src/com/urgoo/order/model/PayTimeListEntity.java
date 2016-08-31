package com.urgoo.order.model;

/**
 * Created by bb on 2016/8/12.
 */
public class PayTimeListEntity {
    private String descb;
    private String payTime;

    public String getDescb() {
        return descb;
    }

    public void setDescb(String descb) {
        this.descb = descb;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return "PayTimeListEntity{" +
                "descb='" + descb + '\'' +
                ", payTime='" + payTime + '\'' +
                '}';
    }
}
