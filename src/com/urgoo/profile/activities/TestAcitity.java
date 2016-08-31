package com.urgoo.profile.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.urgoo.base.ActivityBase;
import com.urgoo.client.R;
import com.urgoo.common.ZWConfig;
import com.urgoo.net.EventCode;
import com.zw.express.tool.net.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class TestAcitity extends  ActivityBase{

	private LinearLayout back;
	WebView mWebView;
	
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testactactivity_layout);
		initView() ;
		getDataByNoFromOkHttp("");
		
		
		
	}

	@Override
	public void initView() {
		
		 mWebView = (WebView) findViewById(R.id.webView);
		//设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        //mWebView.getSettings().setDefaultTextEncodingName("GBK");//设置字符编码  
        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色 透明
        mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));


        //载入js
        mWebView.loadUrl(ZWConfig.URGOOURL_BASE+"001/001/account/account");
       // mWebView.loadUrl("https://www.baidu.com/");
       
        


		
		back = (LinearLayout) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TestAcitity.this.finish(); 
			               
			}
		});
	}

	@Override
	public void initData() {
		// TODO 自动生成的方法存根
		super.initData();
	}

	@Override
	public void initTool() {
		// TODO 自动生成的方法存根
		super.initTool();
	}
	
	
	private void getDataByNoFromOkHttp(final String NO){ 
		//String url, final ResultCallback callback, 
		Map<String, String> params=new HashMap<String, String>();
		/*params.put(ZWConfig.AppZL_key, ZWConfig.AppZL_value);
		params.put(ZWConfig.ACTION_type, ZWConfig.ACTION_qblist);
		params.put("SJHM", "13636320562");
		params.put("KSWZ", "0");
		params.put("SJTS", "18");
		*/
		
		
		
		
		OkHttpClientManager.postAsyn(ZWConfig.URGOOURL_BASE+"001/001/account/account",
				new OkHttpClientManager.ResultCallback<String>() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String respon) {
						//mTv.setText(u);// 注意这里是UI线程
						
						JSONObject j;
						JSONArray ja =null;
						try {
							j = new JSONObject(respon);
							
							
						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						
						
						
						/* String htmlStr3= u;
						 String htmlStr32= "";*/
						 /*UiUtil.show(TestActivity.this, "u");
						 test2.setText("测试："+u);*/
					}
				},params);
	}
}
