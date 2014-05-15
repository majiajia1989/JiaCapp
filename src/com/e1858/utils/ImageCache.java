package com.e1858.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.e1858.common.Constant;

public class ImageCache
{
	private static HashMap<String, SoftReference<Drawable>>	cache;
	static
	{
		if (null == cache)
		{
			cache = new HashMap<String, SoftReference<Drawable>>();
		}
	}

	public static HashMap<String, SoftReference<Drawable>> getCache()
	{
		if (null == cache)
		{
			cache = new HashMap<String, SoftReference<Drawable>>();
		}
		return cache;
	}

	public static void putDrawable(String key, Drawable drawable)
	{
		putDrawable(key, drawable, false);
	}

	public static void putDrawable(String key, Drawable drawable, boolean isSaveToDisk)
	{
		if (null == cache)
		{
			cache = new HashMap<String, SoftReference<Drawable>>();
		}
		if (cache.containsKey(key))
		{
			cache.remove(key);
		}
		if (isSaveToDisk)
		{
			FileUtil.saveBmpToSd(((BitmapDrawable) drawable).getBitmap(), key.concat(Constant.PICTURE_EXTENSION));
		}
		cache.put(key, new SoftReference<Drawable>(drawable));
	}

	public static Drawable getDrawable(String key)
	{
		if (null == cache)
		{
			cache = new HashMap<String, SoftReference<Drawable>>();
		}
		Drawable result = null;
		if (containsKey(key))
		{
			SoftReference<Drawable> softReference = ImageCache.getCache().get(key);
			if (null != softReference)
			{
				result = softReference.get();
			}
		}
		if (null == result)
		{
			File cacheFile = new File(FileUtil.getCachDirectory().concat("/").concat(key).concat(Constant.PICTURE_EXTENSION));
			if (cacheFile.exists())
			{
				result = Drawable.createFromPath(cacheFile.getPath());
				ImageCache.putDrawable(key, result);
			}
		}

		return result;
	}

	public static boolean containsKey(String key)
	{
		if (null == cache)
		{
			cache = new HashMap<String, SoftReference<Drawable>>();
		}
		return cache.containsKey(key);
	}

	public static void loadFileFromLocalDisk()
	{
		File cacheFile = new File(FileUtil.getCachDirectory());
		File[] cacheFiles = cacheFile.listFiles(new FilenameFilter()
		{
			public boolean accept(File dir, String filename)
			{
				// TODO Auto-generated method stub
				return (filename.endsWith(Constant.PICTURE_EXTENSION));
			}
		});

		if (null == cacheFiles)
		{
			return;
		}
		for (File file : cacheFiles)
		{
			Drawable cacheDrawable = Drawable.createFromPath(file.getPath());
			ImageCache.putDrawable(FileUtil.getFileNameNoEx(file.getName()), cacheDrawable);
		}
	}
}
