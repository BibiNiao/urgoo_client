package com.urgoo.pay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shrb.hrsdk.HRSDK;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.urgoo.adapter.PaySelectAdapter;
import com.urgoo.base.NavToolBarActivity;
import com.urgoo.client.R;
import com.urgoo.client.wxapi.Constants;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.Order;
import com.urgoo.domain.WxEntity;
import com.urgoo.net.EventCode;
import com.urgoo.pay.alipay.AliConstants;
import com.urgoo.pay.alipay.PayResult;
import com.urgoo.pay.biz.PayManager;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.GsonTools;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bb on 2016/6/28.
 */
public class PaySelectActivity extends NavToolBarActivity implements View.OnClickListener {
    public static final String ORDERID = "Orderid";
    private TextView tv_price;
    private String orderId;
    private String statu;
    private String orderInfo;
    private ListView lv;
    private PaySelectAdapter adapter;
    private int payPath = -1;//记录选中哪一条
    private Order mOrder;
    private IWXAPI iwxapi;
    private WxEntity wxEntity;
    private EditText et_price;
    private TextView tv_Paid;
    private TextView tobe_Paid;
    private String price;
    private String order;
    private String random;
    private String outTradeNo;
    private String payReqSign;
    private String body;
    String openID;
    String personUnionID;

    @Override
    protected View createContentView() {
        View view = inflaterViewWithLayoutID(R.layout.activity_select_pay, null);
        setNavTitleText("优顾收银台");
        initViews(view);
        getOrder(orderId);
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected void onNavLeftClick(View v) {
        if (statu != null) {
            startActivity(new Intent(PaySelectActivity.this, BaseWebViewActivity.class).putExtra(BaseWebViewFragment.EXTRA_URL, ZWConfig.ACTION_parentOrder));
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onNavLeftClick(null);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initViews(View view) {
        mOrder = new Order();
        orderId = getIntent().getExtras().getString(ORDERID);
        statu = getIntent().getExtras().getString("statu");
        order = "-" + orderId;
        lv = (ListView) view.findViewById(R.id.lv_pay);
        view.findViewById(R.id.submit_btn).setOnClickListener(this);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                price = s.toString();
                spManager.setPrice(price);
            }
        });

        tv_Paid = (TextView) view.findViewById(R.id.tv_Paid);
        tobe_Paid = (TextView) view.findViewById(R.id.tobe_Paid);
        adapter = new PaySelectAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView ivCheck = null;
                if (payPath != position && payPath >= 0) {
                    ivCheck = (ImageView) parent.getChildAt(payPath).findViewById(R.id.check_btn);
                    ivCheck.setImageResource(R.drawable.ic_unselected);
                }
                payPath = position;
                switch (position) {
                    case 0:
                        setCheck(ivCheck, view);
                        break;
                    case 1:
                        setCheck(ivCheck, view);
                        break;
                    case 2:
                        setCheck(ivCheck, view);
                        break;
                }
            }
        });
    }

    private void setCheck(ImageView ivCheck, View view) {
        ivCheck = (ImageView) view.findViewById(R.id.check_btn);
        ivCheck.setImageResource(R.drawable.ic_selected);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                if (!Util.isEmpty(price)) {
                    if (Double.parseDouble(price) > Double.parseDouble(tobe_Paid.getText().toString())) {
                        showToastSafe("输入的金额超出了需要支付的金额");
                    } else {
                        selectPayPath();
                    }
                } else {
                    showToastSafe("请输入需要支付的金额");
                }
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AliConstants.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PaySelectActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PaySelectActivity.this, PayCompleteActivity.class).putExtra("orderId", orderId)
                                .putExtra("price", price).putExtra("payRequestOrderId", mOrder.getPayRequestOrderId());
                        startActivity(i);
                        finish();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        Toast.makeText(PaySelectActivity.this, "取消支付", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        Toast.makeText(PaySelectActivity.this, "未安装支付宝", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(PaySelectActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                        Toast.makeText(PaySelectActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaySelectActivity.this, PayforfailureActivity.class).putExtra("orderId", orderId)
                                .putExtra("price", price).putExtra("payRequestOrderId", mOrder.getPayRequestOrderId()));
                        finish();
                    }
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    break;
                }
                case AliConstants.SDK_CHECK_FLAG: {
                    Toast.makeText(PaySelectActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 微信支付
     */
    private void wechatPay(String packageValue) {
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        iwxapi.registerApp(Constants.APP_ID);
        if (!iwxapi.isWXAppInstalled()) {
            showToastSafe("没有安装微信");
            return;
        }
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = wxEntity.getPartnerId();
        req.prepayId = wxEntity.getPrepayId();
        req.nonceStr = wxEntity.getNonceStr();
        req.timeStamp = wxEntity.getTimeStamp();
        req.packageValue = packageValue;
        req.sign = wxEntity.getSign();
        req.extData = "app data"; // optional
        iwxapi.sendReq(req);
    }

    /**
     * alipay
     */
    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void aliPay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PaySelectActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo, true);

                Message msg = new Message();
                msg.what = AliConstants.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void getOrder(String orderId) {
        showLoadingDialog();
        PayManager.getInstance(this).getOrder(orderId, this);
    }

    private void getAliPayDetail(String subject, String body, String price, String orderId) {
        showLoadingDialog();
        PayManager.getInstance(this).getAliPayDetail(subject, body, price, orderId, this);
    }

    private void getWechatDetail(String body, String price, String orderId) {
        showLoadingDialog();
        PayManager.getInstance(this).getWechatDetail(body, price, orderId, this);
    }

    private void getHuaRui(String body, String price, String orderId) {
        showLoadingDialog();
        PayManager.getInstance(this).getHuaRuiPayLaunch(body, price, orderId, this);
    }

    /**
     * 根据选择支付的路径进行跳转
     */
    private void selectPayPath() {
        if (payPath >= 0) {
            if (payPath == 0) {
                //支付宝
                getAliPayDetail(mOrder.getServiceName(), mOrder.getServiceName(), price, mOrder.getPayRequestOrderId() + order);
            } else if (payPath == 1) {
                //微信
                getWechatDetail(mOrder.getServiceName(), price, mOrder.getPayRequestOrderId() + order);
            } else if (payPath == 2) {
                //银行卡
                getHuaRui(mOrder.getServiceName(), price, orderId);
            }
        } else {
            showToastSafe("请选择支付方式");
        }
    }

    /**
     * 验证开发者
     */
    private void approveDev(String approveDevSign, String random) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map responseMap = (Map) msg.obj;
                android.util.Log.e("responseMap:", responseMap.toString());
                getUserIDs();
            }
        };
        HRSDK.approveDev(approveDevSign, random, handler);
    }

    /**
     * 获取ID
     */
    private void getUserIDs() {
        personUnionID = "";
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map responseMap = (Map) msg.obj;
                if (responseMap.get("returnCode").equals("000000")) {
                    openID = responseMap.get("openID").toString();
                    if (responseMap.get("personUnionID") != null) {
                        personUnionID = responseMap.get("personUnionID").toString();
                        android.util.Log.e("personUnionID:", personUnionID);
                    }
                    orderPay();
                }
                android.util.Log.e("responseMap:", responseMap.toString());
                android.util.Log.e("openID:", openID);
            }
        };

        HRSDK.Users.getUserIDs(spManager.getToken(), handler);
    }


    /**
     * 绑定卡
     */
    private void orderPay() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map responseMap = (Map) msg.obj;
