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
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.facebook.drawee.view.SimpleDraweeView;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.business.UpdateService;
import com.urgoo.common.APPManagerTool;
import com.urgoo.common.ScreenManager;
import com.urgoo.common.ZWConfig;
import com.urgoo.counselor.activities.CounselorActivity;
import com.urgoo.data.SPManager;
import com.urgoo.message.activities.MainActivity;
import com.urgoo.message.activities.SplashActivity;
import com.urgoo.profile.activities.MessageActivity;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.urgoo.schedule.activites.PrecontractMyOrder;
import com.urgoo.zhibo.activities.ZhiBodDetailActivity;
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
            case SplashActivity.TARGET_MESSAGE:
                extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                openActivity(context, MessageActivity.class);
                break;
            case SplashActivity.TARGET_COUNSELOR:
                if (!Util.isEmpty(pushJson)) {
                    JSONObject jsonObject = new JSONObject(pushJson);
                    extras.putBoolean(SplashActivity.EXTRA_FROM_PUSH, true);
                    extras.putString("counselorId", jsonObject.optString("counselorId"));
                    openActivityWithBundle(context, CounselorActivity.class, extras);
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
                    extras.putString("liveId", jsonObject.optString("liveId"));
                    openActivityWithBundle(context, ZhiBodDetailActivity.class, extras);
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

}
