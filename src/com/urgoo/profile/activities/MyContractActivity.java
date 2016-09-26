package com.urgoo.profile.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.urgoo.base.BaseActivity;
import com.urgoo.client.R;


public class MyContractActivity extends BaseActivity {
	private LinearLayout back;
	
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycontractactivity_layout);
		initView() ;
	}

	@Override
	public void initView() {
		back = (LinearLayout) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyContractActivity.this.finish(); 
			               
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
	
	
}
