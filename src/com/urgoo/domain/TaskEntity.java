package com.urgoo.domain;

/**
 * Created by dff on 2016/8/9.
 */
public class TaskEntity {

    private String subReply;
    private String subjectTitle;
    private String endTime;
    private int taskId;
    private int taskStatus;

    @Override
    public String toString() {
        return "TaskEntity{" +
                "subReply='" + subReply + '\'' +
                ", subjectTitle='" + subjectTitle + '\'' +
                ", endTime='" + endTime + '\'' +
                ", taskId=" + taskId +
                ", taskStatus=" + taskStatus +
                '}';
    }

    public String getSubReply() {
        return subReply;
    }

    public void setSubReply(String mSubReply) {
        subReply = mSubReply;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String mSubjectTitle) {
        subjectTitle = mSubjectTitle;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String mEndTime) {
        endTime = mEndTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int mTaskId) {
        taskId = mTaskId;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int mTaskStatus) {
        taskStatus = mTaskStatus;
    }
}
