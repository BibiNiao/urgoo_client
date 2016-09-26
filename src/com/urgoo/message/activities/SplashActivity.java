package com.urgoo.message.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgoo.account.activity.HomeActivity;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.APPManagerTool;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.VersionBean;
import com.zw.express.tool.Util;
import com.zw.express.tool.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    private int target;
    private String pushJson;
    public static final String EXTRA_JSON = "extra_json";
    public static final String EXTRA_TARGET = "extra_target";
    public static final String EXTRA_FROM_PUSH = "extra_from_push";
    /**
     * 视频接收拒绝页
     */
    public static final int TARGET_VIDEO = 1;
    /**
     * 直播详情
     */
    public static final int TARGET_LIVE_DETAIL = 2;
    /**
     * 预约列表
     */
    public static final int TARGET_APPOINTMENT = 3;
    /**
     * 消息列表
     */
    public static final int TARGET_MESSAGE = 4;
    /**
     * 顾问详情
     */
    public static final int TARGET_COUNSELOR = 5;

    protected static final int CODE_UPDATE_DIALOG = 0;
    protected static final int CODE_URL_ERROR = 1;
    protected static final int CODE_NET_ERROR = 2;
    protected static final int CODE_JSON_ERROR = 3;
    protected static final int CODE_ENTER_HOME = 4;// 进入主页面
    private static final String TAG = "banben    ";
    // 服务器返回的信息
    private String mVersionName;// 版本名
    private int mVersionCode;// 版本号
    private String mDesc;// 版本描述
    private String mDownloadUrl;// 下载地址
    private int pro;

    private boolean isForce = false;
    private boolean isShow = false;

    private RelativeLayout rootLayout;
    private VersionBean vb = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    //说明有更新的版本
                    if (!SplashActivity.this.isFinishing()) {
                        switch (Integer.valueOf(vb.getIsForce())) {
                            case 0:
                                enterHome();
                                break;
                            case 1:
                                Util.showUpgradeDialog(SplashActivity.this, 1, mVersionName, mDesc, mDownloadUrl);
                                break;
                            case 2:
                                Util.showUpgradeDialog(SplashActivity.this, 2, mVersionName, mDesc, mDownloadUrl);
                                break;
                        }

                    }
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
                            .show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
                            .show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_splash);
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        System.out.println("-----------------------" + getIntent().getIntExtra(EXTRA_TARGET, 0));
        target = getIntent().getIntExtra(EXTRA_TARGET, 0);
        if (!Util.isEmpty(getIntent().getStringExtra(EXTRA_JSON))) {
            pushJson = getIntent().getStringExtra(EXTRA_JSON);
        }
//        checkVerson();
//        versionText.setText(getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    //显示广告页面
//    private void showGulide() {
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }


    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
