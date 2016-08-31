package com.zw.express.tool.image;

import com.zw.express.tool.Util;


public class ImageCacheSrcInfo {
	public String mPath = null;
	public int mWidth = 0;
	public int mHeight = 0;
	public static final int DEFAULTWIDTH = -1;
	public static final int DEFAULTHEIGHT = -1;
	public static final int MAXLONGSIDE = 1600;
	public static final int MAXSHORTSIDE = 1200;
	
	private final String DOT = new String(".");
	
	public ImageCacheSrcInfo(String path,int w,int h){
		mPath = path;
		mWidth = w;
		mHeight = h;
	}
	
	public ImageCacheSrcInfo(String path){
		mPath = path;
	}

	/*public ImageCacheSrcInfo(String icsi){
		if(icsi!=null){
			String[] temp = icsi.split(DOT);
			if(temp==null||temp.length<3){
				mPath = null;
				mWidth = 0;
				mHeight = 0;
			}
			else{
				mHeight = Integer.parseInt(temp[temp.length - 1]);
				mWidth = Integer.parseInt(temp[temp.length - 2]);
				for(int i = 0;i<(temp.length - 3);i++){
					mPath += temp[i];
					mPath += DOT;
				}
				mPath += temp[temp.length - 3];
			}
		}
		else{
			mPath = null;
			mWidth = 0;
			mHeight = 0;
		}
	}*/
	
	public String ToString(){
		return Util.getMD5(mPath)+DOT+mWidth+DOT+mHeight;
	}

}
