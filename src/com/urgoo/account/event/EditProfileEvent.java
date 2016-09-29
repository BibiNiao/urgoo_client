package com.urgoo.account.event;


import com.urgoo.account.model.UserBean;

/**
 * 修改用户资料成功后的Event
 * Created by qiang.w.
 */
public class EditProfileEvent {

    private UserBean userBean;

    public EditProfileEvent(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

}
