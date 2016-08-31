package com.urgoo.profile.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.urgoo.base.ActivityBase;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.ShareUtil;
import com.urgoo.net.EventCode;
import com.urgoo.order.biz.ServerManager;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class QrcodeActivity extends ActivityBase {

    private LinearLayout ll_breakss;
    private ImageView img_code;
    private Button but_fenxiang;
    private String pic;
    private String title;
    private String url;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        img_code = (ImageView) findViewById(R.id.img_code);
        but_fenxiang = (Button) findViewById(R.id.but_fenxiang);
        ll_breakss = (LinearLayout) findViewById(R.id.ll_break);
        showLoadingDialog("二维码生成中...");
        ServerManager.getInstance(this).getQrcode(this);
        ll_breakss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        but_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isEmpty(title) && !Util.isEmpty(desc) && !Util.isEmpty(pic) && !Util.isEmpty(url)) {
                    ShareUtil.share(QrcodeActivity.this, title, desc, pic, url);
                }
            }
        });
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case createQrcode:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    String code = new JSONObject(result.get("header").toString()).getString("code");
                    if (code.equals("200")) {
                        String qrcodeUrl = jsonObject.getString("qrcodeUrl");
                        pic = jsonObject.getString("pic");
                        title = jsonObject.getString("title");
                        url = jsonObject.getString("url");
                        desc = jsonObject.getString("desc");
                        new imageLoadBusiness().imageLoadByNewURL(qrcodeUrl, img_code);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;

        }
    }
}
