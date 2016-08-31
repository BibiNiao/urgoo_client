package com.urgoo.domain;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AdvanceRelationEntity {

    private String daojishi;
    private String notime;

    public String getNotime() {
        return notime;
    }

    public void setNotime(String mNotime) {
        notime = mNotime;
    }

    public String getIsAdvanceRelation() {
        return isAdvanceRelation;
    }

    public void setIsAdvanceRelation(String isAdvanceRelation) {
        this.isAdvanceRelation = isAdvanceRelation;
    }

    public String getDaojishi() {
        return daojishi;
    }

    public void setDaojishi(String daojishi) {
        this.daojishi = daojishi;
    }

    public String getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(String advanceId) {
        this.advanceId = advanceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    private String isAdvanceRelation;
    private String advanceId;
    private String status;
    private String counselorId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private  String type;

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    private  String show;

}
