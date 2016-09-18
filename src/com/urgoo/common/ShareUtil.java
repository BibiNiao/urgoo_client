package com.urgoo.common;

import android.content.Context;

import com.urgoo.client.R;
import com.urgoo.common.event.ShareEvent;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import de.greenrobot.event.EventBus;

/**
 * Created by bb on 2016/7/25.
 */
public class ShareUtil {
    /**
     * 调用第三方分享传入需要的值
     *
     * @param ctx
     * @param title
     * @param text
     * @param ImageUrl
     * @param url
     */
    public static void share(Context ctx, String title, String text, String ImageUrl, final String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                switch (platform.getName()) {
                    case "SinaWeibo":
                        paramsToShare.setText(paramsToShare.getTitle() + url);
                        break;
                }
            }
        });
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        //oks.setTitle(getString(R.string.share));
        oks.setTitle(title);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(ImageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // 启动分享GUI
        oks.show(ctx);
    }
}
