package com.e1858.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SDCardUtil
{
	public static String getSDPath()
	{
		String result = null;
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			result = sdDir.getPath();
		}
		return result;
	}

	public static int getBlockSize()
	{
		int result = -1;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			result = statfs.getBlockSize();
		}
		return result;
	}

	// return KB
	public static float getTotalSize()
	{
		float result = -1f;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			result = (statfs.getBlockCount() * statfs.getBlockSize()) / 1024f;
		}
		return result;
	}

	// return KB
	public static float getFreeSize()
	{
		float result = -1;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			result = (statfs.getAvailableBlocks() * statfs.getBlockSize()) / 1024f;
		}
		return result;
	}
}
