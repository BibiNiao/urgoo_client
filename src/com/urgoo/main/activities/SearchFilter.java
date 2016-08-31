package com.urgoo.main.activities;

import java.io.Serializable;

/**
 * Created by lijie on 2016/5/27.
 */
public class SearchFilter implements Serializable {
    private String countryType = "";
    private String intgender = "";
    private String serviceMode = "";
    private String lableId = "";

    @Override
    public String toString() {
        return "SearchFilter{" +
                "countryType='" + countryType + '\'' +
                ", intgender='" + intgender + '\'' +
                ", serviceMode='" + serviceMode + '\'' +
                ", lableId='" + lableId + '\'' +
                '}';
    }

    public String getCountryType() {
        return countryType;
    }

    public void setCountryType(String countryType) {
        this.countryType = countryType;
    }

    public String getIntgender() {
        return intgender;
    }

    public void setIntgender(String intgender) {
        this.intgender = intgender;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getLableId() {
        return lableId;
    }

    public void setLableId(String lableId) {
        this.lableId = lableId;
    }
}
