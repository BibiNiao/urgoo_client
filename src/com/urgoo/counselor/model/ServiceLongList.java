package com.urgoo.counselor.model;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class ServiceLongList {
    private int serviceLongId;
    private String detailed;

    public int getServiceLongId() {
        return serviceLongId;
    }

    public void setServiceLongId(int serviceLongId) {
        this.serviceLongId = serviceLongId;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "ServiceLongListBean{" +
                "serviceLongId=" + serviceLongId +
                ", detailed='" + detailed + '\'' +
                '}';
    }
}
