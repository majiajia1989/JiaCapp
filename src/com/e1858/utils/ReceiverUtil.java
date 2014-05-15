package com.e1858.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ReceiverUtil
{
	public static Bundle getAllMetaData(Context context, BroadcastReceiver receiver)
	{
		try
		{
			ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(new ComponentName(context, receiver.getClass()), PackageManager.GET_META_DATA);
			return activityInfo.metaData;
		}
		catch (Exception ex)
		{
			return new Bundle();
		}
	}

	public static String getMetaData(Context context, BroadcastReceiver receiver, String key)
	{
		return getAllMetaData(context, receiver).getString(key);
	}
}
