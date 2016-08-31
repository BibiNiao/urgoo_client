package com.urgoo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import com.urgoo.client.R;

/**
 * Created by urgoo_01 on 2016/6/27.
 */

public class BaseDialog extends Dialog {
    private Context context;
    public BaseDialog(Context context) {
        super(context, R.style.dialogs);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (d.getWidth() * 0.75);
        getWindow().setAttributes(p);
    }
}
