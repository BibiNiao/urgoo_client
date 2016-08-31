package com.urgoo.business;

import com.urgoo.common.ZWConfig;
import com.zw.express.tool.ImageLoaderUtils;
import com.zw.express.tool.image.ImageLoaderUtil;

import android.widget.ImageView;

public class imageLoadBusiness {

	public void imageLoad(String userPhone, final ImageView img) {

		String url = ZWConfig.ITAPURL_BASE + "?type="
				+ ZWConfig.ACTION_getNetFile + "&AppZL=" + ZWConfig.AppZL_value
				+ "&SJHM=" + userPhone;
		ImageLoaderUtils.displayImage(url, img);
	}


	public void imageLoadByURL(String urlStr, final ImageView img) {

		String url = ZWConfig.URGOOURL_BASE+urlStr;
		ImageLoaderUtils.displayImage(url, img);
	}

	/*public void imageLoadByURL(String urlStr, final com.urgoo.view.CircleImageView img) {

		String url = ZWConfig.URGOOURL_BASE+urlStr;

		ImageLoaderUtil.displayImage(url, img);
	}*/

	public void imageLoadByURL(String urlStr, final com.urgoo.view.CircleImageView img) {
		if(urlStr.contains("qingdao")){
			String url = urlStr.replace("\\/","/");

			ImageLoaderUtil.displayImage(url, img);
		}else {
			String url = ZWConfig.URGOOURL_BASE3+urlStr;
			ImageLoaderUtil.displayImage(url, img);
		}

	}

	public static void imageLoadByNewURL(String urlStr, final ImageView img) {

		if(urlStr.contains("qingdao")){
			String url = urlStr.replace("\\/","/");
			ImageLoaderUtil.displayImage(url, img);
		}else {
			String url = ZWConfig.URGOOURL_BASE3+urlStr;
			ImageLoaderUtils.displayImage(url, img);
		}

	}

}
