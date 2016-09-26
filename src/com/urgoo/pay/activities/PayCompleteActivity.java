package com.urgoo.pay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.Order;
import com.urgoo.net.EventCode;
import com.urgoo.pay.biz.PayManager;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bb on 2016/6/28.
 */
public class PayCompleteActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_order_details;
    private Button btn_complete;
    private TextView tv_ordertotal;
    private TextView tv_paidamount;
    private TextView tv_tobeamount;
    private LinearLayout back;
    private String orderId;
    private String price;
    private String payRequestOrderId;
    private Order mOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);
        orderId = spManager.getOrderId();
        price = spManager.getPrice();
        payRequestOrderId = spManager.getPayorderId();
        getUpdateUser(orderId, price, "1", payRequestOrderId);
        initView();
    }

    public void initView() {
        btn_order_details = (Button) findViewById(R.id.btn_order_details);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        tv_ordertotal = (TextView) findViewById(R.id.tv_ordertotal);
        tv_paidamount = (TextView) findViewById(R.id.tv_paidamount);
        tv_tobeamount = (TextView) findViewById(R.id.tv_tobeamount);
        back = (LinearLayout) findViewById(R.id.back);
        btn_complete.setOnClickListener(this);
        btn_order_details.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    /**
     * 订单详情页
     */
    private void toOrderDetail(){
        String strURL = ZWConfig.ACTION_parentOrderIn + "?orderId=" + orderId;
        startActivity(new Intent(PayCompleteActivity.this, BaseWebViewActivity.class).putExtra(BaseWebViewFragment.EXTRA_URL, strURL));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            toOrderDetail();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete:
                //返回继续支付，吧传进来的orderid再传回去
                startActivity(new Intent(PayCompleteActivity.this, PaySelectActivity.class).putExtra("Orderid", orderId));
                finish();
                break;
            case R.id.btn_order_details:
                //返回到订单的列表页面
                toOrderDetail();
                break;
            case R.id.back:
                //返回到订单的列表页面
                toOrderDetail();
                break;
        }
    }

    private void getUpdateUser(String orderId, String price, String payStatus, String payRequestOrderId) {
        showLoadingDialog();
        PayManager.getInstance(this).getUpdateUser(orderId, price, payStatus, payRequestOrderId, this);
    }

    private void getOrder(String orderId) {
        PayManager.getInstance(this).getOrder(orderId, this);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        switch (eventCode) {
            case EventCodeUpdateUser:
                getOrder(orderId);
                break;
            case EventCodeGetPayOrder:
                dismissLoadingDialog();
                try {
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mOrder = gson.fromJson(result.getString("body").toString(), new TypeToken<Order>() {
                    }.getType());
                    if (mOrder.getRetStatus().equals("2")) {
                        btn_complete.setVisibility(View.GONE);
                    }
                    tv_ordertotal.setText(mOrder.getPriceTotal() + "元");
                    tv_paidamount.setText(mOrder.getPriceed() + "元");
                    tv_tobeamount.setText(mOrder.getBalancePrice() + "元");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
