/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zw.express.tool.image;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Environment;

import com.urgoo.common.ZWConfig;
import com.zw.express.tool.Util;

public class DiskCache {
	private static final int DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100MB

	private final File mCacheDir;

	private DiskCache(File cacheDir) {
		mCacheDir = cacheDir;
	}

	public static DiskCache openCache() {
		File cacheDir = getDiskCacheDir(ZWConfig.IMAGEDISKCACHEDIR);
		if(cacheDir == null){
			return null;
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		if (cacheDir.isDirectory() && cacheDir.canWrite()
				&& ImageCacheUtils.getUsableSpace(cacheDir) > DISK_CACHE_SIZE) {
			return new DiskCache(cacheDir);
		} else {
			// clearCache(cacheDir);
		}
		// TODO UsableSpace <DISK_CACHE_SIZE

		return null;

	}

	public static File getDiskCacheDir(String uniqueName) {
		final String cachePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File ifchangeDir = new File(cachePath + File.separator + ZWConfig.ZWTDIR);
		if(ifchangeDir == null){
			return null;
		}
		if (!ifchangeDir.exists()) {
			ifchangeDir.mkdir();
		}
		File orangetigerDir = new File(ifchangeDir.getAbsolutePath() + File.separator + ZWConfig.AM990DIR);
		if(orangetigerDir == null){
			return null;
		}
		if (!orangetigerDir.exists()) {
			orangetigerDir.mkdir();
		}
		return new File(orangetigerDir.getAbsolutePath() + File.separator + uniqueName);

	}

	private static void clearCache(File cacheDir) {
		final File[] files = cacheDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	public static String getFilePath(String url) {
		try {
			return URLEncoder.encode(url.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String createFilePath(File cacheDir, String key) {
		//return cacheDir.getAbsolutePath() + File.separator + getFilePath(key);
		return cacheDir.getAbsolutePath() + File.separator + Util.getMD5(key);
	}

	/**
	 * Create a constant cache file path using the current cache directory and
	 * an image key.
	 * 
	 * @param key
	 * @return
	 */
	public String createFilePath(String key) {
		return createFilePath(mCacheDir, key);
	}

	public String get(String key) {
		final String existingFile = createFilePath(mCacheDir, key);
		if (new File(existingFile).exists()) {
			return existingFile;
		}

		return null;

	}

	/**
	 * Checks if a specific key exist in the cache.
	 * 
	 * @param key
	 *            The unique identifier for the bitmap
	 * @return true if found, false otherwise
	 */
	public boolean containsKey(String key) {
		final String existingFile = createFilePath(mCacheDir, key);
		if (new File(existingFile).exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Removes all disk cache entries from this instance cache dir
	 */
	public void clearCache() {
		DiskCache.clearCache(mCacheDir);
	}

	public void clearCache(String uniqueName) {
		File cacheDir = getDiskCacheDir(uniqueName);
		clearCache(cacheDir);
	}

}