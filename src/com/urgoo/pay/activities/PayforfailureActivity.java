package com.urgoo.pay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
import com.urgoo.pay.biz.PayManager;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;

import org.json.JSONObject;

public class PayforfailureActivity extends ActivityBase implements View.OnClickListener {

    private Button btn_complete;
    private Button btn_order_details;
    private LinearLayout back;
    private String orderId;
    private String price;
    private String payRequestOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payforfailure);
        orderId = getIntent().getExtras().getString("orderId");
        price = getIntent().getExtras().getString("price");
        payRequestOrderId = getIntent().getExtras().getString("payRequestOrderId");
        getUpdateUser(orderId, price, "0", payRequestOrderId);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        btn_order_details = (Button) findViewById(R.id.btn_order_details);
        back = (LinearLayout) findViewById(R.id.back);
        btn_complete.setOnClickListener(this);
        btn_order_details.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete:
                //返回继续支付，吧传进来的orderid再传回去
                startActivity(new Intent(PayforfailureActivity.this, PaySelectActivity.class).putExtra("Orderid", orderId));
                finish();
                break;
            case R.id.btn_order_details:
                breaks();
                break;
            case R.id.back:
                breaks();
                break;
        }
    }
    //返回到订单的列表页面
    private void breaks() {
        startActivity(new Intent(PayforfailureActivity.this, BaseWebViewActivity.class).putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentOrder));
        finish();
    }

    private void getUpdateUser(String orderId, String price, String payStatus, String payRequestOrderId) {
        showLoadingDialog();
        PayManager.getInstance(this).getUpdateUser(orderId, price, payStatus, payRequestOrderId, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeUpdateUser:
                break;
        }
    }
}