//                payID = responseMap.get("payID").toString();
                android.util.Log.e("orderPay", responseMap.toString());
                if (responseMap.get("returnCode").equals("000000")) {
                    Intent i = new Intent(PaySelectActivity.this, PayCompleteActivity.class).putExtra("orderId", orderId)
                            .putExtra("price", price).putExtra("payRequestOrderId", mOrder.getPayRequestOrderId());
                    startActivity(i);
                    finish();
                } else {
                    startActivity(new Intent(PaySelectActivity.this, PayforfailureActivity.class).putExtra("orderId", orderId)
                            .putExtra("price", price).putExtra("payRequestOrderId", mOrder.getPayRequestOrderId()));
                    finish();
                }
            }
        };
        Map callbackMap = new HashMap();
        Map autoFillMap = new HashMap();
//        autoFillMap.put("mobile", "18310839846");//弱实名手机号
//        autoFillMap.put("realName", "刘汉明");//真实姓名
//        autoFillMap.put("cardNo", "620904789023452324");//银行卡号
//        autoFillMap.put("identity", "42070419900611503X");//证件号码
//        autoFillMap.put("revmobile", "15692125542");//银行预留手机号
        String mchName = "优顾留学";
        String mchID = "SYT004";
        String detail = "Android LE 1s 手机，￥1099，超薄";
        String confirmOrder = "N";
        String attach = "attach附加数据";
        String paidAmount = mOrder.getPriceed();
        String unpaidAmount;
        if (mOrder.getPriceed().equals("0.0")) {
            unpaidAmount = mOrder.getPriceTotal();
        } else {
            unpaidAmount = mOrder.getBalancePrice();
        }
        String limitPay = "01";
        String feeType = "CNY";
        String goodsTag = "WXG";
        String timeValid = "120";
        String deviceInfo = "34234234";
        HRSDK.Pay.orderPay(openID, personUnionID, mchName, mchID, outTradeNo, body, detail, random, payReqSign, attach, confirmOrder,
                unpaidAmount, paidAmount, unpaidAmount, limitPay, feeType, goodsTag, timeValid, deviceInfo, autoFillMap, callbackMap, PaySelectActivity.this, handler);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        dismissLoadingDialog();
        switch (eventCode) {
            case EventCodeHuaRuiPay:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    body = jsonObject.getString("body");
                    random = jsonObject.getString("randomNumber");
                    payReqSign = jsonObject.getString("payReqSign");
                    outTradeNo = jsonObject.getString("outTradeNo");
                    approveDev(jsonObject.getString("approveDevSign"), random);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeGetPayOrder:
                try {
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                    mOrder = gson.fromJson(result.getString("body").toString(), new TypeToken<Order>() {
                    }.getType());
                    tv_price.setText(mOrder.getPriceTotal());
                    tv_Paid.setText(mOrder.getPriceed());
                    if (mOrder.getPriceed().equals("0.0")) {
                        tobe_Paid.setText(mOrder.getPriceTotal());
                    } else {
                        tobe_Paid.setText(mOrder.getBalancePrice());
                    }

                    spManager.setOrderId(orderId);
                    spManager.setPayorderId(mOrder.getPayRequestOrderId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeAliPayDetail:
                try {
                    orderInfo = new JSONObject(result.get("body").toString()).getString("aliPayRequest");
                    aliPay();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EventCodeWechatPayDetail:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("body").toString());
                    wxEntity = new WxEntity();
                    wxEntity = GsonTools.getTargetClass(result.get("body").toString(), WxEntity.class);
                    Constants.APP_ID = jsonObject.getString("appId");
                    wechatPay(jsonObject.getString("package"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
