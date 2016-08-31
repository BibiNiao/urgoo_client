package com.urgoo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences 管理
 *
 * @author 51offer
 */
public class SPManager {
    private final static String SP_NAME = "spdata";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String COOKIE = "cookie";
    public final static String USER_ID = "userId";
    public final static String VERSION_CODE = "versionCode";
    public final static String MY_STATUS = "status";
    public static String ZOOM_ID = "";
    public final static String HOST_ID = "hostId";
    public final static String LOGINS = "logins";
    public static final List<String> searchList = new ArrayList<>();
    public final static String TOKEN = "urgootoken";
    public final static String NICKNAME = "nickName";
    public final static String ORDER_ID = "orderId";
    public final static String PRICE = "price";
    public final static String PAYORDER_ID = "payRequestOrderId";


    public boolean setUserName(String username) {
        return getEditor().putString(USERNAME, username).commit();
    }

    public String getUserName() {
        return getSharedPreferences().getString(USERNAME, "");
    }

    public boolean setPassWord(String password) {
        return getEditor().putString(PASSWORD, password).commit();
    }

    public String getPassWord() {
        return getSharedPreferences().getString(PASSWORD, "");
    }

    public boolean setCookie(String cookie) {
        return getEditor().putString(COOKIE, cookie).commit();
    }

    public String getCookie() {
        return getSharedPreferences().getString(COOKIE, "");
    }

    public boolean setToken(String token) {
        return getEditor().putString(TOKEN, token).commit();
    }

    public String getToken() {
        return getSharedPreferences().getString(TOKEN, "");
    }

    public boolean setUserId(String userId) {
        return getEditor().putString(USER_ID, userId).commit();
    }

    public String getUserId() {
        return getSharedPreferences().getString(USER_ID, "");
    }

    public boolean setVersionCode(String versionCode) {
        return getEditor().putString(VERSION_CODE, versionCode).commit();
    }

    public String getVersionCode() {
        return getSharedPreferences().getString(VERSION_CODE, "");
    }

    public boolean setMyStatus(String myStatus) {
        return getEditor().putString(MY_STATUS, myStatus).commit();
    }

    public String getMyStatus() {
        return getSharedPreferences().getString(MY_STATUS, "");
    }

    public boolean setZoomId(String zoomId) {
        return getEditor().putString(ZOOM_ID, zoomId).commit();
    }

    public String getZoomId() {
        return getSharedPreferences().getString(ZOOM_ID, "");
    }

    public boolean setHostId(String hostId) {
        return getEditor().putString(HOST_ID, hostId).commit();
    }

    public String getHostId() {
        return getSharedPreferences().getString(HOST_ID, "");
    }

    public boolean setLogins(String logins) {
        return getEditor().putString(LOGINS, logins).commit();
    }

    public String getLogins() {
        return getSharedPreferences().getString(LOGINS, "");
    }

    public boolean setNickName(String nickname) {
        return getEditor().putString(NICKNAME, nickname).commit();
    }

    public String getNickName() {
        return getSharedPreferences().getString(NICKNAME, "");
    }

    public boolean setOrderId(String orderId) {
        return getEditor().putString(ORDER_ID, orderId).commit();
    }

    public String getOrderId() {
        return getSharedPreferences().getString(ORDER_ID, "");
    }

    public boolean setPrice(String price) {
        return getEditor().putString(PRICE, price).commit();
    }

    public String getPrice() {
        return getSharedPreferences().getString(PRICE, "");
    }

    public boolean setPayorderId(String payOrderId) {
        return getEditor().putString(PAYORDER_ID, payOrderId).commit();
    }

    public String getPayorderId() {
        return getSharedPreferences().getString(PAYORDER_ID, "");
    }

    private static SPManager sInstance;
    private Context mContext;
    private SharedPreferences sp;
    private Editor mEditor;

    private SPManager(Context context) {
        this.mContext = context;
    }

    public static SPManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new SPManager(context);
        }
        return sInstance;
    }

    private SharedPreferences getSharedPreferences() {
        if (null == sp) {
            sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    private Editor getEditor() {
        if (null == mEditor) {
            mEditor = getSharedPreferences().edit();
        }
        return mEditor;
    }

    /**
     * 清除登录信息
     *
     * @return
     */
    public boolean clearLoginInfo() {
        Editor editor = getEditor();
        editor.remove(USERNAME);
        editor.remove(PASSWORD);
        editor.remove(COOKIE);
        editor.remove(USER_ID);
        editor.remove(VERSION_CODE);
        editor.remove(MY_STATUS);
        editor.remove(ZOOM_ID);
        editor.remove(HOST_ID);
        editor.remove(LOGINS);
        editor.remove(TOKEN);
        editor.remove(NICKNAME);
        editor.remove(ORDER_ID);
        editor.remove(PRICE);
        editor.remove(PAYORDER_ID);
        return editor.commit();
    }

}