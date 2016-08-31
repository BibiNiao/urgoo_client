package com.urgoo.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lijie on 2016/4/15.
 */
public class AssiatantBean implements Serializable {
    private String cnName;
    private String enName;
    private String status;
    private ArrayList<String> planSituation;
    private ArrayList<String> taskSituation;

    @Override
    public String toString() {
        return "AssiatantBean{" +
                "cnName='" + cnName + '\'' +
                ", enName='" + enName + '\'' +
                ", status='" + status + '\'' +
                ", planSituation=" + planSituation +
                ", taskSituation=" + taskSituation +
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getPlanSituation() {
        return planSituation;
    }

    public void setPlanSituation(ArrayList<String> planSituation) {
        this.planSituation = planSituation;
    }

    public ArrayList<String> getTaskSituation() {
        return taskSituation;
    }

    public void setTaskSituation(ArrayList<String> taskSituation) {
        this.taskSituation = taskSituation;
    }

}
