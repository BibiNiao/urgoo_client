package com.urgoo.net;


import okhttp3.Call;

/**
 * 字符串协议回调接口
 * 
 */
public interface StringRequestCallBack {

	/**
	 * 请求成功后的回调方法
	 * 
	 * @param eventCode
	 *            EventCode 事件编码
	 * @param response
	 *            响应返回内容
	 */
	void onSuccess(EventCode eventCode, String response);

	/**
	 * 请求失败后的回调方法
	 * 
	 * @param eventCode
	 *            EventCode 事件编码
	 */
	void onFailure(EventCode eventCode, Call call);
}