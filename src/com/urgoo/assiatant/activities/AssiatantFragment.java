/**
 *
 */
package com.urgoo.assiatant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.urgoo.base.HomeFragment;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.domain.AssiatantBean;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * @author A18ccms a18ccms_gmail_com
 * @ClassName: ProfileFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年3月22日 下午1:01:33
 */
public class AssiatantFragment extends HomeFragment {


    RelativeLayout RStpwLayout, re_order, re_customerservicetitle, re_lianxkef;
    RelativeLayout re_myinfo;

    private final static int MSGTYPE_QBINTERACT_SUCC = 0;
    private final static int MSGTYPE_QB_FAIL = 1;

    private TextView txt_username;
    private TextView txt_phone;
    private String username;
    private String phone;
    private TextView
            tv_fxid2, iv_xiangce2, iv_xiangce22, iv_xiangce223,
            txt_personalcertificate6, txt_re_texttwo, txt_re_textthree;

    ImageView iv_avatar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.assiatantfragment, null);
        initView();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getStatus();
    }

    //杨德成 20160519 新增试图可见刷新数据

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else{
            getStatus();
        }
    }

    protected void initView() {
        tv_fxid2 = (TextView) view.findViewById(R.id.tv_fxid2);
        iv_xiangce2 = (TextView) view.findViewById(R.id.iv_xiangce2);

        RelativeLayout re_fuwuleix2 = (RelativeLayout) view.findViewById(R.id.re_scheduletitile);
        re_fuwuleix2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "renwu", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getActivity(), BaseWebViewActivity.class);
                it.putExtra("EXTRA_URL", ZWConfig.ACTION_taskJz);
                startActivity(it);
            }

        });


        re_lianxkef = (RelativeLayout) view.findViewById(R.id.re_lianxkef);
        re_lianxkef.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


//                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", ZWConfig.ACTION_CustomerService));
            }

        });

        re_order = (RelativeLayout) view.findViewById(R.id.re_order);
        re_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

               /* Intent it = new Intent(getActivity(), TestAcitity.class);

				startActivity(it);*/


                Intent it = new Intent(getActivity(), BaseWebViewActivity.class);
                it.putExtra("EXTRA_URL", ZWConfig.ACTION_parentPlanningHtml);

                startActivity(it);

				/*Intent it = new Intent(getActivity(), MyContractActivity.class);

				startActivity(it);*/
            }

        });
        txt_personalcertificate6 = (TextView) view.findViewById(R.id.txt_personalcertificate6);
        txt_re_texttwo = (TextView) view.findViewById(R.id.txt_re_texttwo);
        txt_re_textthree = (TextView) view.findViewById(R.id.txt_re_textthree);
        iv_xiangce22 = (TextView) view.findViewById(R.id.iv_xiangce22);
        iv_xiangce223 = (TextView) view.findViewById(R.id.iv_xiangce223);
    }


    private void getStatus() {
        try {

            Map<String, String> params = new HashMap<String, String>();
            OkHttpClientManager.postAsyn(ZWConfig.Action_getTaskDetail, new OkHttpClientManager.ResultCallback<String>() {

                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    Log.d("AssiatantBean__response", response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response).getJSONObject("body");
                        AssiatantBean assiatantBean = (new Gson()).fromJson(jsonObject.toString(),
                                AssiatantBean.class);
                        Log.d("assiatantBean", assiatantBean.toString());
                        //tv_fxid2.setText("你好~" + assiatantBean.getCnName());
                        if (assiatantBean.getTaskSituation().size() > 0) {
                            iv_xiangce2.setText(String.valueOf(assiatantBean.getTaskSituation().get(0)));
                        } else {
                            iv_xiangce2.setText("无任务");
                        }
                        if (assiatantBean.getTaskSituation().size() > 1) {
                            iv_xiangce22.setText(String.valueOf(assiatantBean.getTaskSituation().get(1)));
                        }
                        if (assiatantBean.getTaskSituation().size() > 2) {
                            iv_xiangce223.setText(String.valueOf(assiatantBean.getTaskSituation().get(2)));
                        }
                        if (assiatantBean.getPlanSituation().size() > 0) {
                            txt_personalcertificate6.setText(assiatantBean.getPlanSituation().get(0));
                        } else {
                            txt_personalcertificate6.setText("无留学规划");
                        }
                        if (assiatantBean.getPlanSituation().size() > 1) {
                            txt_re_texttwo.setText(assiatantBean.getPlanSituation().get(1));
                        }
                        if (assiatantBean.getPlanSituation().size() > 2) {
                            txt_re_textthree.setText(assiatantBean.getPlanSituation().get(2));
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, params);
        } catch (Exception e) {
        }
    }


}

