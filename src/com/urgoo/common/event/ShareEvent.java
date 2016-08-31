package com.urgoo.common.event;

/**
 * 分享内容到第三方平台回调状态
 */
public class ShareEvent {
    public static final int SHARE_TYPE_COMPLETE = 0;//分享成功
    public static final int SHARE_TYPE_ERROR = 1;//分享失败
    public static final int SHARE_TYPE_CANCEL = 2;//取消分享

    public int getStatus() {
        return status;
    }
    private int status;

    public ShareEvent(int status) {
        this.status = status;
    }
}
