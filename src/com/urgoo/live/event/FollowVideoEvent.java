package com.urgoo.live.event;

/**
 * Created by bb on 2016/10/13.
 */
public class FollowVideoEvent {
    private int videoId;

    public FollowVideoEvent(int videoId) {
        this.videoId = videoId;
    }

    public int getVideoId() {
        return videoId;
    }
}
