package com.zw.express.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.urgoo.client.R;

import java.io.File;

public class ImageLoaderUtils {

	/**
	 * 初始化图片加载类配置信息
	 **/
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"shitap/cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPoolSize(4)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// 加载图片的线程数
				.denyCacheImageMultipleSizesInMemory()
				// 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
				.memoryCache(new WeakMemoryCache())
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置加载显示图片队列进程
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
																				// s)超时时间
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 图片加载
	 * 
	 * @param imageUrl
	 * @param imageView
	 */
	public static void displayImage(String imageUrl, ImageView imageView) {
		displayImage(imageUrl, imageView, defaultOptions());
	}

	/**
	 * 图片加载
	 * 
	 * @param imageUrl
	 * @param imageView
	 */
	public static void displayImage(String imageUrl, ImageView imageView,
			DisplayImageOptions displayImageOptions) {
		ImageLoader imageLoader = ImageLoader.getInstance();

		imageLoader.displayImage(imageUrl, imageView, displayImageOptions);

	}

	/**
	 * 默许图片处理
	 * 
	 * @return
	 */
	public static DisplayImageOptions defaultOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_useravatar)
				// 在ImageView加载过程中显示图片
				.showImageForEmptyUri(R.drawable.default_useravatar)
				// image连接地址为空时
				.showImageOnFail(R.drawable.default_useravatar)
				// image加载失败
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true) // 加载图片时会在内存中加载缓存
				.cacheOnDisk(true)// 加载图片时会在硬盘中加载缓存
				.displayer(new RoundedBitmapDisplayer(0)) // default
				.build();
		return options;
	}

	/**
	 * 图片圆角处理
	 * 
	 * @param id
	 * @param rounded
	 * @return
	 */
	public static DisplayImageOptions displayImageOptions(int id, int rounded) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(id)
				// 在ImageView加载过程中显示图片
				.showImageForEmptyUri(id)
				// image连接地址为空时
				.showImageOnFail(id)
				// image加载失败
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true) // 加载图片时会在内存中加载缓存
				.cacheOnDisk(true)// 加载图片时会在硬盘中加载缓存
				.displayer(new RoundedBitmapDisplayer(rounded)) // default
				.build();
		return options;
	}
	
	//缓存清理
	public static  void clearMemoryCache(){
		 ImageLoader imageLoader = ImageLoader.getInstance();
		 imageLoader.clearMemoryCache();  
         imageLoader.clearDiskCache(); 
	}

}
