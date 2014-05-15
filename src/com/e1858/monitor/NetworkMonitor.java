package com.e1858.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.e1858.CEappApp;
import com.e1858.ceapp.R;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.utils.ThreadPool;
import com.e1858.utils.ToastUtil;

public class NetworkMonitor extends BroadcastReceiver
{
	private Handler			handler;
	private CEappApp		cEappApp;
	private NetworkInfo		networkInfo_WIFI;
	private NetworkInfo		networkInfo_Mobile;
	private State			state_WIFI;
	private State			state_Mobile;
	public NetworkMonitor(CEappApp cEappApp)
	{
		this.cEappApp = cEappApp;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
		{
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

			handler = cEappApp.getCurrentHandler();
			if (networkInfo != null && networkInfo.isAvailable())
			{
				Log.i(Constant.TAG_NETWORK, "network is available");
				cEappApp.setNetworkAvailable(true);

				if (cEappApp.isLoginOK())
				{
					ToastUtil.showAtCenterShort(cEappApp.getCurrentActivity(), cEappApp.getCurrentActivity().getResources().getString(R.string.network_success));
					ThreadPool.execute(new Runnable()
					{
						public void run()
						{
							try
							{
								Thread.sleep(Constant.NETWORK_AVAILABLE_SOCKET_OPEN_INTERVAL);
							}
							catch (InterruptedException e)
							{
							}

							cEappApp.getSocketSession().open();
						}
					});
				}
				if (null != handler)
				{
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_OPENED, networkInfo);
					handler.sendMessage(msg);
				}
			}
			else
			{
				
				
				ToastUtil.showAtCenterShort(cEappApp.getCurrentActivity(),cEappApp.getCurrentActivity().getResources().getString(R.string.network_fail));
				Log.i(Constant.TAG_NETWORK, "network is inavailable");
				cEappApp.setNetworkAvailable(false);
				if (null != handler)
				{
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_CLOSED, networkInfo);
					handler.sendMessage(msg);
				}
			}

			networkInfo_WIFI = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			networkInfo_Mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			state_WIFI = networkInfo_WIFI.getState();
			state_Mobile = networkInfo_Mobile.getState();

			if (State.CONNECTED == state_WIFI)
			{
				Log.i(Constant.TAG_NETWORK, "WIFI is available");
				if (null != handler)
				{
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_WIFI_OPENED, networkInfo_WIFI);
					handler.sendMessage(msg);
				}
			}
			else
			{
				Log.i(Constant.TAG_NETWORK, "WIFI is inavailable");
				if (null != handler)
				{
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_WIFI_CLOSED, networkInfo_WIFI);
					handler.sendMessage(msg);
				}
			}

			if (State.CONNECTED == state_Mobile)
			{
				if (State.CONNECTED != state_WIFI && cEappApp.isLoginOK())
				{
					ToastUtil.showAtCenterLong(cEappApp.getCurrentActivity(),cEappApp.getCurrentActivity().getResources().getString(R.string.network_mobile));
				}
				Log.i(Constant.TAG_NETWORK, "mobile data is available");
				if (null != handler)
				{
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_MOBILE_OPENED, networkInfo_Mobile);
					handler.sendMessage(msg);
				}
			}
			else
			{
				if (null != handler)
				{
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_MOBILE_CLOSED, networkInfo_Mobile);
					handler.sendMessage(msg);
				}
				Log.i(Constant.TAG_NETWORK, "mobile is inavailable");
			}
		}
	}
}
