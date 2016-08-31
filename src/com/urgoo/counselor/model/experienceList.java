package com.urgoo.counselor.model;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class experienceList {
    private int experienceId;
    private String detailed;
    private String endDate;
    private String companyName;
    private String position;
    private String startDate;

    public int getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(int experienceId) {
        this.experienceId = experienceId;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "ExperienceListBean{" +
                "experienceId=" + experienceId +
                ", detailed='" + detailed + '\'' +
                ", endDate='" + endDate + '\'' +
                ", companyName='" + companyName + '\'' +
                ", position='" + position + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
