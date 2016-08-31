package com.zw.express.tool.image;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

/**
 * 缓存图片的cache （包含mem，disk）
 * 
 */
public enum ImageCache {

	INSTANCE;

	// 默认内存缓存大小
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB

	// 是否使用内存缓存
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	// 是否使用SD卡缓存
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	// 是否在使用缓存欠清理SD卡
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;
	// sd卡
	private DiskCache mDiskCache;
	// 内存
	private LruCache<String, Bitmap> mMemoryCache;

	public static ImageCache createCache() {
		return INSTANCE;
	}

	private ImageCache() {
	}

	public void setCacheParams(ImageCacheParams cacheParams) {
		init(cacheParams);
	}

	private void init(ImageCacheParams cacheParams) {
		// Set up disk cache
		if (cacheParams.diskCacheEnabled) {
			mDiskCache = DiskCache.openCache();
			if (cacheParams.clearDiskCacheOnStart) {
				mDiskCache.clearCache();
			}
		}

		// Set up memory cache
		if (cacheParams.memoryCacheEnabled) {
			mMemoryCache = new LruCache<String, Bitmap>(
					cacheParams.memCacheSize) {
				/**
				 * Measure item size in bytes rather than units which is more
				 * practical for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return ImageCacheUtils.getBitmapSize(bitmap);
				}

			};
		}

	}

	/**
	 * 将图片添加到缓存
	 */
	public void addBitmapToCache(ImageCacheSrcInfo icsi, Bitmap bitmap) {
		if (icsi == null || bitmap == null) {
			return;
		}

		// Add to memory cache
		if (mMemoryCache != null && mMemoryCache.get(icsi.ToString()) == null) {
			mMemoryCache.put(icsi.ToString(), bitmap);
		}

	}

	/**
	 * 从 内存取得图片
	 * 
	 * @return
	 */
	public Bitmap getBitmapFromMem(ImageCacheSrcInfo icsi) {
		if (mMemoryCache != null) {
			final Bitmap memBitmap = mMemoryCache.get(icsi.ToString());
			if (memBitmap != null) {
				return memBitmap;
			}
		}
		return null;
	}

	/**
	 * 从 sd取得图片
	 * 
	 * @return
	 */
	public Bitmap getBitmapFromDiskCache(ImageCacheSrcInfo icsi) {
		if (mDiskCache != null) {
			final File cacheFile = new File(mDiskCache.createFilePath(icsi.mPath));
			if (mDiskCache.containsKey(icsi.mPath)) {
				return decodeBitmap(cacheFile,icsi);
			}
		}
		return null;
	}

	/**
	 * 从 sd取得图片
	 * 
	 * @return
	 */
	public Bitmap getBitmapFromDiskCache(File file,ImageCacheSrcInfo icsi) {
		if (file != null && file.exists()) {
			return decodeBitmap(file,icsi);
		}
		return null;
	}

	/**
	 * 清理缓存
	 */
	public void clearCaches() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}
	}

	public synchronized Bitmap decodeBitmap(File file,ImageCacheSrcInfo icsi) {
		return decodeBitmap(file.getAbsolutePath(), icsi.mWidth,icsi.mHeight/*mImageCacheParams.reqWidth,
				mImageCacheParams.reqHeight*/);
	}

	private synchronized Bitmap decodeBitmap(String fileName, int width,
			int height) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		if (options.outWidth < 1 || options.outHeight < 1) {
			String fn = fileName;
			File ft = new File(fn);
			if (ft.exists()) {
				ft.delete();
				return null;
			}
		}
		// Calculate inSampleSize
		options.inSampleSize = calculateOriginal(options, width, height);

		// Decode bitmap with inSampleSize set

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(fileName, options);
	}

	private int calculateOriginal(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		int inSampleSize = 1;
		final int height = options.outHeight;
		final int width = options.outWidth;
		
		if(reqWidth == 0&&reqHeight == 0){
			reqWidth = width;
			reqHeight = height;
		}
		else if(reqWidth == -1&&reqHeight == -1){
			if(height>width){
				reqWidth = ImageCacheSrcInfo.MAXSHORTSIDE;
				reqHeight = ImageCacheSrcInfo.MAXLONGSIDE;
			}
			else{
				reqWidth = ImageCacheSrcInfo.MAXLONGSIDE;
				reqHeight = ImageCacheSrcInfo.MAXSHORTSIDE;
			}
		}
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 3;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * A holder class that contains cache parameters.
	 */
	public static class ImageCacheParams {
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		public Integer loadingResId = 0;
	}
}
