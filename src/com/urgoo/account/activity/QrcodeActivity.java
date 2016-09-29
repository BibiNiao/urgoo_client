package com.urgoo.account.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgoo.base.NavToolBarActivity;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.common.ShareUtil;
import com.urgoo.net.EventCode;
import com.urgoo.order.biz.ServerManager;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class QrcodeActivity extends NavToolBarActivity {
    private ImageView imgCode;
    private Button btnShare;
    private TextView tvTitle;
    private String pic;
    private String title;
    private String url;
    private String desc;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_qrcode, null);
        initViews(view);
        getQrcode();
        return view;
    }

    private void initViews(View view) {
        setNavTitleText("告诉朋友");
        imgCode = (ImageView) view.findViewById(R.id.img_code);
        btnShare = (Button) view.findViewById(R.id.btn_share);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isEmpty(title) && !Util.isEmpty(desc) && !Util.isEmpty(pic) && !Util.isEmpty(url)) {
                    ShareUtil.share(QrcodeActivity.this, title, desc, pic, url);
                }
            }
        });
    }

    private void getQrcode() {
        showLoadingDialog("二维码生成中...");
        ServerManager.getInstance(this).getQrcode(this);
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
                        if (Util.isEmpty(jsonObject.getString("resultStr"))) {
                            tvTitle.setVisibility(View.GONE);
                        } else {
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.setText(jsonObject.getString("resultStr"));
                        }
                        new imageLoadBusiness().imageLoadByNewURL(qrcodeUrl, imgCode);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;

        }
    }
}
