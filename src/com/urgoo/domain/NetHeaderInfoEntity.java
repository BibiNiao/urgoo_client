package com.urgoo.domain;

/**
 * Created by Administrator on 2016/7/15.
 */
public class NetHeaderInfoEntity {


    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;
    private  String message;
    private  String serverTime;

}
