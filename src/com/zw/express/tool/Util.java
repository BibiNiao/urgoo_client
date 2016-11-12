package com.zw.express.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.business.UpdateService;
import com.urgoo.common.APPManagerTool;
import com.urgoo.common.ScreenManager;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.counselor.activities.CounselorMainActivity;
import com.urgoo.data.SPManager;
import com.urgoo.jpush.JpushUtlis;
import com.urgoo.live.activities.LiveDetailActivity;
import com.urgoo.live.activities.VideoDetailActivity;
import com.urgoo.message.EaseHelper;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.message.activities.UserMessageActivity;
import com.urgoo.profile.activities.MessageActivity;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.urgoo.schedule.activites.PrecontractMyOrder;
import com.urgoo.live.activities.ZhiBodDetailActivity;
import com.zw.express.tool.image.ImagePiece;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

public class Util {

    public final static String DATE_YMD = "yyyy-MM-dd";
    public final static String DATE_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static int dp2px(Context context, float dpValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final float scale = dm.density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String dataFormat(String format, String parse, String datestr) {
        if (datestr == null || "".equals(datestr)) {
            return "";
        }
        String str = "";
        try {
            Date date = dateParse(parse, datestr);
            str = getDatedes(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str != null ? str : "";
    }

    public static String dateFormat(String format, String parse, String datestr) {
        if (datestr == null || "".equals(datestr)) {
            return "";
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            SimpleDateFormat pa = new SimpleDateFormat(parse);
            Date date = pa.parse(datestr);
            datestr = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datestr;
    }

    public static Date dateParse(String parse, String datestr) {
        if (datestr == null || "".equals(datestr)) {
            return null;
        }
        Date date = null;
        try {
            SimpleDateFormat pa = new SimpleDateFormat(parse);
            date = pa.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateFormat(String format, Date date) {
        if (date == null) {
            return "";
        }
        String datestr = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            datestr = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestr;
    }

    public static Map<String, Object> jsonStringToMap(String rsContent, LinkedHashMap<String, Object> map) {
        try {
            if (rsContent != null && !"".equals(rsContent)) {
                JSONObject jsonObject = new JSONObject(rsContent);
                for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof String) {
                        map.put(key, String.valueOf(value));
                    } else if (value instanceof JSONObject) {
                        String jsonstr = String.valueOf(value);
                        map.put(key, jsonStringToMap(jsonstr, new LinkedHashMap<String, Object>()));
                    } else if (value instanceof JSONArray) {
                        JSONArray arry = new JSONArray(String.valueOf(value));
                        List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
                        if (arry != null && arry.length() > 0) {
                            for (int i = 0; i < arry.length(); i++) {
                                JSONObject json = arry.getJSONObject(i);
                                rsList.add(jsonStringToMap(String.valueOf(json), new LinkedHashMap<String, Object>()));
                                map.put(key, rsList);
                            }
                        } else {
                            map.put(key, rsList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取本地app的版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);// 获取包的信息

            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有找到包名的时候会走此异常
            e.printStackTrace();
        }
        return -1;
    }

    public static String getDatedes(long srctime) {
        if (srctime == 0) {
            return null;
        }
        long time = new Date().getTime() - srctime;
        String str = "";
        int day = (int) (time / 1000 / 60 / 60 / 24);
        if (day > 0) {
            str = dateFormat(Util.DATE_YMD, new Date(srctime));
        } else {
            int hour = (int) (time / 1000 / 60 / 60);
            if (hour > 0) {
                str = hour + "小时前";
            } else {
                int minute = (int) (time / 1000 / 60);
                if (minute > 0) {
                    str = minute + "分钟前";
                } else {
                    str = (int) time / 1000 + "秒前";
                }
            }
        }
        return str;
    }

    public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {
        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                ImagePiece piece = new ImagePiece();
                piece.index = j + i * xPiece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                pieces.add(piece);
            }
        }
        return pieces;
    }

    public static String getMD5(String str) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = null;
        try {
            md5StrBuff = new StringBuffer();
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            for (byte b : messageDigest.digest())
                md5StrBuff.append(String.format("%02x", b & 0xff));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    /**
     * 获得设备的宽度
     *
     * @param mContext
     * @return
     */
    public static int getDeviceWidth(Context mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获得设备的密度
     *
     * @param mContext
     * @return
     */
    public static float getDeviceDensity(Context mContext) {
        return mContext.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue px(像素)值
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getDeviceDensity(context) + 0.5f);
    }

    /**
     * 判断是否是空字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || str.equals("null");
    }

    public static int getDeviceHeight(Context mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 打开指定的Activity
     *
     * @param mContext
     * @param destCls  目标Activity类名
     */
    public static void openActivity(Context mContext, Class<?> destCls) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, destCls);
        mContext.startActivity(mIntent);
    }

    /**
     * 打开指定的Activity，并传入指定的Bundle参数
     *
     * @param mContext
     * @param destCls  目标Activity类名
     * @param extras   传入目标Activity的bundle参数
     */
    public static void openActivityWithBundle(Context mContext,
                                              Class<?> destCls, Bundle extras) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, destCls);
        mIntent.putExtras(extras);
        mContext.startActivity(mIntent);
    }

    /**
     * 打开指定的Activity，并传入指定的Bundle参数和flag标识
     *
     * @param mContext
     * @param destCls  目标Activity类名
     * @param extras   传入目标Activity的bundle参数
     * @param flags    传入目标Activity的FLAG
     */
    public static void openActivityWithBundle(Context mContext,
                                              Class<?> destCls, Bundle extras, int flags) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, destCls);
        mIntent.putExtras(extras);
        mIntent.setFlags(flags);
        mContext.startActivity(mIntent);
    }

    /**
     * 打开指定的Activity，并携带请求Code
     *
     * @param mContext
     * @param destCls     目标Activity类名
     * @param requestCode 请求码
     */
    public static void openActivityForResult(Context mContext,
                                             Class<?> destCls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, destCls);
        ((Activity) mContext).startActivityForResult(intent, requestCode);
    }


