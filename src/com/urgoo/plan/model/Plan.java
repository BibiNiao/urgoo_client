package com.urgoo.plan.model;

/**
 * Created by bb on 2016/10/21.
 */
public class Plan {
    /**
     * 计划中
     */
    private String doing;
    private String des;
    private String color;
    /**
     * 剩余天数
     */
    private String deadDay;
    /**
     * 描述
     */
    private String title;
    private String type;
    /**
     * 已完成
     */
    private String done;
    /**
     * 日期
     */
    private String deadLine;
    /**
     * 详情ID
     */
    private String taskId;

    public String getDoing() {
        return doing;
    }

    public void setDoing(String doing) {
        this.doing = doing;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDeadDay() {
        return deadDay;
    }

    public void setDeadDay(String deadDay) {
        this.deadDay = deadDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
