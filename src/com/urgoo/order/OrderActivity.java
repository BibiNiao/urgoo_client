package com.urgoo.order;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.urgoo.base.BaseActivity;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
import com.urgoo.order.biz.ServerManager;
import com.urgoo.order.model.OrderServiceEntity;
import com.urgoo.pay.activities.PaySelectActivity;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderActivity extends NavToolBarActivity implements View.OnClickListener {
    private OrderServiceEntity orderServiceEntitys;
    private String extraService;
    private String serviceId;
    private String counselorId;
    private SimpleDraweeView iv_avatar;
    private TextView tvAgreement;
    private TextView tvService;
    private TextView tvPayTime;
    private boolean isReedAgr = false;
    private boolean isReedService = false;
    private boolean isReedPayTime = false;
    private ImageView ivCheck;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_order, null);
        setNavTitleText("订单");
        initViews(view);
        extraService = getIntent().getStringExtra("extraService");
        serviceId = getIntent().getStringExtra("serviceId");
        counselorId = getIntent().getStringExtra("counselorId");
        getOrderInfo();
        return view;
    }

    private void getOrderInfo() {
        showLoadingDialog();
        ServerManager.getInstance(this).getInfo(this, serviceId, counselorId);
    }

    /**
     * 设置下划线
     *
     * @param tv
     */
    private void setUnderLine(TextView tv) {
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void initViews(View view) {
        view.findViewById(R.id.tv_order).setOnClickListener(this);
        ivCheck = (ImageView) view.findViewById(R.id.iv_check);
        iv_avatar = (SimpleDraweeView) view.findViewById(R.id.img_order_icon);
        tvAgreement = (TextView) view.findViewById(R.id.tv_agreement);
        tvAgreement.setOnClickListener(this);
        tvService = (TextView) view.findViewById(R.id.tv_service);
        tvService.setOnClickListener(this);
        tvPayTime = (TextView) view.findViewById(R.id.tv_paytime);
        tvPayTime.setOnClickListener(this);
        setUnderLine(tvAgreement);
        setUnderLine(tvService);
        setUnderLine(tvPayTime);
    }

    /**
     * 是否刷新图片
     */
    private boolean isCheck() {
        if (isReedAgr && isReedPayTime && isReedService) {
            ivCheck.setImageResource(R.drawable.ic_check);
            return true;
        } else {
            ivCheck.setImageResource(R.drawable.ic_uncheck);
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tv_paytime:
                isReedPayTime = true;
                isCheck();
                bundle.putParcelable("orderInfo", orderServiceEntitys);
                Util.openActivityWithBundle(this, OrderTimeLineActivity.class, bundle);
                break;
            case R.id.tv_service:
                isReedService = true;
                isCheck();
                bundle.putString("extraService", extraService);
                Util.openActivityWithBundle(this, ServiceActivity.class, bundle);
                break;
            case R.id.tv_agreement:
                isReedAgr = true;
                isCheck();
                String strURL = ZWConfig.URL_requestAgreement + "?token=" + spManager.getToken();
                startActivity(new Intent(this, BaseWebViewActivity.class).putExtra(BaseWebViewActivity.EXTRA_URL, strURL));
                break;
            case R.id.tv_order:
                if (isCheck()) {
                    showLoadingDialog();
                    ServerManager.getInstance(this).insertOrder(this, serviceId, orderServiceEntitys.getCounselorId(), orderServiceEntitys.getOrderCode());
                } else {
                    showToastSafe("请阅读三方协议、具体服务以及支付时间后再进行下单");
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
                    iv_avatar.setImageURI(Uri.parse(orderServiceEntitys.getUserIcon()));
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;
        }
    }
}
