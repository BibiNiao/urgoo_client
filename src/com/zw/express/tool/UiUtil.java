package com.zw.express.tool;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.client.R;

import java.util.Timer;
import java.util.TimerTask;

public class UiUtil {

    private static Handler mHandler;

    public static void show(Activity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showInfoDialog(final Activity context, String msg, int ms) {
        showInfoDialog(context, null, msg, null, null, ms, 0, 0, null);
    }

    public static void showInfoDialog(final Activity context, String title, String msg, String ok, String cancel, int ms, final int succ, final int fail, final Handler mhandler) {
        final Dialog dialog = new Dialog(context, R.style.infodialog);
        dialog.setContentView(R.layout.dialog_layout);
        TextView titleText = (TextView) dialog.findViewById(R.id.dialog_title_text);
        View lineV1 = dialog.findViewById(R.id.dialog_lineV1);
        View lineV2 = dialog.findViewById(R.id.dialog_lineV2);
        View lineV3 = dialog.findViewById(R.id.dialog_lineV3);
        TextView msgText = (TextView) dialog.findViewById(R.id.dialog_msg_text);
        TextView okText = (TextView) dialog.findViewById(R.id.dialog_ok_text);
        TextView cancelText = (TextView) dialog.findViewById(R.id.dialog_cancel_text);
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
            }
        };
        mHandler.postDelayed(mRunnable, 3000);
        msgText.setText(msg);
        if (title != null) {
            titleText.setText(title);
            titleText.setVisibility(View.VISIBLE);
            lineV1.setVisibility(View.VISIBLE);
        }
        if (ok != null) {
            okText.setText(ok);
            cancelText.setText(cancel);
            lineV2.setVisibility(View.VISIBLE);
            lineV3.setVisibility(View.VISIBLE);
            okText.setVisibility(View.VISIBLE);
            okText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null && mhandler != null) {
                        mhandler.sendEmptyMessage(succ);
                    }
                    dialog.dismiss();
                }
            });
            cancelText.setVisibility(View.VISIBLE);
            cancelText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null && mhandler != null) {
                        mhandler.sendEmptyMessage(fail);
                    }
                    dialog.dismiss();
                }
            });
        } else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (context != null && !context.isFinishing()) {
                        dialog.dismiss();
                    }
                }
            }, ms);
        }

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //此处可以设置dialog显示的位置
        dialog.show();
    }

    private static Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };
}
