package com.zw.express.tool.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.zw.express.tool.image.ImageCache.ImageCacheParams;
import com.zw.express.tool.log.ZWLog;

public class ImageWorker extends Thread{

	private final static ImageWorker INSTANCE = new ImageWorker();

	private ImageCache mImageCache;

	private static Context mContext;

	private volatile boolean onScreen = true;

	private Bitmap mLoadingBitmap;

	private ExecutorService searchThreadPool;
	private HashMap<String, ImageCacheParams> params;
	private Handler mHandler;

	private OnHandleCacheListener mIHandleCache;

	private final String TAG = new String("ImageWorker");

	private final int THREADCOUNT = 20;

	private ImageWorker() {
		mHandler = new Handler();
		mImageCache = ImageCache.createCache();
		searchThreadPool = Executors.newFixedThreadPool(THREADCOUNT);
		mIHandleCache = new OnHandleCacheListener() {

			@Override
			public void onSetImage(final ImageView imageView,
					final Bitmap bitmap) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						imageView.setBackgroundColor(0x00000000);
						imageView.setImageBitmap(bitmap);
					}
				});
			}

			@Override
			public void onError(final ImageView imageView) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						if (imageView != null) {
							//							imageView.setImageResource(R.drawable.error);
							ZWLog.e("ImageWorker", "ImageWorker loadimage onError");
						}
					}
				});
			}
		};

	}

	public static ImageWorker newInstance(Context context) {
		mContext = context;
		return INSTANCE;
	}

	public void loadBitmap(final ImageCacheSrcInfo icsi, final ImageView imageView) {
		ZWLog.d(TAG, "loadBitmap icsi = "+icsi.ToString());
		Bitmap result = mImageCache.getBitmapFromMem(icsi);
		if (result != null && !result.isRecycled()) {
			ZWLog.i("foyo", "find  bitmap from mem");
			mIHandleCache.onSetImage(imageView, result);
		} else if (cancelWork(imageView, icsi)) {
			ZWLog.d(TAG, "loadBitmap else icsi = "+icsi.ToString());
			final SearchTask task = new SearchTask(icsi, imageView,
					mIHandleCache);
			Bitmap bmp = imageView.getDrawingCache();
			Drawable d = imageView.getDrawable();
			if (d != null) {
				d.setCallback(null);
				d = null;
			}
			//			final AsyncDrawable asyncDrawable = new AsyncDrawable(
			//					mContext.getResources(), mLoadingBitmap, task);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					mContext.getResources(), bmp, task);
			imageView.setImageDrawable(asyncDrawable);
			if (!searchThreadPool.isTerminated()
					&& !searchThreadPool.isShutdown()) {
				searchThreadPool.execute(task);
			}
		}

	}

	public Bitmap loadImage(final ImageCacheSrcInfo icsi, final ImageView imageView, final OnImageLoaderListener listener){
		Bitmap result = mImageCache.getBitmapFromMem(icsi);
		if(result != null){
			listener.onImageLoader(result, icsi);
		} else if (cancelWork(imageView, icsi)) {
			try {
				final Handler handler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						listener.onImageLoader((Bitmap)msg.obj, icsi);
					}
				};
				File file = null;
				try{
					file = downloadBitmap(icsi.mPath);
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(file != null){
					Bitmap bitmap = mImageCache.getBitmapFromDiskCache(file,icsi);
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					if (bitmap != null && mImageCache != null && onScreen) {
						mImageCache.addBitmapToCache(icsi, bitmap);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Bitmap loadImage2(final ImageCacheSrcInfo icsi, final ImageView imageView, final OnImageLoaderListener listener){
		Bitmap result = mImageCache.getBitmapFromMem(icsi);
		if(result != null){
			listener.onImageLoader(result, icsi);
		} else if (cancelWork(imageView, icsi)) {
			try {
				//Looper.prepare();
				final Handler handler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						listener.onImageLoader((Bitmap)msg.obj, icsi);
					}
				};
				File file = null;
				try{
					file = downloadBitmap(icsi.mPath);
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(file != null){
					Bitmap bitmap = mImageCache.getBitmapFromDiskCache(file,icsi);
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					if (bitmap != null && mImageCache != null && onScreen) {
						mImageCache.addBitmapToCache(icsi, bitmap);
					}
				}
				//Looper.loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	protected boolean cancelWork(final ImageView view, final ImageCacheSrcInfo icsi) {
		SearchTask task = getSearchTask(view);
		if (task != null) {
			final String taskIcsi = task.getIcsi();
			if (TextUtils.isEmpty(taskIcsi) || !taskIcsi.equals(icsi.ToString())) {
				ZWLog.i("foyo", "cancelWork");
				task.cancelWork();
			} else {
				ZWLog.i("foyo", "task is exist");
				//				return false;防止一次失败后，再也不载入了 zate


			}
		} else {
			ZWLog.i("foyo", "new  task");
		}
		return true;
	}

	public static void cancelWork(final ImageView imageView) {
		final SearchTask bitmapWorkerTask = getSearchTask(imageView);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancelWork();
		}
	}

	public static SearchTask getSearchTask(final ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getTask();
			}
		}
		return null;
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param resId
	 */
	private void setLoadingImage(final int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				resId);
	}

	public class SearchTask implements Runnable {

		ImageCacheSrcInfo icsi;
		volatile boolean stop = false;
		OnHandleCacheListener mIHandleCache;

		private WeakReference<ImageView> mImageViewReference;

		// 停止掉任务
		public void cancelWork() {
			stop = true;
		}

		public SearchTask(final ImageCacheSrcInfo icsi, final ImageView imageView,
				final OnHandleCacheListener mIHandleCache) {
			this.icsi = icsi;
			mImageViewReference = new WeakReference<ImageView>(imageView);
			this.mIHandleCache = mIHandleCache;
		}

		public String getIcsi() {
			return icsi.ToString();
		}

		@Override
		public void run() {
			Bitmap bitmap = null;
			ZWLog.d(TAG, "SearchTask run 1 icsi = "+icsi.ToString());
			if (mImageCache != null && !stop && getAttachedImageView() != null
					&& onScreen) {
				ZWLog.d(TAG, "SearchTask run 2 icsi = "+icsi.ToString());
				bitmap = mImageCache.getBitmapFromDiskCache(icsi);
			}
			ZWLog.d(TAG, "SearchTask run 3 bitmap = "+bitmap);
			if (bitmap == null && mImageCache != null && !stop
					&& getAttachedImageView() != null && onScreen) {
				ZWLog.d(TAG, "SearchTask run 4 icsi = "+icsi.ToString());
				try {
					File file = downloadBitmap(icsi.mPath);
					bitmap = mImageCache.getBitmapFromDiskCache(file,icsi);
					if (bitmap == null) {
						mIHandleCache.onError(getAttachedImageView());
					}
				} catch (IOException e) {
					mIHandleCache.onError(getAttachedImageView());
					e.printStackTrace();
				}

			}
			ZWLog.d(TAG, "SearchTask run 5 bitmap = "+bitmap);

			if (bitmap != null && mImageCache != null && !stop && onScreen) {
				ImageView imageView = getAttachedImageView();
				mImageCache.addBitmapToCache(icsi, bitmap);
				ZWLog.d(TAG, "SearchTask run 6 imageView = "+imageView);
				if (imageView != null && !stop) {
					mIHandleCache.onSetImage(imageView, bitmap);
				} else {
					bitmap.recycle();
					bitmap = null;

				}
			}
			ZWLog.d(TAG, "SearchTask run 8 end");

		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = mImageViewReference.get();
			final SearchTask bitmapWorkerTask = ImageWorker
					.getSearchTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}

	}

	public void setOnScreen(String tag, boolean onScreen) {
		this.onScreen = onScreen;
		if (!onScreen) {
			mImageCache.clearCaches();
			shutdownThreadPool();
		} else {
			restartThreadPool();
			mImageCache.setCacheParams(getParams(tag));
			setLoadingImage(getParams(tag).loadingResId);
		}
	}

	private void restartThreadPool() {
		synchronized (searchThreadPool) {
			if (searchThreadPool.isTerminated()
					|| searchThreadPool.isShutdown()) {
				searchThreadPool = null;
				searchThreadPool = Executors.newFixedThreadPool(THREADCOUNT);
			}
		}

	}

	private void shutdownThreadPool() {
		searchThreadPool.shutdown();
	}

	/**
	 * Set a new CacheParams.
	 */
	public void addParams(String tag, ImageCacheParams cacheParams) {
		if (params == null) {
			params = new HashMap<String, ImageCache.ImageCacheParams>();
		}
		params.put(tag, cacheParams);
		mImageCache.setCacheParams(cacheParams);
		setLoadingImage(cacheParams.loadingResId);
	}

	/**
	 * Get a CacheParams by flag.
	 */
	public ImageCacheParams getParams(String tag) {
		return params.get(tag);
	}

	public class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<SearchTask> task;

		public AsyncDrawable(Resources res, Bitmap bitmap, SearchTask searchTask) {
			super(res, bitmap);
			task = new WeakReference<SearchTask>(searchTask);
		}

		public SearchTask getTask() {
			return task.get();
		}
	}

	private File downloadBitmap(String urlString) throws IOException {
		ZWLog.d(TAG, "downloadBitmap urlString = "+urlString);
		final DiskCache cache = DiskCache.openCache();

		final File cacheFile = new File(cache.createFilePath(urlString));

		if (cache.containsKey(urlString)) {
			return cacheFile;
		}
		ImageCacheUtils.disableConnectionReuseIfNecessary();
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setReadTimeout(10 * 1000);
			urlConnection.setConnectTimeout(5 * 1000);
			final InputStream in = new BufferedInputStream(urlConnection.getInputStream(), ImageCacheUtils.IO_BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(cacheFile),
					ImageCacheUtils.IO_BUFFER_SIZE);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			cacheFile.setLastModified(System.currentTimeMillis());
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}

		return cacheFile;
	}


	/**
	 * 异步下载图片的回调接口
	 *
	 */
	public interface OnImageLoaderListener{
		void onImageLoader(Bitmap bitmap, ImageCacheSrcInfo icsi);
	}

//**************************************************************************************************

	private ImageCacheSrcInfo icsi;
	private ImageView imageView;
	private OnImageLoaderListener listener;
	public ImageWorker(ImageCacheSrcInfo icsi, ImageView imageView, OnImageLoaderListener listener){
		this.icsi = icsi;
		this.imageView = imageView;
		this.listener = listener;
		mImageCache = ImageCache.createCache();
		searchThreadPool = Executors.newFixedThreadPool(THREADCOUNT);
	}

	@Override
	public void run() {
		Bitmap result = mImageCache.getBitmapFromMem(icsi);
		if(result != null){
			listener.onImageLoader(result, icsi);
		} else if (cancelWork(imageView, icsi)) {
			try {
				//Looper.prepare();
				File file = null;
				try{
					file = downloadBitmap(icsi.mPath);
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(file != null){
					Bitmap bitmap = mImageCache.getBitmapFromDiskCache(file,icsi);
					if (bitmap != null && mImageCache != null && onScreen) {
						mImageCache.addBitmapToCache(icsi, bitmap);
						if(listener != null){
							listener.onImageLoader(bitmap, icsi);
						}
					}
				}
				//Looper.loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
