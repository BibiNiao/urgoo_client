package com.urgoo.common.event;

/**
 * Created by bb on 2016/9/7.
 */
public class MessageEvent {
    public String getMessage() {
        return message;
    }

    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
