package com.urgoo.domain;

import java.lang.ref.PhantomReference;

/**
 * Created by Administrator on 2016/4/26.
 */
public class UserInfoEntity {

    private String  cnName;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getUserHxCode() {
        return userHxCode;
    }

    public void setUserHxCode(String userHxCode) {
        this.userHxCode = userHxCode;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    private String  nickName;
    private String  enName;
    private String  userHxCode;
    private String  userIcon;

    public String getRoleTyp() {
        return roleTyp;
    }

    public void setRoleTyp(String roleTyp) {
        this.roleTyp = roleTyp;
    }

    private String roleTyp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;



}
