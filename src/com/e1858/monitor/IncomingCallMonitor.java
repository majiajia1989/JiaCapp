package com.e1858.monitor;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.e1858.common.Constant;

public class IncomingCallMonitor extends BroadcastReceiver
{
	private static Vector<IncomingCallListener>	incomingCallListeners	= new Vector<IncomingCallListener>();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i(Constant.TAG_BROADCAST_RECEIVER, intent.getAction());
		if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
		{
			String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			Log.i(Constant.TAG_CALL, new StringBuilder("incoming clall:").append(number).toString());
			for (IncomingCallListener incomingCallListener : incomingCallListeners)
			{
				if (null != incomingCallListener)
				{
					incomingCallListener.onIncomingCall(number);
				}
			}
		}
	}

	public static void addIncomingCallListener(IncomingCallListener incomingCallListener)
	{
		if (null != incomingCallListener)
		{
			incomingCallListeners.add(incomingCallListener);
		}
	}

	public static void removeIncomingCallListener(IncomingCallListener incomingCallListener)
	{
		if (null != incomingCallListener)
		{
			incomingCallListeners.remove(incomingCallListener);
		}
	}
}
