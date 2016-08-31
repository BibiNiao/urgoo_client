/**
 *
 */
package com.urgoo.service.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.urgoo.base.HomeFragment;
import com.urgoo.business.BaseService;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.domain.NetHeaderInfoEntity;
import com.urgoo.profile.activities.UrgooVideoActivity;
import com.urgoo.webviewmanage.BaseWebViewActivity;
import com.urgoo.webviewmanage.BaseWebViewFragment;
import com.zw.express.tool.log.Log;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * @author A18ccms a18ccms_gmail_com
 * @ClassName: ProfileFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年3月22日 下午1:01:33
 */
public class ServiceFragment extends HomeFragment implements View.OnClickListener {


   // LinearLayout ll_task, ll_report, ll_liuxueguihua;
    RelativeLayout  ll_task, ll_report, ll_liuxueguihua;
    private final static int MSGTYPE_QBINTERACT_SUCC = 0;
    private final static int MSGTYPE_QB_FAIL = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.servicefragment_layout, null);
        initView();
        return view;
    }


    protected void initView() {
        ll_task = (RelativeLayout) view.findViewById(R.id.ll_task);
        ll_report = (RelativeLayout) view.findViewById(R.id.ll_report);
        ll_liuxueguihua = (RelativeLayout) view.findViewById(R.id.ll_liuxueguihua);
        ll_task.setOnClickListener(this);
        ll_report.setOnClickListener(this);
        ll_liuxueguihua.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.ll_task:
                url = ZWConfig.ACTION_taskJz;
                break;
            case R.id.ll_report:
                url = ZWConfig.ACTION_reportSemesterJz;
                break;

            case R.id.re_personalcertificate:
                url = ZWConfig.Action_helpJz;
                break;
            case R.id.ll_liuxueguihua:
                url = ZWConfig.ACTION_parentPlanningHtml;
                break;
        }
        if (!TextUtils.isEmpty(url)) {
            Log.d("ServiceFragment", url + "xxxxxxx");
            Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
            intent.putExtra(BaseWebViewFragment.EXTRA_URL, url);
            getActivity().startActivity(intent);
        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //UiUtil.show(getActivity(), "隐藏");
        }else{

            getZOOMInfo();
            //notificationIntent.putExtra(MainActivity.EXTRA_TAB, 1);
           /* Intent it= new Intent(getActivity(), UrgooVideoActivity.class);
            it.putExtra("icon", "");
            it.putExtra("name", "");
            it.putExtra("hxCode", "");
            startActivity(it);*/
            //getData();
            //UiUtil.show(getActivity(), "显示");
            //UiUtil.show(getActivity(),"显示");
        }
    }
    //杨德成20160801 获取ZOOM房间;接受或拒绝顾问端发起的视频邀请 0:用户未操作，1：接受，2：拒绝
    private void getZOOMInfo() {
        Map<String, String> params = new HashMap<String, String>();
         params.put("token", SPManager.getInstance(getActivity()).getToken());
        //params.put("token", "p2OdthB5P+A=");
        params.put("termType", "2");
        OkHttpClientManager.postAsyn(ZWConfig.Action_getZoomRoom,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String respon) {
                        JSONObject j;
                        JSONArray ja = null;
                        try {
                            j = new JSONObject(respon);
                            android.util.Log.d("test123",j.toString());
                            NetHeaderInfoEntity hentity= BaseService.getNetHeadInfo(j);
                            if (hentity.getCode().equals("200")) {

                                String zoomId= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("zoomId");
                                String nickname= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("nickname");
                                String pic= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("pic");
                                String zoomNo= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("zoomNo");
                                String status= new JSONObject(new JSONObject(j.get("body").toString()).getString("zoomInfo")).getString("status");
                                //notificationIntent.putExtra(MainActivity.EXTRA_TAB, 1);
                                if(!status.equals("2")){
                                    Intent it= new Intent(getActivity(), UrgooVideoActivity.class);
                                    it.putExtra("icon", pic);
                                    it.putExtra("name", nickname);
                                    it.putExtra("zoomId", zoomId);
                                    it.putExtra("zoomNo", zoomNo);
                                    //it.putExtra("hxCode", "");
                                    startActivity(it);
                                }

                            } else if (hentity.getCode().equals("404")) {
                                // UiUtil.show(ZhiBodDetailActivity.this, hentity.getMessage());
                            }
                            if (hentity.getCode().equals("400")) {
                                //UiUtil.show(ZhiBodDetailActivity.this,hentity.getMessage());
                            }
                        } catch (JSONException e) {
                            // UiUtil.show(ZhiBodDetailActivity.this, "网络状态不佳，请刷新后重试！");
                        }
                    }
                }
                , params);
    }


}
