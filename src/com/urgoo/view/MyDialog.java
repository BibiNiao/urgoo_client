package com.urgoo.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.client.R;
import com.zw.express.tool.Util;

/**
 * Created by lijie on 2016/6/1.
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    private ImageView xx;
    private TextView ct, ygt;
    private Button qd;

    public MyDialog(Context context) {
        this(context, R.style.dialog);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init(context, theme);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.height = Util.dp2px(context, 260);
        params.height = Util.dp2px(context, 272);
        window.setAttributes(params);

    }

    private void init(Context context, int theme) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.cancle, null);
        xx = (ImageView) inflate.findViewById(R.id.cx);
        ct = (TextView) inflate.findViewById(R.id.shijianct);
        ygt = (TextView) inflate.findViewById(R.id.yigoutong);
        qd = (Button) inflate.findViewById(R.id.qr);
        xx.setOnClickListener(this);
        ct.setOnClickListener(this);
        ygt.setOnClickListener(this);
        qd.setOnClickListener(this);
        setContentView(inflate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cx:
                dismiss();
                break;
            case R.id.qr:
                break;
            case R.id.shijianct:
                clickBtn(v);
                break;
            case R.id.yigoutong:
                clickBtn(v);
                break;
        }
    }

    private void clickBtn(View view) {
        if (view instanceof TextView) {
            ((TextView) ygt).setTextColor(getContext().getResources().getColor(R.color.common_botton_bar_blue));
            ((TextView) ygt).setBackgroundResource(R.drawable.checked_btn8);
            ((TextView) ct).setTextColor(getContext().getResources().getColor(R.color.common_botton_bar_blue));
            ((TextView) ct).setBackgroundResource(R.drawable.checked_btn8);
        }
        ((TextView) view).setTextColor(getContext().getResources().getColor(R.color.white));
        ((TextView) view).setBackgroundResource(R.drawable.checked_btn7);
    }

}
