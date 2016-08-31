package com.urgoo.domain;

import java.io.Serializable;

/**
 * Created by lijie on 2016/4/14.
 */
public class Person implements Serializable {
    private String cnName;
    private String nickName;
    private String gender;
    private String genderName;
    private String grade;
    private String cityName;
    private String cityId;
    private String countryName;
    private String countryId;
    private String userIcon;

    @Override
    public String toString() {
        return "Person{" +
                "cnName='" + cnName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", genderName='" + genderName + '\'' +
                ", grade='" + grade + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", countryName='" + countryName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", userIcon='" + userIcon + '\'' +
                '}';
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getNickName() {
        return nickName == null ? "" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderName() {
        return genderName == null ? "" : genderName;
    }

    public void setGenderName(String genderId) {
        this.genderName = genderId;
    }

    public String getGrade() {
        return grade == null ? "" : grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCityName() {
        return cityName == null ? "" : cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId == null ? "" : cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountryName() {
        return countryName == null ? "" : countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId == null ? "" : countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getUserIcon() {
        return userIcon == null ? "" : userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

}
