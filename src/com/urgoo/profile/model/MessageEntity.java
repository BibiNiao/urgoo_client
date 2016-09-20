package com.urgoo.profile.model;

import java.util.List;

/**
 * Created by bb on 2016/9/8.
 */
public class MessageEntity {
    private int systemCount;
    private int personCount;
    private List<InformationEntity> information;

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public int getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(int systemCount) {
        this.systemCount = systemCount;
    }

    public List<InformationEntity> getInformation() {
        return information;
    }

    public void setInformation(List<InformationEntity> information) {
        this.information = information;
    }
}
