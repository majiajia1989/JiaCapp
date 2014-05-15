package com.e1858.utils;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.monitor.BootMonitor;

public class AppUtil
{
	private static PackageManager	packageManager;
	private static PackageInfo		packageInfo;
	private static Instrumentation	instrumentation;

	public static void hideTitle(Activity activity)
	{
		// call this before setContentView
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public static void fullScreen(Activity activity)
	{
		hideTitle(activity);
		Window window = activity.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void installApk(Context context, String apkFileName)
	{
		File apkfile = new File(apkFileName);
		if (!apkfile.exists())
		{
			Log.w(Constant.TAG_APP, new StringBuilder("apk file \"").append(apkFileName).append("\" not exist").toString());
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static PackageManager getPackageManager(Context context)
	{
		if (null == packageManager)
		{
			packageManager = context.getPackageManager();
		}
		return packageManager;
	}

	public static PackageInfo getPackageInfo(Context context)
	{
		if (null == packageInfo)
		{
			try
			{
				packageInfo = getPackageManager(context).getPackageInfo(context.getPackageName(), 0);
			}
			catch (PackageManager.NameNotFoundException ex)
			{
				Log.e(Constant.TAG_APP, ex.getMessage(), ex);
			}
		}
		return packageInfo;
	}

	public static int getVersionCode(Context context)
	{
		if (null != getPackageInfo(context))
		{
			return packageInfo.versionCode;
		}
		else
		{
			return -1;
		}
	}

	public static String getVersionName(Context context)
	{
		if (null != getPackageInfo(context))
		{
			return packageInfo.versionName;
		}
		else
		{
			return "unknow";
		}
	}

	public static void createShortcut(Context context, String name, int icon, Activity activity)
	{
		Intent shortcutIntent = new Intent(context, activity.getClass());// 快捷方式的动作
		Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		Parcelable shortcutIcon = Intent.ShortcutIconResource.fromContext(context, icon); // 获取快捷键的图标
		addIntent.putExtra("duplicate", false); // ;
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);// 快捷方式的标题
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIcon);// 快捷方式的图标
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);// 快捷方式的动作
		context.sendBroadcast(addIntent);// 发送广播
	}

	public static boolean IsExistShortCut(Context context)
	{
		boolean flag = false;
		String url = "";
		if (android.os.Build.VERSION.SDK_INT < 8)
		{
			url = "content://com.android.launcher.settings/favorites?notify=true";
		}
		else
		{
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new String[] { context.getString(R.string.app_name) }, null);

		if (cursor != null && cursor.moveToFirst())
		{
			cursor.close();
			flag = true;
		}

		return flag;
	}

	public static void dial(Context context, String number)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(new StringBuilder("tel:").append(number.trim()).toString()));
		context.startActivity(intent);
	}

	public static void call(Context context, String number)
	{
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(new StringBuilder("tel:").append(number.trim()).toString()));
		context.startActivity(intent);
	}

	public static void sendSMS(Context context, String number, String msg)
	{
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(new StringBuilder("smsto:").append(number.trim()).toString()));
		intent.putExtra("sms_body", msg);
		context.startActivity(intent);
	}

	public static void bootStartup(Activity activity)
	{
		new BootMonitor(activity);
	}

	public static HashMap getContacts(Activity activity)
	{
		ContentResolver contentresolver = activity.getContentResolver();
		Cursor cursor = contentresolver.query(android.provider.ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		HashMap hashmap = new HashMap();
		int i = 1;
		do
		{
			if (!cursor.moveToNext())
			{
				cursor.close();
				return hashmap;
			}
			HashMap hashmap1 = new HashMap();
			hashmap1.put("name", cursor.getString(cursor.getColumnIndex("display_name")));
			String s = cursor.getString(cursor.getColumnIndex("_id"));
			Cursor cursor1 = contentresolver.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, (new StringBuilder("contact_id = ")).append(s).toString(), null, null);
			if (cursor1.moveToNext())
			{
				hashmap1.put("phonenum", cursor1.getString(cursor1.getColumnIndex("data1")));
				int j = i + 1;
				hashmap.put(Double.valueOf(i), hashmap1);
				i = j;
			}
			cursor1.close();
		}
		while (true);
	}

	public static ApplicationInfo getApplicationInfo(Activity activity)
	{
		try
		{
			return getPackageManager(activity).getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return null;
		}
	}

	public static String getMetaData(Activity activity, String key)
	{
		try
		{
			ApplicationInfo applicationInfo = getPackageManager(activity).getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
			return applicationInfo.metaData.getString(key);
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return null;
		}
	}

	public static ActivityInfo geActivityInfo(Context context)
	{
		try
		{
			return getPackageManager(context).getActivityInfo(new ComponentName(context, context.getClass()), PackageManager.GET_INTENT_FILTERS | PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return null;
		}
	}

	public static ActivityInfo geActivityInfo(Context context, BroadcastReceiver receiver)
	{
		try
		{
			return getPackageManager(context).getActivityInfo(new ComponentName(context, receiver.getClass()), PackageManager.GET_INTENT_FILTERS | PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return null;
		}
	}

	public static ServiceInfo getServiceInfo(Service service)
	{
		try
		{
			return getPackageManager(service).getServiceInfo(new ComponentName(service, service.getClass()), PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			return null;
		}
	}

	public static void sendKey(final int key)
	{
		if (null == instrumentation)
		{
			instrumentation = new Instrumentation();
		}

		ThreadPool.execute(new Runnable()
		{
			public void run()
			{
				instrumentation.sendKeyDownUpSync(key);
			}
		});
	}

}
