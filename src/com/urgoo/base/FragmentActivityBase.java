package com.urgoo.base;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.urgoo.Interface.ActivityInterface;
import com.urgoo.client.R;
import com.zw.express.tool.image.ImageLoader;

@SuppressLint("NewApi")
public class FragmentActivityBase extends FragmentActivity implements ActivityInterface{
	public static ImageLoader imgLoader;
	protected FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		initTool();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	//*******************华丽的分隔线***************

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		return super.onMenuOpened(featureId, menu);
	}

	//*******************华丽的分隔线***************
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*if(keyCode == KeyEvent.KEYCODE_BACK){
			onFinish();
		}*/
		return false;
	}

	@Override
	public void onBackPressed() {
	  super.onBackPressed();
		onFinish();
	}
	
	public void onFinish(){
		overridePendingTransition(0, R.anim.out_to_right);
	}
	
	//*******************华丽的分隔线***************

	@Override
	public void initView() {
		
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initTool() {
		imgLoader = new ImageLoader(this);
		fragmentManager = getSupportFragmentManager();
	}
	
	@Override
	public void refreshView(){
		
	}
	
	//*******************************
	public void hideSoftInput(View v){
		if(v != null){
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	
	public void showSoftInput(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
}
