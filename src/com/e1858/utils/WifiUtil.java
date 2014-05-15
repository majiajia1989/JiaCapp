package com.e1858.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/*
 *android.permission.ACCESS_WIFI_STATE   
 *android.permission.CHANGE_WIFI_STATE   
 *android.permission.WAKE_LOCK
 */
public class WifiUtil
{

	private WifiManager				wifiManager			= null;
	private WifiInfo				wifiInfo			= null;
	private List<ScanResult>		wifiList			= null; // 扫描出的网络连接列表
	private List<WifiConfiguration>	wifiConfiguration	= null; // 网络连接列表
	private WifiLock				wifiLock			= null;

	public WifiUtil(Context context)
	{
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
	}

	public void openWifi()
	{
		if (!wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(true);// 打开wifi
		}
	}

	public void closeWifi()
	{
		if (wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(false);// 关闭wifi
		}
	}

	public void lockWifi()
	{
		wifiLock.acquire();// 锁定wifi,当下载大文件时需要锁定
	}

	public void releaseWifi()
	{
		if (wifiLock.isHeld())
		{
			wifiLock.acquire();// 解锁wifi
		}
	}

	public void createWifiLock()
	{
		wifiLock = wifiManager.createWifiLock("Testss");// 创建一个wifilock
	}

	public List<WifiConfiguration> getConfinguration()
	{
		return wifiConfiguration;// 得到配置好的网络
	}

	// 指定配置好的网络进行连接
	public void connectConfiguration(int index)
	{
		if (index > wifiConfiguration.size())
		{
			return;
		}
		wifiManager.enableNetwork(wifiConfiguration.get(index).networkId, true);// 连接配置好的指定ID的网络
	}

	public void scan()
	{
		wifiManager.startScan();
		// 得到扫描结果
		wifiList = wifiManager.getScanResults();
		// 得到配置好的网络连接
		wifiConfiguration = wifiManager.getConfiguredNetworks();
	}

	// 得到网络列表
	public List<ScanResult> getWifiList()
	{
		return wifiList;
	}

	// 查看扫描结果
	public StringBuilder lookUpScan()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < wifiList.size(); i++)
		{
			stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			stringBuilder.append((wifiList.get(i)).toString());
			stringBuilder.append("\n");
		}
		return stringBuilder;
	}

	// 得到MAC地址
	public String getMacAddress()
	{
		return (wifiInfo == null) ? null : wifiInfo.getMacAddress();
	}

	// 得到接入点的BSSID
	public String getBSSID()
	{
		return (wifiInfo == null) ? null : wifiInfo.getBSSID();
	}

	// 得到IP地址
	public int getIPAddress()
	{
		return (wifiInfo == null) ? 0 : wifiInfo.getIpAddress();
	}

	// 得到连接的ID
	public int getNetworkId()
	{
		return (wifiInfo == null) ? 0 : wifiInfo.getNetworkId();
	}

	// 得到WifiInfo的所有信息包
	public String getWifiInfo()
	{
		return (wifiInfo == null) ? null : wifiInfo.toString();
	}

	// 添加一个网络并连接
	public void addNetwork(WifiConfiguration wcg)
	{
		int wcgID = wifiManager.addNetwork(wcg);
		wifiManager.enableNetwork(wcgID, true);
	}

	// 断开指定ID的网络
	public void disconnectWifi(int netId)
	{
		wifiManager.disableNetwork(netId);
		wifiManager.disconnect();
	}
}
