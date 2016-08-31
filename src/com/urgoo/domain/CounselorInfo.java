package com.urgoo.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lijie on 2016/5/27.
 */
public class CounselorInfo implements Serializable {

    private String cnName;
    private String enName;
    private String workYear;
    private String userIcon;
    private String slogan;
    private String counselorId;
    private String countyName;
    private ArrayList<LableList> lableList;
    private ArrayList<String> organization;

    @Override
    public String toString() {
        return "CounselorInfo{" +
                "cnName='" + cnName + '\'' +
                ", enName='" + enName + '\'' +
                ", workYear='" + workYear + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", slogan='" + slogan + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", countyName='" + countyName + '\'' +
                ", lableList=" + lableList +
                ", organization=" + organization +
                '}';
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

    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public ArrayList<LableList> getLableList() {
        return lableList;
    }

    public void setLableList(ArrayList<LableList> lableList) {
        this.lableList = lableList;
    }

    public ArrayList<String> getOrganization() {
        return organization;
    }

    public void setOrganization(ArrayList<String> organization) {
        this.organization = organization;
    }

    public static class  LableList{
        private String lableCnNAme;
        private String  lableColor;
        private String  lableEnName;
        private String  lableId;

        @Override
        public String toString() {
            return "LableList{" +
                    "lableCnNAme='" + lableCnNAme + '\'' +
                    ", lableColor='" + lableColor + '\'' +
                    ", lableEnName='" + lableEnName + '\'' +
                    ", lableId='" + lableId + '\'' +
                    '}';
        }

        public String getLableCnNAme() {
            return lableCnNAme;
        }

        public void setLableCnNAme(String lableCnNAme) {
            this.lableCnNAme = lableCnNAme;
        }

        public String getLableColor() {
            return lableColor;
        }

        public void setLableColor(String lableColor) {
            this.lableColor = lableColor;
        }

        public String getLableEnName() {
            return lableEnName;
        }

        public void setLableEnName(String lableEnName) {
            this.lableEnName = lableEnName;
        }

        public String getLableId() {
            return lableId;
        }

        public void setLableId(String lableId) {
            this.lableId = lableId;
        }
    }
}
