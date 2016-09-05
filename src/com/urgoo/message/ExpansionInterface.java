package com.urgoo.message;

import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;

/**
 * Created by admin on 2016/4/25.
 */
public interface ExpansionInterface {

    public  void SettingAvatarAndnickname(String mgs, TextView name, ImageView avatar, EMConversation conversation);
    public  void SettingChatnickname(String mgs, TextView name, String conversationId);
    public  void SettingChatAvatar(String mgs, TextView name, ImageView avatar, String conversationId);
//    public  void SettingChatTitle(EaseTitleBar titleBar, String conversationId);

    //杨德成 20160516 点击聊天头像回调接口方法
    public  void SettingChatAvatarClick(String conversationId);
}
