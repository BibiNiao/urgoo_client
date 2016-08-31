package com.zw.express.tool.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

public class NetWorkToolBase {
	
	protected Context context;
	
	private ProgressDialog progressDialog = null;

	public void pressBackBtn(){
		if(progressDialog!=null&&progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	public void destroy(){
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

	public void showProgressDialog(Context context,boolean isShow){
		if(!isShow){
			return;
		}
		try{
			if(progressDialog != null && progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			progressDialog = ProgressDialog.show(context, null, "数据加载中...", true, true);
			progressDialog.setOnKeyListener(onKeyListener);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean dismissProgressDialog(boolean isDismiss){
		if(!isDismiss){
			return true;
		}
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * add a keylistener for progress dialog
	 */
	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				if(progressDialog!=null&&progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			}
			return false;
		}
	};
}
