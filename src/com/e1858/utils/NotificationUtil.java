package com.e1858.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.e1858.CEappApp;
import com.e1858.common.IntentValue;

public class NotificationUtil
{
	private static int	NOTIFICATION_ID	= 0;

	// private static NotificationManager notificationManager = null;

	public static int create(Activity activity, int drawable, String tickerText, String contentTitle, String contentText, Class<?> startActivity, NotificationInfo notificationInfo)
	{
		++NOTIFICATION_ID;
		// 创建一个启动其他Activity的Intent
		CEappApp cEappApp = (CEappApp) activity.getApplication();
		Intent intent = new Intent(activity, startActivity);
		if (null == notificationInfo)
		{
			notificationInfo = new NotificationInfo(NOTIFICATION_ID);
		}
		intent.putExtra(IntentValue.NOTIFICATION_INFO, notificationInfo);
		PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
		// 创建一个Notification
		Notification notify = new Notification();
		// 为Notification设置图标，该图标显示在状态栏
		notify.icon = drawable;
		// 为Notification设置文本内容，该文本会显示在状态栏
		notify.tickerText = tickerText;
		// 为Notification设置发送时间
		notify.when = System.currentTimeMillis();
		// 为Notification设置声音
		// 为Notification设置默认声音、默认振动、默认闪光灯
		// notify.defaults = Notification.DEFAULT_ALL;

		// 设置事件信息
		notify.setLatestEventInfo(activity, contentTitle, contentText, pendingIntent);
		// 获取系统的NotificationManager服务
		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		// 发送通知
		notificationManager.notify(notificationInfo.getNotificationID(), notify);
		return NOTIFICATION_ID;
	}

	public static void cancel(Context context, int id)
	{
		// 获取系统的NotificationManager服务
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// 取消通知
		notificationManager.cancel(id);
	}
}
