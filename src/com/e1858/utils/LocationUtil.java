package com.e1858.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.e1858.common.Constant;

public class LocationUtil
{
	private final static double		EARTH_RADIUS	= 6378137.0;
	private static CdmaCellLocation	location		= null;

	/** 基站信息 */
	public static class SCell
	{
		public int		CID;		// cellId基站编号，是个16位的数据（范围是0到65535）
		public int		MCC;		// mobileCountryCode移动国家代码（中国的为460）
		public int		MNC;		// mobileNetworkCode移动网络号码（中国移动为00，中国联通为01）
		public int		LAC;		// locationAreaCode位置区域码
		public String	radioType;	// 联通移动gsm，电信cdma

	}

	/** 经纬度 */
	public static class SItude
	{
		public String	latitude;
		public String	longitude;
	}

	/**
	 * 根据基站数据获取经纬度 SItude itude = getItude(getCellInfo(activity)); 获取地理位置 String
	 * location = getLocation(getItude(getCellInfo()));
	 */

	/** 获取基站信息:移动，联通2G 网都可使用GsmCellLocation,电信2G,3G网则使用CdmaCellLocation **/
	public static SCell getCellInfo(Activity activity)
	{
		SCell cell = new SCell();

		try
		{
			/** 调用API获取基站信息 */
			TelephonyManager mTelNet = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
			int type = mTelNet.getNetworkType();
			int phoneType = mTelNet.getPhoneType();
			// 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
			if (type == TelephonyManager.NETWORK_TYPE_EVDO_0 || type == TelephonyManager.NETWORK_TYPE_EVDO_A || type == TelephonyManager.NETWORK_TYPE_CDMA || type == TelephonyManager.NETWORK_TYPE_1xRTT)
			{
				location = (CdmaCellLocation) mTelNet.getCellLocation();

				StringBuilder nsb = new StringBuilder();
				nsb.append(location.getSystemId());

				cell.CID = location.getBaseStationId();
				cell.LAC = location.getNetworkId();
				cell.MNC = location.getSystemId();
				cell.MCC = Integer.valueOf(mTelNet.getNetworkOperator().substring(0, 3));
				cell.radioType = "cdma";

			}
			// 移动2G卡 + CMCC + 2 type = NETWORK_TYPE_EDGE
			else if (type == TelephonyManager.NETWORK_TYPE_GPRS || type == TelephonyManager.NETWORK_TYPE_EDGE || type == TelephonyManager.NETWORK_TYPE_HSDPA)
			{
				GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
				String operator = mTelNet.getNetworkOperator();
				if (!operator.equals(""))
				{
					cell.CID = location.getCid();
					cell.MCC = Integer.parseInt(operator.substring(0, 3));
					cell.MNC = Integer.parseInt(operator.substring(3));
					cell.LAC = location.getLac();
					cell.radioType = "gsm";
				}
			}
			// 联通的2G经过测试 China Unicom 1 NETWORK_TYPE_GPRS
			// 经过测试，获取联通数据的时候，无法获取国家代码和网络号码，错误类型为JSON Parsing Error
			else if (type == TelephonyManager.NETWORK_TYPE_GPRS || type == TelephonyManager.NETWORK_TYPE_UMTS)
			{
				GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
				cell.CID = location.getCid();
				cell.LAC = location.getLac();
				cell.radioType = "gsm";
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return cell;
	}

	/** 获取经纬度 **/
	public static SItude getItude(SCell cell)
	{
		SItude itude = new SItude();

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.google.com/loc/json");

		try
		{
			/** 构造POST的JSON数据 */
			JSONObject holder = new JSONObject();
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("request_address", true);
			holder.put("radio_type", cell.radioType);
			holder.put("home_mobile_country_code", cell.MCC);
			holder.put("home_mobile_network_code", cell.MNC);
			holder.put("address_language", "zh_CN");

			JSONObject tower = new JSONObject();
			tower.put("mobile_country_code", cell.MCC);
			tower.put("mobile_network_code", cell.MNC);
			tower.put("cell_id", cell.CID);
			tower.put("location_area_code", cell.LAC);
			tower.put("age", 0);

			JSONArray towerarray = new JSONArray();
			towerarray.put(tower);

			holder.put("cell_towers", towerarray);

			StringEntity query = new StringEntity(holder.toString());
			post.setEntity(query);

			/** 发出POST数据并获取返回数据 */
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();

			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null)
			{
				strBuff.append(result);
			}

			/** 解析返回的JSON数据获得经纬度 */
			JSONObject json = new JSONObject(strBuff.toString());
			JSONObject subjosn = new JSONObject(json.getString("location"));

			itude.latitude = subjosn.getString("latitude");
			itude.longitude = subjosn.getString("longitude");

			Log.i(Constant.TAG_LOCATION, "latitude:" + itude.latitude + "longitude:" + itude.longitude);

		}
		catch (Exception e)
		{
			itude.latitude = String.valueOf(Constant.MAX_LATITUDE);
			itude.longitude = String.valueOf(Constant.MAX_LONGITUDE);

		}
		finally
		{
			post.abort();
			client = null;
		}

		return itude;
	}

	public static double getDistance(double resLatitude, double resLongitude, double destLatitude, double destLongitude)
	{
		double result;
		if (resLatitude >= Constant.MAX_LATITUDE || resLongitude >= Constant.MAX_LONGITUDE || destLatitude >= Constant.MAX_LATITUDE || destLongitude >= Constant.MAX_LONGITUDE)
		{
			result = -1;
		}
		else
		{
			double radLat1 = (resLatitude * Math.PI / 180.0);
			double radLat2 = (destLatitude * Math.PI / 180.0);
			double a = radLat1 - radLat2;
			double b = (resLongitude - destLongitude) * Math.PI / 180.0;
			result = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
			result = result * EARTH_RADIUS;
			result = Math.round(result * 10000) / 10000;
		}
		return result;
	}

	public static String getDistanceStr(double resLatitude, double resLongitude, double destLatitude, double destLongitude)
	{
		double distance = getDistance(resLatitude, resLongitude, destLatitude, destLongitude);
		String result = "";
		if (distance <= -1)
		{
			result = "未知";
		}
		else if (distance >= 1000)
		{
			result = String.valueOf((int) Math.ceil(distance / 1000)) + "公里以内";
		}
		else if (distance > 0)
		{
			result = String.valueOf((int) Math.floor(distance)) + "米以内";
		}
		return result;
	}

	/**
	 * 获取地理位置
	 */
	public static String getLocation(SItude itude)
	{
		String resultString = "";

		/** 这里采用get方法，直接将参数加到URL上 */
		String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.latitude, itude.longitude);

		/** 新建HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** 采用GET方法 */
		HttpGet get = new HttpGet(urlString);
		try
		{
			/** 发起GET请求并获得返回数据 */
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null)
			{
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			/** 解析JSON数据，获得物理地址 */
			if (resultString != null && resultString.length() > 0)
			{
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++)
				{
					resultString = jsonArray.getJSONObject(i).getString("address");
				}
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			get.abort();
			client = null;
		}
		Log.i(Constant.TAG_LOCATION, "当前位置：" + resultString);
		return resultString;
	}

}