//                if (!notFirst) {
//                    showGulide();
//                    return;
//                }
                checkVerson();
            }
        }).start();

    }

    /**
     * 获取本地app的版本号
     *
     * @return
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);// 获取包的信息

            int versionCode = packageInfo.versionCode;
            Log.d(TAG, "本地版本 ： " + versionCode);
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有找到包名的时候会走此异常
            e.printStackTrace();
        }

        return -1;
    }

    private String readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        String result = out.toString();
        in.close();
        out.close();
        return result;
    }

    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVerson() {
        final long startTime = System.currentTimeMillis();
        // 启动子线程异步加载数据
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                HttpURLConnection conn = null;
                android.util.Log.d(TAG, "run: ");
                try {
                    // 本机地址用localhost, 但是如果用模拟器加载本机的地址时,可以用ip(10.0.2.2)来替换
                    //  URL url = new URL(ApiUrl.BASE_URL + "vesionUp/vesionUp");
                    //URL url = new URL(ZWConfig.URGOOURL_BASE + "001/001/admin/getVesionUpList");
                    //杨德成 20160516 下载更新接口地址配置
                    URL url = new URL(ZWConfig.Action_getVesionUpList);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");// 设置请求方法
                    conn.setConnectTimeout(5000);// 设置连接超时
                    conn.setReadTimeout(5000);// 设置响应超时, 连接上了,但服务器迟迟不给响应
                    conn.connect();// 连接服务器

                    int responseCode = conn.getResponseCode();// 获取响应码
                    android.util.Log.d(TAG, "run: responseCode " + responseCode);
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = readFromStream(inputStream);
                        // System.out.println("网络返回:" + result);
                        Log.d(TAG, "result   :  " + result);
                        // 解析json
                        JSONObject jo = new JSONObject(result).getJSONObject("body");
                        JSONArray ja = jo.getJSONArray("VesionInfoList");
                        android.util.Log.d(TAG, ja.toString());
//                        Log.d("xxx", jo.toString());
//                        mVersionName = jo.getString("versionName");
//                        mVersionCode = jo.getInt("versionCode");
//                        mDesc = jo.getString("description");
//                        isForce = Boolean.valueOf(jo.getString("isForce"));
//                        isShow = Boolean.valueOf(jo.getString("isShow"));
//                        mDownloadUrl = jo.getString("downloadUrl");
                        // System.out.println("版本描述:" + mDesc);
                        ArrayList<VersionBean> list = (new Gson()).fromJson(ja.toString(),
                                new TypeToken<ArrayList<VersionBean>>() {
                                }.getType());
                        if (list == null || list.size() == 0) {
                            // 没有版本更新，直接跳入主界面
                            msg.what = CODE_ENTER_HOME;
                            return;
                        }

                        for (int i = 0; i < list.size(); i++) {
                            if ("2".equals(list.get(i).getDeviceType()) &&
                                    "2".equals(list.get(i).getRoleType())) {
                                vb = list.get(i);
                            }
                        }
                        if (vb == null) {
                            // 没有版本更新，直接跳入主界面
                            msg.what = CODE_ENTER_HOME;
                            return;
                        }

                        mVersionName = vb.getVesionName();
                        mVersionCode = Integer.valueOf(vb.getVesionCode());
                        mDesc = vb.getVesionDescription();
                        Log.d(TAG, vb.toString());
                        //  isForce = Boolean.valueOf(jo.getString("isForce"));
                        //isShow = Boolean.valueOf(jo.getString("isShow"));
                        mDownloadUrl = vb.getDownloadUrl();

                        if (mVersionCode > getVersionCode()) {// 判断是否有更新
                            // 服务器的VersionCode大于本地的VersionCode
                            // 说明有更新, 弹出升级对话框
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            // 没有版本更新
                            msg.what = CODE_ENTER_HOME;
                        }
                    } else {
                        msg.what = CODE_ENTER_HOME;
                    }

                } catch (MalformedURLException e) {
                    // url错误的异常
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    // 网络错误异常
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    // json解析失败
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;// 访问网络花费的时间
                    if (timeUsed < 2000) {
                        // 强制休眠一段时间,保证闪屏页展示2秒钟
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();// 关闭网络连接
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        super.onNewIntent(intent);
//        target = getIntent().getIntExtra(EXTRA_TARGET, 0);
//        if (!Util.isEmpty(getIntent().getStringExtra(EXTRA_JSON))) {
//            pushJson = getIntent().getStringExtra(EXTRA_JSON);
//        }
//        System.out.println("=============" + target);
//    }

    private void enterHome() {

        String UserId = SPManager.getInstance(this).getUserId();
        String nickName = SPManager.getInstance(this).getNickName();
        String username = SPManager.getInstance(this).getUserName();

        android.util.Log.d("testssss", "UserId:" + UserId + "  nickName:" + nickName + " username:" + username);
        if (UserId != null && nickName != null & username != null) {
            APPManagerTool.setGrowingIOCS(UserId, nickName, username);
        }
        Intent intent;
        if (Util.isEmpty(SPManager.getInstance(this).getToken())) {
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            try {
                Util.forward(target, pushJson, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

}
