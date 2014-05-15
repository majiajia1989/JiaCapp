package com.e1858.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtil
{

	public static void hideTitle(Activity activity)
	{
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public static void fullScreen(Activity activity)
	{
		hideTitle(activity);
		Window window = activity.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static Bundle getAllMetaData(Activity activity)
	{
		try
		{
			ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
			return activityInfo.metaData;
		}
		catch (Exception ex)
		{
			return new Bundle();
		}
	}

	public static String getMetaData(Activity activity, String key)
	{
		return getAllMetaData(activity).getString(key);
	}
}
