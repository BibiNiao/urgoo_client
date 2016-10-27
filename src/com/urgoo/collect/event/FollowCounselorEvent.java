package com.urgoo.collect.event;

public class FollowCounselorEvent {
    private String counselorId;
    private String isAttention;

    public FollowCounselorEvent(String counselorId, String isAttention) {
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
