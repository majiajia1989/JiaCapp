package com.e1858.monitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.e1858.common.Constant;

public class BootMonitor extends BroadcastReceiver
{
	private Activity	startActivity;

	public BootMonitor()
	{
	}

	public BootMonitor(Activity activity)
	{
		this.startActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i(Constant.TAG_BROADCAST_RECEIVER, intent.getAction());
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			Intent newIntent = new Intent(context, startActivity.getClass());
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newIntent);
		}
	}

	public void setStartActivity(Activity startActivity)
	{
		this.startActivity = startActivity;
	}
}
