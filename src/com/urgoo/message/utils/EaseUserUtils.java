package com.urgoo.message.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.message.ExpansionInterface;

public class EaseUserUtils {

    //杨德成 20160426
    protected static ExpansionInterface anInterface;
    public static void  setCallBack( ExpansionInterface _anInterface){

        anInterface=_anInterface;

    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	/*EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //正常的string路径
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            //Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
            //杨德成 20160426 修改加载头像
            anInterface.SettingChatAvatar("",null,imageView,username);

        }*/

        anInterface.SettingChatAvatar("",null,imageView,username);
    }
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
//        	EaseUser user = getUserInfo(username);
//        	if(user != null && user.getNick() != null){
//        		textView.setText(user.getNick());
//        	}else{
//        		textView.setText(username);
//                anInterface.SettingChatnickname("",textView,username);
//            }
        }
    }
    
}
