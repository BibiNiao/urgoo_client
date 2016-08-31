package com.zw.express.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.urgoo.common.ZWConfig;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 应用路径
	 */
	private final static String FOLDER_APP = "/" + ZWConfig.APPCODE;
	private static String FOLDER_IMG = "/img";

	private static String getPath(int type) {
		String path = "";
		String basepath = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			basepath = mSdRootPath + FOLDER_APP;
		} else {
			basepath = mDataRootPath + FOLDER_APP;
		}

		switch (type) {
		case 0:
			path = basepath;
			break;
		case 1:
			path = basepath + FOLDER_IMG;
			break;
		default:
			break;
		}
		createPath(path);
		return path;
	}

	public static void createPath(String path) {
		if (path != null && path.length() > 0) {
			String[] folders = path.split("/");
			String temppath = "";
			for (String folder : folders) {
				if (folder.length() > 0) {
					temppath += "/" + folder.trim();
					File folderFile = new File(temppath);
					if (!folderFile.exists()) {
						folderFile.mkdir();
					}
				}
			}
		}
	}

	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * 
	 * @param fileName
	 * @param bitmap
	 * @throws IOException
	 */
	public static String savaBitmap(String fileName, Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		FileOutputStream fos = null;
		try {
			String path = getPath(1) + "/" + fileName;
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			return path;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 从手机或者sd卡获取Bitmap
	 * 
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(String fileName) {
		return BitmapFactory.decodeFile(getPath(1) + "/" + fileName);
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName) {
		return new File(getPath(1) + "/" + fileName).exists();
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getPath(1) + "/" + fileName).length();
	}

	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getPath(0));
		if (!dirFile.exists()) {
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}

		dirFile.delete();
	}

	public static String readRawFile(Context context, int rawId) {
		String content = null;
		Resources resources = context.getResources();
		InputStream is = null;
		try {
			is = resources.openRawResource(rawId);
			byte buffer[] = new byte[is.available()];
			is.read(buffer);
			content = new String(buffer);
		} catch (IOException e) {
			Log.i(FileUtils.class.getName(), e.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.i(FileUtils.class.getName(), e.toString());
				}
			}
		}
		return content;
	}

	public static void createPath() {
		/* 下载保存路径 */
		String mSavePath;
		// 获得存储卡的路径
		String sdpath = Environment.getExternalStorageDirectory() + "/";
		mSavePath = sdpath + "sdcard/itapimages22/";
		File file = new File(mSavePath);
		// 判断文件目录是否存在
		if (!file.exists()) {
			file.mkdir();
		}

	}

	public static void deleteDir(String dir) {
		try {
			File dirFile = new File(dir);
			if (!dirFile.isDirectory()) {
				dirFile.delete();
				return;
			}
			File inFiles[] = dirFile.listFiles();
			for (File in : inFiles) {
				if (in.isDirectory()) {
					deleteDir(in.getAbsolutePath());
				} else {
					in.delete();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}
	}
}
