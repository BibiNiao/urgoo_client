package com.urgoo.client.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.urgoo.client.R;
import com.urgoo.data.SPManager;
import com.urgoo.pay.activities.PayCompleteActivity;
import com.urgoo.pay.activities.PayforfailureActivity;
import com.zw.express.tool.Util;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("微信  aaa ", "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {//success
                Util.openActivity(WXPayEntryActivity.this, PayCompleteActivity.class);
                finish();
            } else if (resp.errCode == -1) {
                //tvResult.setText("支付失败："+resp.errStr +";code=" + String.valueOf(resp.errCode));
//                Toast.makeText(this, "支付失败：" + resp.errStr + ";code=" + String.valueOf(resp.errCode), Toast.LENGTH_LONG).show();
                Util.openActivity(WXPayEntryActivity.this, PayforfailureActivity.class);
                finish();
            } else if (resp.errCode == -2) {
                Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}