package com.urgoo.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;

import com.urgoo.client.R;
import com.zw.express.tool.Util;

/**
 * Created by bb on 2016/7/11.
 */
public class CounselorBtn extends RelativeLayout {
    private SimpleDraweeView imgView;
    private TextView textView;

    public CounselorBtn(Context context) {
        super(context);
    }

    public CounselorBtn(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.btn_textview, this, true);

        this.imgView = (SimpleDraweeView) findViewById(R.id.image);
        this.textView = (TextView) findViewById(R.id.text);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(String uri) {
        Util.setImage(this.imgView, uri);
//        this.imgView.setImageURI(uri);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }
}
