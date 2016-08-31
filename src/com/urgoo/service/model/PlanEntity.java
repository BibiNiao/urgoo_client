package com.urgoo.service.model;

/**
 * Created by dff on 2016/8/23.
 */
public class PlanEntity {

    private String color;
    private String deadDay;
    private String title;
    private String deadLine;
    private String taskId;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String mType) {
        type = mType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String mTaskId) {
        taskId = mTaskId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String mColor) {
        color = mColor;
    }

    public String getDeadDay() {
        return deadDay;
    }

    public void setDeadDay(String mDeadDay) {
        deadDay = mDeadDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String mDeadLine) {
        deadLine = mDeadLine;
    }
}
