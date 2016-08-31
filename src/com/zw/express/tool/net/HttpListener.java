package com.zw.express.tool.net;

public interface HttpListener {
	
	/**
	 * Http get response succeed with string return
	 *
	 * @param response
	 * @param statusCode
	 */
	public void onGetResponseSucceed(String response, int statusCode);
	
	/**
     * HTTP????????????
     */
    public void onGetResponseFailed(Exception e, int statusCode);

    /**
     * ??????reader??????
     *
     * @param e
     */
    public void onCloseReaderFailed(Exception e, int statusCode);

}
