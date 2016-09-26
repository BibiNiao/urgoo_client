package com.urgoo.order;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;
import com.urgoo.net.EventCode;
import com.urgoo.order.biz.ServerManager;
import com.urgoo.order.event.OrderEvent;
import com.urgoo.order.model.OrderServiceEntity;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class LinkActivity extends BaseActivity {
    private RelativeLayout rrl_subit;
    private LinearLayout ll_breakss;
    private OrderServiceEntity orderServiceEntity;
    private TextView tv_counselor_name;
    private TextView tv_counselor_id;
    private TextView tv_counselor_address;
    private TextView tv_counselor_phone;
    private TextView tv_counselor_emil;
    private TextView xieyi;
    private TextView tv_times;
    private EditText et_name;
    private EditText et_names;
    private EditText et_id;
    private EditText et_ids;
    private EditText et_address;
    private EditText et_phone;
    private EditText et_email;
    private String grade;
    private String hh = "\n";
    public static final char[] UNIT = {'亿', '拾', '佰', '仟', '万', '拾', '佰', '仟'};
    public static final char[] CHAINIESFIGURE2 = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        EventBus.getDefault().register(this);
        orderServiceEntity = getIntent().getParcelableExtra("orderInfo");
        initviews();
        initLisener();
        initData();
    }

    public void initData() {
        tv_counselor_name.setText(orderServiceEntity.getEnName());
        xieyi.setText(getString(R.string.xieyi) + orderServiceEntity.getServicePrice() +
                getString(R.string.xieyi1) + toChineseCharI(orderServiceEntity.getServicePrice()) +
                getString(R.string.xieyi2));

        String s3 = orderServiceEntity.getOrderTime();
        String[] temp = s3.split(" ");
        String[] temp2 = temp[0].split("-");
        tv_times.setText("本海外教育信息咨询服务协议三方协议（以下称“三方协议”）由下列三方在 " + temp2[0] + " 年 " + temp2[1] + " 月 " + temp2[2] + " 日签订。");
    }

    private void initLisener() {
        ll_breakss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rrl_subit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEtData();
            }
        });
    }

    // 获取数值后，发起网络请求
    private void setEtData() {

        if (!Util.isEmpty(et_name.getText().toString()) && !Util.isEmpty(et_names.getText().toString()) &&
                !Util.isEmpty(et_id.getText().toString()) && !Util.isEmpty(et_ids.getText().toString()) &&
                !Util.isEmpty(et_address.getText().toString()) && !Util.isEmpty(et_phone.getText().toString()) &&
                !Util.isEmpty(et_email.getText().toString())) {
            grade =
                    getString(R.string.xiyi1) + hh + tv_times.getText().toString() + hh +
                            // 甲方
                            getString(R.string.xiyi_jia) + et_name.getText().toString() + hh + et_names.getText().toString() + hh +
                            getString(R.string.xiyi_id) + et_id.getText().toString() + hh + et_ids.getText() + hh +
                            getString(R.string.xiyi_add) + et_address.getText().toString() + hh +
                            getString(R.string.xiyi_phone) + et_phone.getText().toString() + hh +
                            getString(R.string.xiyi_emil) + et_email.getText().toString() + hh + hh +
                            // 乙方
                            getString(R.string.xiyi_yi) + tv_counselor_name.getText().toString() + hh +
                            getString(R.string.xiyi_id) + tv_counselor_id.getText().toString() + hh +
                            getString(R.string.xiyi_add) + tv_counselor_address.getText().toString() + hh +
                            getString(R.string.xiyi_phone) + tv_counselor_phone.getText().toString() + hh +
                            getString(R.string.xiyi_emil) + tv_counselor_emil.getText().toString() + hh +
                            // 丙方
                            getString(R.string.xiyi_bing) + getString(R.string.xiyi_name) + hh +
                            getString(R.string.xiyi_add) + getString(R.string.xiyi_adda) + hh +
                            getString(R.string.xiyi_phone) + getString(R.string.xiyi_phonea) + hh +
                            getString(R.string.xiyi_emil) + getString(R.string.xiyi_emila) + hh +
                            // 协议
                            xieyi.getText().toString();
            HashMap<String, String> params = new HashMap<>();
            params.put("token", spManager.getToken());
            params.put("counselorId", orderServiceEntity.getCounselorId());
            params.put("paraentName", et_names.getText().toString());
            params.put("paraentIdcard", et_ids.getText().toString());
            params.put("paraentAddress", et_address.getText().toString());
            params.put("paraentMobile", et_phone.getText().toString());
            params.put("paraentEmail", et_email.getText().toString());
            params.put("studentName", et_name.getText().toString());
            params.put("studentIdcard", et_id.getText().toString());
            params.put("consuleorName", tv_counselor_name.getText().toString());
            params.put("consuleorIdcard", tv_counselor_id.getText().toString());
            params.put("consuleorAddress", tv_counselor_address.getText().toString());
            params.put("consuleorMobile", tv_counselor_phone.getText().toString());
            params.put("consuleorEmail", tv_counselor_emil.getText().toString());
            params.put("price", orderServiceEntity.getServicePrice());
            params.put("protocoloContent", grade);
            showLoadingDialog();
            ServerManager.getInstance(LinkActivity.this).setLinkData(LinkActivity.this, params);
        } else {
            Toast.makeText(LinkActivity.this, "还没有输入信息哦!", Toast.LENGTH_SHORT).show();
            et_name.setFocusable(true);
            et_name.setFocusableInTouchMode(true);

        }
    }

    private void initviews() {
        ll_breakss = (LinearLayout) findViewById(R.id.ll_breakss);
        rrl_subit = (RelativeLayout) findViewById(R.id.rrl_subit);

        tv_counselor_name = (TextView) findViewById(R.id.tv_counselor_name);
        tv_counselor_id = (TextView) findViewById(R.id.tv_counselor_id);
        tv_counselor_address = (TextView) findViewById(R.id.tv_counselor_address);
        tv_counselor_phone = (TextView) findViewById(R.id.tv_counselor_phone);
        tv_counselor_emil = (TextView) findViewById(R.id.tv_counselor_emil);
        xieyi = (TextView) findViewById(R.id.xieyi);
        tv_times = (TextView) findViewById(R.id.tv_times);

        et_name = (EditText) findViewById(R.id.et_name);
        et_names = (EditText) findViewById(R.id.et_names);
        et_id = (EditText) findViewById(R.id.et_id);
        et_ids = (EditText) findViewById(R.id.et_ids);
        et_address = (EditText) findViewById(R.id.et_address);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
    }

    @Override
    protected void onResponseSuccess(EventCode eventCode, JSONObject result) {
        super.onResponseSuccess(eventCode, result);
        dismissLoadingDialog();
        switch (eventCode) {
            case insertOrderProtocol:
                try {
                    JSONObject jsonObject = new JSONObject(result.get("header").toString());
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("200")) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("orderInfo", orderServiceEntity);
                        Util.openActivityWithBundle(LinkActivity.this, OrderActivity.class, bundle);
                    } else if (code.equals("404")) {
                        UiUtil.show(LinkActivity.this, message);
                    }
                    if (code.equals("400")) {
                        UiUtil.show(LinkActivity.this, message);
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
                break;

        }
    }

    //整数部分的转换
    public static String toChineseCharI(String intString) throws NumberFormatException {


        //用来存放转换后的大写数字,因为是StringBuffer类型,所以顺便把没有转换
        //的数字倒序排列一下,省一个变量.
        StringBuffer ChineseCharI = null;
        if (intString != null) {
            ChineseCharI = new StringBuffer(intString);
            //倒序的数字,便于同单位合并
            String rintString = ChineseCharI.reverse().toString();
            //清空一下
            ChineseCharI.delete(0, ChineseCharI.length());
            //单位索引
            int unitIndex = 0;
            //数字长度
            int intStringLen = intString.length();
            //一位由字符转换的数字
            int intStringnumber = 0;
            //补0
            boolean addZero = false;
            boolean flagZero = false;
            for (int i = 0; i < intStringLen; i++) {
                //按单位长度循环索引
                unitIndex = i % UNIT.length;

                //异常检查
                if (!Character.isDigit(rintString.charAt(i))) {
                    throw new NumberFormatException("数字中含有非法字符");
                }
                intStringnumber = Character.digit(rintString.charAt(i), 10);

                //如果当前位是0,开启补0继续循环直到不是0的位
                if (intStringnumber == 0) {
                    addZero = true;
                    if (i != 0 && i % 4 == 0) {
                        if (addZero && ChineseCharI.length() != 0) {
                            ChineseCharI.append(CHAINIESFIGURE2[0]);
                            addZero = false;
                        }
                        flagZero = true;
                        ChineseCharI.append(UNIT[unitIndex]);
                    }
                } else {
                    //若当前位不是第一位且补0开启
                    if (addZero && ChineseCharI.length() != 0 && !flagZero) {
                        ChineseCharI.append(CHAINIESFIGURE2[0]);
                    }
                    flagZero = false;
                    //插入单位
                    //个位数后面不需 要单位
                    if (i > 0) {
                        System.out.println(i);
                        ChineseCharI.append(UNIT[unitIndex]);
                    }

                    //插入大写数字
                    ChineseCharI.append(CHAINIESFIGURE2[intStringnumber]);
                    //补0关闭
                    addZero = false;
                }
            }

            //异常处理
            if (ChineseCharI.length() == 0) {
                return "零";
            }
        }
        return ChineseCharI.reverse().toString() + "圆整";

    }

    public void onEvent(OrderEvent event) {
        finish();
    }
}

