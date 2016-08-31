package com.urgoo.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lijie on 2016/5/30.
 */
public class Filter implements Serializable {
    private ArrayList<ServiceModeList> serviceModeList;
    private ArrayList<LableList> lableList;
    private ArrayList<CountryTypeList> countryTypeList;
    private ArrayList<GenderList> genderList;

    @Override
    public String toString() {
        return "Filter{" +
                "serviceModeList=" + serviceModeList +
                ", lableList=" + lableList +
                ", countryTypeList=" + countryTypeList +
                ", genderList=" + genderList +
                '}';
    }

    public ArrayList<ServiceModeList> getServiceModeList() {
        return serviceModeList;
    }

    public void setServiceModeList(ArrayList<ServiceModeList> serviceModeList) {
        this.serviceModeList = serviceModeList;
    }

    public ArrayList<LableList> getLableList() {
        return lableList;
    }

    public void setLableList(ArrayList<LableList> lableList) {
        this.lableList = lableList;
    }

    public ArrayList<CountryTypeList> getCountryTypeList() {
        return countryTypeList;
    }

    public void setCountryTypeList(ArrayList<CountryTypeList> countryTypeList) {
        this.countryTypeList = countryTypeList;
    }

    public ArrayList<GenderList> getGenderList() {
        return genderList;
    }

    public void setGenderList(ArrayList<GenderList> genderList) {
        this.genderList = genderList;
    }

    public static class ServiceModeList {
        private String serviceModeName;
        private String serviceModeType;

        @Override
        public String toString() {
            return "ServiceModeList{" +
                    "serviceModeName='" + serviceModeName + '\'' +
                    ", serviceModeType='" + serviceModeType + '\'' +
                    '}';
        }

        public String getServiceModeName() {
            return serviceModeName;
        }

        public void setServiceModeName(String serviceModeName) {
            this.serviceModeName = serviceModeName;
        }

        public String getServiceModeType() {
            return serviceModeType;
        }

        public void setServiceModeType(String serviceModeType) {
            this.serviceModeType = serviceModeType;
        }
    }

    public static class LableList {
        private String lableCnName;
        private String lableName;
        private String lableColor;
        private String lableId;

        @Override
        public String toString() {
            return "LableList{" +
                    "lableCnName='" + lableCnName + '\'' +
                    ", lableName='" + lableName + '\'' +
                    ", lableColor='" + lableColor + '\'' +
                    ", lableId='" + lableId + '\'' +
                    '}';
        }

        public String getLableCnName() {
            return lableCnName;
        }

        public void setLableCnName(String lableCnName) {
            this.lableCnName = lableCnName;
        }

        public String getLableName() {
            return lableName;
        }

        public void setLableName(String lableName) {
            this.lableName = lableName;
        }

        public String getLableColor() {
            return lableColor;
        }

        public void setLableColor(String lableColor) {
            this.lableColor = lableColor;
        }

        public String getLableId() {
            return lableId;
        }

        public void setLableId(String lableId) {
            this.lableId = lableId;
        }
    }

    public static class CountryTypeList {
        private String countryName;
        private String countryType;

        @Override
        public String toString() {
            return "CountryTypeList{" +
                    "countryType='" + countryType + '\'' +
                    ", countryName='" + countryName + '\'' +
                    '}';
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryType() {
            return countryType;
        }

        public void setCountryType(String countryType) {
            this.countryType = countryType;
        }


    }

    public static class GenderList {
        private String genderName;
        private String genderType;

        @Override
        public String toString() {
            return "GenderList{" +
                    "genderName='" + genderName + '\'' +
                    ", genderType='" + genderType + '\'' +
                    '}';
        }

        public String getGenderName() {
            return genderName;
        }

        public void setGenderName(String genderName) {
            this.genderName = genderName;
        }

        public String getGenderType() {
            return genderType;
        }

        public void setGenderType(String genderType) {
            this.genderType = genderType;
        }
    }
}