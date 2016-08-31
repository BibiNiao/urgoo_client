package com.urgoo.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.ActivityBase;
import com.urgoo.business.imageLoadBusiness;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.urgoo.order.biz.ServerManager;
import com.urgoo.order.event.OrderEvent;
import com.urgoo.order.model.OrderServiceEntity;
import com.urgoo.pay.activities.PaySelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class OrderActivity extends ActivityBase implements View.OnClickListener {
    private OrderServiceEntity orderServiceEntity;
    private OrderServiceEntity orderServiceEntitys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderServiceEntity = getIntent().getParcelableExtra("orderInfo");
        showLoadingDialog();
        ServerManager.getInstance(this).getInfo(this, orderServiceEntity.getServiceId(), orderServiceEntity.getCounselorId());
        initview();
    }

    private void initview() {
        findViewById(R.id.ll_breakss).setOnClickListener(this);
        findViewById(R.id.tv_order).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_breakss:
                finish();
                break;
            case R.id.tv_order:
                if (orderServiceEntity != null) {
                    showLoadingDialog();
                    ServerManager.getInstance(OrderActivity.this).insertOrder(OrderActivity.this,
                            orderServiceEntity.getServiceId(),
                            orderServiceEntity.getCounselorId(),
                            orderServiceEntity.getOrderCode());
                }
                break;
        }
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case insertOrder:
                try {
                    JSONObject jo = new JSONObject(result.get("header").toString());
                    JSONObject ja = new JSONObject(result.get("body").toString());
                    String code = jo.getString("code");
                    String orderId = ja.getString("orderId");
                    if (code.equals("200")) {
                        //  下单成功
                        Toast.makeText(OrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderActivity.this, PaySelectActivity.class)
                                .putExtra("Orderid", orderId)
                                .putExtra("statu", "0"));
                        EventBus.getDefault().post(new OrderEvent());
//                        CounselorManager.getInstance(this).getaddFollow(this, orderServiceEntity.getCounselorId());
                        finish();
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
            case parentOrderPay:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    orderServiceEntitys = gson.fromJson(jsonObject.getString("orderInfo"), new TypeToken<OrderServiceEntity>() {
                    }.getType());
                    ((TextView) findViewById(R.id.tv_order_name)).setText(orderServiceEntitys.getEnName());
                    ((TextView) findViewById(R.id.tv_order_server)).setText(orderServiceEntitys.getServiceName());
                    ((TextView) findViewById(R.id.tv_order_port)).setText(orderServiceEntitys.getServicePrice());
                    ((TextView) findViewById(R.id.tv_order_year)).setText(orderServiceEntitys.getServiceLife());
                    ((TextView) findViewById(R.id.tv_order_ID)).setText(orderServiceEntitys.getOrderCode());
                    ((TextView) findViewById(R.id.tv_order_time)).setText(orderServiceEntitys.getOrderTime());
                    new imageLoadBusiness().imageLoadByURL(orderServiceEntitys.getUserIcon(), ((com.urgoo.view.CircleImageView) findViewById(R.id.img_order_icon)));
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;

//            case EventCodeAddFollow:
//                try {
//                    JSONObject jsonObject = new JSONObject(result.get("header").toString());
//                    String code = jsonObject.getString("code");
//                    if (code.equals("200")) {   //  关注成功
//                        Log.d("uuuu", "成功下单，就关注下单的顾问");
//                    }
//                } catch (JSONException mE) {
//                    mE.printStackTrace();
//                }
//                break;
        }
    }
}
