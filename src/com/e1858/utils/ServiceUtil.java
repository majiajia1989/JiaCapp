package com.e1858.utils;

import android.app.Service;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;

public class ServiceUtil
{
	public static ServiceInfo getServiceInfo(Service service)
	{
		try
		{
			return service.getPackageManager().getServiceInfo(new ComponentName(service, service.getClass()), PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return null;
		}
	}

	public static Bundle getAllMetaData(Service service)
	{
		try
		{
			ServiceInfo serviceInfo = service.getPackageManager().getServiceInfo(new ComponentName(service, service.getClass()), PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
			return serviceInfo.metaData;
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return new Bundle();
		}
	}

	public static String getMetaData(Service service, String key)
	{
		return getAllMetaData(service).getString(key);
	}
}
