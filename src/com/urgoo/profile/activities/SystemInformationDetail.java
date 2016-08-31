package com.urgoo.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.urgoo.account.activity.HomeActivity;
import com.urgoo.base.ActivityBase;
import com.urgoo.common.ZWConfig;
import com.urgoo.client.R;
import com.urgoo.domain.InformationDetailEntity;
import com.urgoo.net.EventCode;
import com.zw.express.tool.GsonTools;
import com.zw.express.tool.UiUtil;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/9.
 */
public class SystemInformationDetail extends ActivityBase {



    private TextView message_title;
    private TextView tv_messagecontent;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.systeminformationdetail);

        initView();
        initData();

    }

    @Override
    public void initView() {

        message_title=(TextView) findViewById(R.id.message_title);
        tv_messagecontent=(TextView)findViewById(R.id.tv_messagecontent);

        $(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemInformationDetail.this.finish();
                /*overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);*/
            }
        });
    }

    @Override
    public void initData() {
        //UiUtil.show(SystemInformationDetail.this, "00");

        Intent it = getIntent();
        String informationId = it.getStringExtra("informationId");
        String unread= it.getStringExtra("unread");
        getNetDataForupdate(informationId,unread);
    }


    private  void getNetDataForupdate( final String informationId,String unread){
        Map<String, String> params = new HashMap<String, String>();

        params.put("token", spManager.getToken());
        params.put("type", "");
        params.put("informationId", informationId);
        params.put("unread",unread);
        params.put("termType","1");

        OkHttpClientManager.postAsyn(ZWConfig.Action_updateInformation,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String respon) {
                        //mTv.setText(u);// 注意这里是UI线程

                        Log.d("mytest20160509","getNetDataForupdate->"+respon);

                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);

                            String code = new JSONObject(j.get("header").toString()).getString("code");
                            String message=new JSONObject(j.get("header").toString()).getString("message");


                            Log.d("mytest20160509","getNetDataForupdate->"+j.toString());
                            if(code.equals("200")){
                                String str=new JSONObject(j.get("body").toString()).getString("informationDetail").toString();

                               InformationDetailEntity entity= GsonTools.getTargetClass(str, InformationDetailEntity.class);
                                //message_title.setText(entity.getTitleCn());
                                tv_messagecontent.setText(entity.getContentCn());


                                //UiUtil.show(SystemInformationDetail.this, "修改成功");


                            } else if (code.equals("404")) {

                                UiUtil.show(SystemInformationDetail.this, message);

                                Intent intent = new Intent(SystemInformationDetail.this,
                                        HomeActivity.class);
                                startActivity(intent);
                                SystemInformationDetail.this.finish();
                                /*SystemInformationDetail.this.overridePendingTransition(R.anim.scale_translate_rotate,
                                        R.anim.my_alpha_action);*/
                            }if(code.equals("400")){

                                UiUtil.show(SystemInformationDetail.this, message);
                            }



                        } catch (JSONException e) {
                            //UiUtil.show(getActivity(), "222");
                            UiUtil.show(SystemInformationDetail.this, "22");

                        }
                    }
                }

                , params);

    }
}
