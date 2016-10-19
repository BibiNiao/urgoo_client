package com.urgoo.collect.event;

public class FollowEvent {
    private String counselorId;
    private String isAttention;

    public FollowEvent(String counselorId, String isAttention) {
        this.counselorId = counselorId;
        this.isAttention = isAttention;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public String getIsAttention() {
        return isAttention;
    }
}
