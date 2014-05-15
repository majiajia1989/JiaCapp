package com.e1858.utils;

import java.io.Serializable;

public class NotificationInfo implements Serializable
{
	private int		notificationID;
	private Object	object;

	public NotificationInfo()
	{
	}

	public NotificationInfo(int notificationID)
	{
		this.notificationID = notificationID;
	}

	public NotificationInfo(Object object)
	{
		this.object = object;
	}

	public int getNotificationID()
	{
		return notificationID;
	}

	public void setNotificationID(int notificationID)
	{
		this.notificationID = notificationID;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}
}