    /**
     * 打开指定的Activity，并携带Bundle参数和请求Code
     *
     * @param mContext
     * @param destCls     目标Activity类名
     * @param extras      传入目标Activity的bundle参数
     * @param requestCode 请求码
     */
    public static void openActivityForResultWithBundle(Context mContext,
                                                       Class<?> destCls, Bundle extras, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, destCls);
        intent.putExtras(extras);
        ((Activity) mContext).startActivityForResult(intent, requestCode);
    }

    /**
     * 老数据图和新数据图
     *
     * @param view
     * @param url
     */
    public static void setImage(SimpleDraweeView view, String url) {
        if (url.contains("qingdao")) {
            view.setImageURI(Uri.parse(url));
        } else {
            view.setImageURI(Uri.parse(ZWConfig.URGOOURL_BASE3 + url));
        }
    }

    /**
     * 短时间显示 Toast
     *
     * @param mContext 上下文Context实例
     * @param text    显示消息内容的资源ID
     */
    public static void shortToast(Context mContext, String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示升级对话框
     *
     * @param context
     * @param isForce
     * @param versionName
     * @param mDesc
     * @param url
     */
    public static void showUpgradeDialog(final Context context, final int isForce, String versionName, String mDesc, final String url) {
        String titleLeftBtn = isForce == 1 ? "退出" : "取消";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("最新版本:" + "V" + versionName);
        builder.setMessage(mDesc);
        builder.setNegativeButton(titleLeftBtn,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isForce == 1) {
                            ScreenManager.getInstance().popAllActivitys();
                        } else {
                            enterHome(context);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(url)
                                && url.startsWith(
                                "http")) {
                            // Utils.openURL(context,
                            // versionInfo.getDownloadUrl());
                            Intent mIntent = new Intent(context,
                                    UpdateService.class);
                            mIntent.putExtra(UpdateService.EXTRA_DOWNLOAD_URL,
                                    url);
                            context.startService(mIntent);
                            if (isForce == 1) {
                                ScreenManager.getInstance().popAllActivitys();
                            } else {
                                enterHome(context);
                            }
                            dialog.dismiss();
                        }
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    private static void enterHome(Context context) {
        String UserId = SPManager.getInstance(context).getUserId();
        String nickName = SPManager.getInstance(context).getNickName();
        String username = SPManager.getInstance(context).getUserName();

        if (UserId != null && nickName != null & username != null) {
            APPManagerTool.setGrowingIOCS(UserId, nickName, username);
        }
        Intent intent;
        if (SPManager.getInstance(context).getToken() != null) {
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, HomeActivity.class);
        }
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * 根据target推送跳转指定页面
     *
     * @param target
     * @param pushJson
     * @param context
     * @throws JSONException
     */
    public static void forward(int target, String pushJson, final Context context) throws JSONException {
        Intent intent;
        Bundle extras = new Bundle();
        switch (target) {
            case SplashActivity.TARGET_VIDEO_DETAIL:
                if (!Util.isEmpty(pushJson)) {
                    JSONObject jsonObject = new JSONObject(pushJson);
                    extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                    extras.putString(VideoDetailActivity.EXTRA_VIDEO_ID, jsonObject.optString("albumId"));
                    openActivityWithBundle(context, VideoDetailActivity.class, extras);
                }
                break;
            case SplashActivity.TARGET_MESSAGE:
                extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                openActivity(context, UserMessageActivity.class);
                break;
            case SplashActivity.TARGET_COUNSELOR:
                if (!Util.isEmpty(pushJson)) {
                    JSONObject jsonObject = new JSONObject(pushJson);
                    extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                    extras.putString(CounselorMainActivity.EXTRA_COUNSELOR_ID, jsonObject.optString("counselorId"));
                    openActivityWithBundle(context, CounselorMainActivity.class, extras);
                }
                break;
            case SplashActivity.TARGET_APPOINTMENT:
                extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                openActivity(context, PrecontractMyOrder.class);
                break;
            case SplashActivity.TARGET_LIVE_DETAIL:
                if (!Util.isEmpty(pushJson)) {
                    JSONObject jsonObject = new JSONObject(pushJson);
                    extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                    extras.putString(LiveDetailActivity.EXTRA_LIVE_ID, jsonObject.optString("liveId"));
                    openActivityWithBundle(context, LiveDetailActivity.class, extras);
                }
                break;
            case SplashActivity.TARGET_VIDEO:
                if (!Util.isEmpty(pushJson)) {
                    JSONObject jsonObject = new JSONObject(pushJson);
                    extras.putString("icon", jsonObject.optString("pic"));
                    extras.putString("name", jsonObject.optString("nickname"));
                    extras.putString("zoomId", jsonObject.optString("zoomId"));
                    extras.putString("zoomNo", jsonObject.optString("zoomNo"));
                    openActivityWithBundle(context, UrgooVideoActivity.class, extras);
                }
                break;
            default:
                intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    /**
     * 冻结账号、token验证失败、登录信息过期;
     *
     * @param context
     * @param msg
     */
    public static void onFreezeAccount(final Context context, String msg) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage(msg);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EaseHelper.getInstance().logout(false, new EMCallBack() {

                                @Override
                                public void onSuccess() {
                                    JpushUtlis.setAlias(context, "", new TagAliasCallback() {
                                        @Override
                                        public void gotResult(int mI, String mS, Set<String> mSet) {
                                            android.util.Log.d("alias", "设置alias为 :  " + mS);
                                        }
                                    });
                                    // 重新显示登陆页面
                                    SPManager.getInstance(context).clearLoginInfo();
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        }
                    });
            builder.setCancelable(false);
            builder.show();
        }
    }


    /**
     * 弹出软键盘
     *
     * @param mContext
     * @param viewHolder
     */
    public static void popSoftKeyBoard(Context mContext, View viewHolder) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewHolder, 0);
    }


    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int getMixedTextlength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 判断当前网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        return getNetworkType(context) != 0;
    }


    /**
     * 获取当前连接的网络类型：
     * <ul>
     * <li>0：无网络</li>
     * <li>1：WIFI</li>
     * <li>2：CMWAP</li>
     * <li>3：CMNET</li>
     * </ul>
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return 0;
        } else {
            NetworkInfo[] info = connManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = anInfo;
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo)
                                    || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 获取不同尺寸对应的像素值
     *
     * @param context
     * @param resId   dimen资源ID
     * @return
     */
    public static int getDimensionPixel(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }


    /**
     * 关闭软键盘
     *
     * @param mContext
     */
    public static void closeSoftKeyBoard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                .getDecorView().getWindowToken(), 0);
    }


    /**
     * 获取系统状态栏高度
     */
    public static int getStatusBarHeight(Context conterxt) {
        int result = 0;
        int resourceId = conterxt.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = conterxt.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 时间换算
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
