package com.urgoo.message.biz;

import java.util.List;

/**
 * Created by bb on 2016/11/1.
 */
public class Robot {
    /**
     * 文字内容
     */
    private String text;
    /**
     * 1 滑动日期  2 滑动时间  3 竖排  4横排
     */
    private String style;
    /**
     * 是否是自己发送的消息
     */
    private boolean isMyself;
    /**
     * 机器人头像
     */
    private String staffIcon;
    /**
     * 用户头像
     */
    private String userIcon;
    /**
     * 是否显示控件
     */
    private Boolean isShow;
    private List<RobotOption> listOption;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public String getStaffIcon() {
        return staffIcon;
    }

    public void setStaffIcon(String staffIcon) {
        this.staffIcon = staffIcon;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public List<RobotOption> getListOption() {
        return listOption;
    }

    public void setListOption(List<RobotOption> listOption) {
        this.listOption = listOption;
    }
}
