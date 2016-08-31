package com.urgoo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.urgoo.client.R;
import com.zw.express.tool.Util;

/**
 * 自定义加载中进度对话框
 * 
 * @author 51offer
 *
 */
public class LoadingDialog extends Dialog {

    private Context mContext;

    /**
     * 提示文字
     */
    private TextView tvTip;

    private LoadingDialog(Context context) {
        super(context, R.style.AlertDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        tvTip = (TextView) findViewById(R.id.tv_tip);
        // 设置对话框居中显示
        getWindow().setGravity(Gravity.CENTER);
        // 设置对话框的宽度为屏幕的宽度的1/3
        int width = Util.getDeviceWidth(mContext) / 3;
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = width;
    }

    /**
     * 设置提示文字 【默认显示为：加载中......】 如果传递Null或者空字符串，则显示默认的文字
     * 
     * @param tipMessage
     */
    public void setTipMessage(String tipMessage) {
        if (tipMessage != null && !tipMessage.equals("")) {
            tvTip.setText(tipMessage);
        }
    }

    /**
     * 显示加载进度框
     * 
     * @param mContext
     * @return
     */
    public static LoadingDialog show(Context mContext) {
        LoadingDialog dialog = new LoadingDialog(mContext);
        dialog.show();
        return dialog;
    }

}
