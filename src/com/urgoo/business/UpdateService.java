package com.urgoo.business;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;


import com.urgoo.client.R;
import com.urgoo.message.activities.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 应用程序升级服务
 */
public class UpdateService extends Service {

    // ==========================================================================
    // Constants
    // ==========================================================================
    private static final int TIME_OUT = 10 * 1000; // 超时
    private static final int DOWNLOAD_OK = 1;
    private static final int DOWNLOAD_ERROR = 0;
    /**
     * 下载url传值的KEY
     */
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String APK_FILE = Environment.getExternalStorageDirectory() + "/urgoo.apk";

    // ==========================================================================
    // Fields
    // ==========================================================================
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private Bitmap largeIcon;
    private int notification_id = R.layout.notification_upgrade_down;

    // ==========================================================================
    // Constructors
    // ==========================================================================

    // ==========================================================================
    // Getters
    // ==========================================================================

    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            createNotification();
            createThread(intent.getStringExtra(EXTRA_DOWNLOAD_URL));
        }
        return super.onStartCommand(intent, flags, startId);
    }

//    /** 通知栏视图 */
//    RemoteViews contentView;

    /**
     * 创建通知
     */
    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        updateIntent = new Intent(this, MainActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        mBuilder = new NotificationCompat.Builder(this);
        if (largeIcon == null) {
            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        }
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.start_download_variable, getString(R.string.app_name)))
                .setTicker(getString(R.string.start_download_variable, getString(R.string.app_name)))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon);
//                .setColor(getResources().getColor(R.color.color_yellow_701));

//        notification = mBuilder.build();
//        notification.icon = R.drawable.ic_launcher;
//        notification.tickerText = getString(R.string.start_download_variable, getString(R.string.app_name));

        /** 在这里我们用自定的view来显示Notification */
//        contentView = new RemoteViews(getPackageName(), R.layout.notification_upgrade_down);
//        contentView.setTextViewText(R.id.tv_title,
//                getString(R.string.downloading_variable, getString(R.string.app_name)));
//        contentView.setTextViewText(R.id.tv_time, Utils.getCurrentTimeByPattern("a hh:mm"));
//        contentView.setTextViewText(R.id.tv_progress, "0%");
//        contentView.setProgressBar(R.id.pb_progress, 100, 0, false);
//        notification.contentView = contentView;

//        updateIntent = new Intent(this, MainActivity.class);
//        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
//        notification.contentIntent = pendingIntent;

        notificationManager.notify(notification_id, mBuilder.build());

    }

    /***
     * 开线程下载
     */
    @SuppressLint("HandlerLeak")
    public void createThread(final String downloadURL) {
        /** 更新UI */
        final Handler handler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWNLOAD_OK:
                        // 下载完成，点击安装
                        File file = new File(APK_FILE);
                        Uri uri = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            startActivity(intent);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        notificationManager.cancel(notification_id);
                        pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);
                        mBuilder.setContentText(getString(R.string.download_success))
                                .setContentIntent(pendingIntent)
                                .setTicker(getString(R.string.download_success));
                        notificationManager.notify(notification_id, mBuilder.build());

                        stopSelf();
                        break;
                    case DOWNLOAD_ERROR:
                        mBuilder.setTicker(getString(R.string.download_failed)).setContentIntent(pendingIntent);
                        stopSelf();
                        break;

                    default:
                        stopSelf();
                        break;
                }

            }

        };

        final Message message = new Message();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    long downloadSize = downloadFile(downloadURL, getApplicationContext());
                    if (downloadSize > 0) {
                        // 下载成功，通知栏提醒
                        message.what = DOWNLOAD_OK;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = DOWNLOAD_ERROR;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }

    /**
     * 下载文件
     *
     * @param downloadUrl 下载URL
     * @param context
     * @return 下载的大小
     * @throws Exception
     */
    public long downloadFile(String downloadUrl, Context context) throws Exception {
        int down_step = 5;// 提示step
        int updateCount = 0;// 已经上传的文件大小
        int totalSize;// 文件总大小
        long downloadedSize = 0;// 已经下载的大小
        InputStream is;
        OutputStream os;

        URL url = new URL(downloadUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        if (conn.getResponseCode() == 404) {
            throw new Exception("Failed! Download url resource not found!");
        }
        // 获取下载文件的大小
        totalSize = conn.getContentLength();
        is = conn.getInputStream();
        os = new FileOutputStream(new File(APK_FILE), false);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
            downloadedSize += len;
            /** 每次增张5% */
            if (updateCount == 0 || (downloadedSize * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                mBuilder.setProgress(100, updateCount, false).setContentText(getString(R.string.downloading_variable, updateCount + "%"));
//                contentView.setTextViewText(R.id.tv_progress, updateCount + "%");
//                contentView.setProgressBar(R.id.pb_progress, 100, updateCount, false);
//                contentView.setTextViewText(R.id.tv_time, Utils.getCurrentTimeByPattern("a hh:mm"));
                // show View
                notificationManager.notify(notification_id, mBuilder.build());
            }
        }
        conn.disconnect();
        is.close();
        os.flush();
        os.close();
        return downloadedSize;
    }

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================
}