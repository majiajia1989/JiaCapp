package com.e1858.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.e1858.CEappApp;
import com.e1858.protocol.http.IPacket;
import com.e1858.common.Constant;
import com.e1858.common.MessageWhat;
import com.e1858.utils.FileUtil;
import com.e1858.utils.ThreadPool;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

public class NetUtil
{
	private static String cookie;
	//检测网络是否连接
	public static boolean isNetworkAvailable(Activity mActivity)
	{
		if (null == mActivity) { return false; }
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null)
		{
			return false;
		}
		else
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; }
				}
			}
		}
		return false;
	}
	//检测网络状态
	public static boolean checkNetWorkStatus(Context context)
	{
		boolean result = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected())
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
	
	public static boolean isMobileActive(Context context)
	{
		boolean result = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state_Mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED == state_Mobile)
		{
			result = true;
		}
		return result;
	}

	public static boolean isWifiActive(Context context)
	{
		boolean result = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state_WIFI = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == state_WIFI)
		{
			result = true;
		}
		return result;
	}

	private static void getCookie(HttpURLConnection httpURLConnection)
	{
		cookie = httpURLConnection.getHeaderField("Set-Cookie");
		if (cookie != null)
		{
			cookie = cookie.substring(0, cookie.indexOf(";"));
		}
	}

	public static boolean checkURL(String url)
	{
		boolean result = false;
		HttpURLConnection httpURLConnection = null;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			getCookie(httpURLConnection);
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				result = true;
			}
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}
	//get请求方式
	public static String get(String url)
	{
		String result = null;
		String contentEncoding;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int readBytes = 0;
		byte[] buffer = new byte[1024];
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setDoInput(true);
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			getCookie(httpURLConnection);
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = httpURLConnection.getInputStream();
				while ((readBytes = inputStream.read(buffer)) > 0)
				{
					byteArrayOutputStream.write(buffer, 0, readBytes);
				}
			}
			if ((contentEncoding = httpURLConnection.getContentEncoding()) == null)
			{
				contentEncoding = Constant.ENCODING;
			}
			result = new String(byteArrayOutputStream.toByteArray(), contentEncoding);
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{

			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	// post not form data
	public static byte[] download(String url, byte[] data)
	{
		return download(url, data, null, 0);
	}

	//url baseurl  data：download path路径  fileName：文件名
	
	//列入："voa/", "a.txt"
	public static String getExtension(String url,byte[] data){
		String extension = null;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
//			httpURLConnection.setRequestProperty("Accept-Encoding", "identity"); 

			httpURLConnection.setInstanceFollowRedirects(true);
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			// add post data 
//			if (android.os.Build.VERSION.SDK_INT > 9) {	
//				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//				StrictMode.setThreadPolicy(policy);		  
//			} 
			outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			// get response
			getCookie(httpURLConnection);	
			
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				for(int i=0;i<Constant.MIME_MapTable.length;i++){
					if(httpURLConnection.getContentType().equals(Constant.MIME_MapTable[i][1]))
						extension = Constant.MIME_MapTable[i][0];
				}
			}
		}catch (Exception e) {
				// TODO: handle exception
			e.printStackTrace();
		}finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch (Exception ex)
				{
				}
			}

			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return extension;
	}
	
	
	
	public static int downFile(CEappApp ceapp,String url,byte[] data,String path,long id){
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		FileUtil fileUtils=new FileUtil();
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
//			httpURLConnection.setRequestProperty("Accept-Encoding", "identity"); 

			httpURLConnection.setInstanceFollowRedirects(true);
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			// add post data  
			
			outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			// get response
			getCookie(httpURLConnection);	
			
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				
				
				
				int content_length=httpURLConnection.getContentLength();
				
				Log.v("content_length", "==="+content_length);
				
				Log.v("header","===="+ httpURLConnection.getHeaderFields());


					
				String extension = null;
				Log.v("httpURLConnection.getContentType()", httpURLConnection.getContentType());
					
				for(int i=0;i<Constant.MIME_MapTable.length;i++){
					if(httpURLConnection.getContentType().equals(Constant.MIME_MapTable[i][1]))
						extension = Constant.MIME_MapTable[i][0];
				}
	
					
				
				String fileName=id+"."+extension;
				Log.v("fileName", "==="+fileName);
					
				if(fileUtils.isFileExist(path+fileName)){	
						
					Message message = ceapp.getCurrentHandler().obtainMessage(MessageWhat.ISEXISTS, "");
					ceapp.getCurrentHandler().sendMessage(message);
					return 1;
				}else{
					
					inputStream = httpURLConnection.getInputStream();
				 		
					File resultFile=fileUtils.write2SDFromInput(ceapp,httpURLConnection.getContentLength(),path, fileName, inputStream);
				 		
					if(resultFile==null){
				 		
						return -1;
					}
				}
				
			}
				
				
			
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch (Exception ex)
				{
				}
			}

			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return 0;
	}

	public static byte[] download(String url, byte[] data, final Handler handler, final int messageWhat)
	{
		byte[] result = null;
		int bufferSize = 1024;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] buffer = new byte[bufferSize];
		int readBytes = 0;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
			httpURLConnection.setInstanceFollowRedirects(true);
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			// add post data
			outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			// get response
			getCookie(httpURLConnection);
			byteArrayOutputStream = new ByteArrayOutputStream();

			
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{	
				inputStream = httpURLConnection.getInputStream();
				
				while ((readBytes = inputStream.read(buffer)) > 0)
				{
					byteArrayOutputStream.write(buffer, 0, readBytes);
				}
			}
			result = byteArrayOutputStream.toByteArray();
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch (Exception ex)
				{
				}
			}

			if (byteArrayOutputStream != null)
			{
				try
				{
					byteArrayOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	// post not form data
	public static String post(final String url, final byte[] data)
	{
		String result = null; 
		int bufferSize = 1024;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		String contentEncoding;
		byte[] buffer = new byte[bufferSize];
		int readBytes = 0;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
			httpURLConnection.setInstanceFollowRedirects(true);
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			// add post data
			outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			// get response
			getCookie(httpURLConnection);
			byteArrayOutputStream = new ByteArrayOutputStream();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				while ((readBytes = inputStream.read(buffer)) > 0)
				{
					byteArrayOutputStream.write(buffer, 0, readBytes);
				}
			}
			contentEncoding = httpURLConnection.getContentEncoding();
			if (contentEncoding == null || contentEncoding.trim().endsWith(""))
			{
				contentEncoding = Constant.ENCODING;
			}
			result = new String(byteArrayOutputStream.toByteArray(), Constant.ENCODING);
			// result =
			// ZipUtil.decompress(Encrypt.des3DecodeECB(byteArrayOutputStream.toByteArray()));
			// result = ZipUtil.decompress(Encrypt.des3DecodeECB(
			// Encrypt.base64Decode(new
			// String(byteArrayOutputStream.toByteArray(),Constant.ENCODING))));
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch (Exception ex)
				{
				}
			}

			if (byteArrayOutputStream != null)
			{
				try
				{
					byteArrayOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	// post not form data
	public static String post(final String url, final IPacket packet)
	{
		return post(url, packet.wrap());
	}

	// post not form data
	public static String post(final String url, final IPacket packet, final Handler handler, final int messageWhat)
	{
		String result = null;
		if (null != handler)
		{
			ThreadPool.execute(new Runnable()
			{
				public void run()
				{
					// TODO Auto-generated method stub
					String resultString = post(url, packet);
					if (null != handler)
					{
						Message message = handler.obtainMessage(messageWhat, resultString);
						handler.sendMessage(message);
					}
				}
			});
		}
		else
		{
			Future<String> future = ThreadPool.submit(new Callable<String>()
			{
				public String call() throws Exception
				{
					return post(url, packet);
				};
			});
			try
			{
				result = future.get();
			}
			catch (Exception ex)
			{
				return null;
			}
		}
		return result;
	}

	// post form data
	public static String post(final String url, final Map<String, String> formParameters)
	{
		String result = null;
		int bufferSize = 1024;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		DataOutputStream dataOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		String contentEncoding;
		byte[] buffer = new byte[bufferSize];
		int readBytes = 0;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
			httpURLConnection.setInstanceFollowRedirects(true);
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			// add form parameter
			StringBuilder formData = new StringBuilder("");
			if (formParameters != null)
			{
				for (Map.Entry<String, String> entry : formParameters.entrySet())
				{
					if (formData.length() > 0)
					{
						formData.append("&");
					}
					formData.append(String.valueOf(entry.getKey()));
					formData.append("=");
					formData.append(URLEncoder.encode(String.valueOf(entry.getValue()), Constant.ENCODING));
				}
			}
			dataOutputStream.writeBytes(formData.toString());
			dataOutputStream.flush();
			// get response
			getCookie(httpURLConnection);
			byteArrayOutputStream = new ByteArrayOutputStream();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				while ((readBytes = inputStream.read(buffer)) > 0)
				{
					byteArrayOutputStream.write(buffer, 0, readBytes);
				}
			}
			contentEncoding = httpURLConnection.getContentEncoding();
			if (contentEncoding == null || contentEncoding.trim().endsWith(""))
			{
				contentEncoding = Constant.ENCODING;
			}
			result = new String(byteArrayOutputStream.toByteArray(), contentEncoding);
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (dataOutputStream != null)
			{
				try
				{
					dataOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}

			if (byteArrayOutputStream != null)
			{
				try
				{
					byteArrayOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	public static File download(String url, String localFile)
	{
		File file = null;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url)).openConnection();
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			getCookie(httpURLConnection);
			inputStream = httpURLConnection.getInputStream();
			int length=httpURLConnection.getContentLength();
			String content_type=httpURLConnection.getContentType();
			Log.v("coent_type", content_type);
			
			file = new File(localFile);
			fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int readBytes;
			int total = 0;
			while ((readBytes = inputStream.read(buffer)) > 0)
			{
				fileOutputStream.write(buffer, 0, readBytes);
				fileOutputStream.flush();
				total += readBytes;
				Log.v("netutil-total", "="+total);
			}
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (fileOutputStream != null)
			{
				try
				{
					fileOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return file;
	}

	public static String upload(String url, Map<String, String> formParameters, String[] fileNames)
	{
		try
		{
			Vector<File> files = new Vector<File>();
			for (String fileName : fileNames)
			{
				files.add(new File(fileName));
			}
			return upload(url, formParameters, files);
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
			return null;
		}
	}

	public static String upload(String url, Map<String, String> formParameters, String fileName)
	{
		try
		{
			Vector<File> files = new Vector<File>();
			files.add(new File(fileName));
			return upload(url, formParameters, files);
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
			return null;
		}
	}

	// upload(post form data and attachment)
	public static String upload(String url, Map<String, String> formParameters, byte[][] datas)
	{
		String result = null;
		int bufferSize = 1024;
		HttpURLConnection httpURLConnection = null;
		FileInputStream fileInputStream = null;
		DataOutputStream dataOutputStream = null;
		BufferedInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		String contentEncoding;
		byte[] buffer = new byte[bufferSize];
		int index = 0;
		int readBytes = 0;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
			httpURLConnection.setRequestProperty("Content-Type", new StringBuilder("multipart/form-data;boundary=").append(Constant.BOUNDARY).toString());
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			// add form parameter
			if (formParameters != null)
			{
				for (Map.Entry<String, String> entry : formParameters.entrySet())
				{
					dataOutputStream.writeBytes(new StringBuilder(Constant.TWO_HYPHEN).append(Constant.BOUNDARY).append(Constant.CR_LF).toString());
					dataOutputStream.writeBytes(new StringBuilder("Content-Disposition: form-data; name=\"").append(String.valueOf(entry.getKey())).append("\"").append(Constant.CR_LF).toString());
					dataOutputStream.writeBytes(Constant.CR_LF);
					buffer = String.valueOf(entry.getValue()).getBytes(Constant.ENCODING);
					dataOutputStream.write(buffer, 0, buffer.length);
					dataOutputStream.writeBytes(Constant.CR_LF);
				}
			}
			// add file
			for (byte[] data : datas)
			{
				++index;
				dataOutputStream.writeBytes(new StringBuilder(Constant.TWO_HYPHEN).append(Constant.BOUNDARY).append(Constant.CR_LF).toString());
				dataOutputStream.writeBytes(new StringBuilder("Content-Disposition: form-data; name=\"file").append(index).append("\";filename=\"attach").append(index).append(".dat\"").append(Constant.CR_LF).toString());
				dataOutputStream.writeBytes(new StringBuilder("Content-Type: application/octet-stream").append(Constant.CR_LF).toString());
				dataOutputStream.writeBytes(Constant.CR_LF);
				dataOutputStream.write(data);
				dataOutputStream.writeBytes(Constant.CR_LF);
			}
			dataOutputStream.writeBytes(new StringBuilder(Constant.TWO_HYPHEN).append(Constant.BOUNDARY).append(Constant.TWO_HYPHEN).append(Constant.CR_LF).toString());
			dataOutputStream.flush();
			// get response
			getCookie(httpURLConnection);
			outputStream = new ByteArrayOutputStream();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				while ((readBytes = inputStream.read(buffer)) > 0)
				{
					outputStream.write(buffer, 0, readBytes);
				}
			}
			contentEncoding = httpURLConnection.getContentEncoding();
			if (contentEncoding == null || contentEncoding.trim().endsWith(""))
			{
				contentEncoding = Constant.ENCODING;
			}
			result = new String(outputStream.toByteArray(), contentEncoding);
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (fileInputStream != null)
			{
				try
				{
					fileInputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (dataOutputStream != null)
			{
				try
				{
					dataOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	// upload(post form data and attachment)
	public static String upload(String url, Map<String, String> formParameters, Vector<File> files)
	{
		String result = null;
		int bufferSize = 1024;
		HttpURLConnection httpURLConnection = null;
		FileInputStream fileInputStream = null;
		DataOutputStream dataOutputStream = null;
		BufferedInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		String contentEncoding;
		byte[] buffer = new byte[bufferSize];
		int index = 0;
		int readBytes = 0;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", Constant.ENCODING);
			httpURLConnection.setRequestProperty("Content-Type", new StringBuilder("multipart/form-data;boundary=").append(Constant.BOUNDARY).toString());
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			// add form parameter
			if (formParameters != null)
			{
				for (Map.Entry<String, String> entry : formParameters.entrySet())
				{
					dataOutputStream.writeBytes(new StringBuilder(Constant.TWO_HYPHEN).append(Constant.BOUNDARY).append(Constant.CR_LF).toString());
					dataOutputStream.writeBytes(new StringBuilder("Content-Disposition: form-data; name=\"").append(String.valueOf(entry.getKey())).append("\"").append(Constant.CR_LF).toString());
					dataOutputStream.writeBytes(Constant.CR_LF);
					buffer = String.valueOf(entry.getValue()).getBytes(Constant.ENCODING);
					dataOutputStream.write(buffer, 0, buffer.length);
					dataOutputStream.writeBytes(Constant.CR_LF);
				}
			}
			// add file
			for (File file : files)
			{
				if (!file.exists())
				{
					continue;
				}
				++index;
				dataOutputStream.writeBytes(new StringBuilder(Constant.TWO_HYPHEN).append(Constant.BOUNDARY).append(Constant.CR_LF).toString());
				dataOutputStream.writeBytes(new StringBuilder("Content-Disposition: form-data; name=\"file").append(index).append("\";filename=\"").append(file.getName()).append("\"").append(Constant.CR_LF).toString());
				dataOutputStream.writeBytes(new StringBuilder("Content-Type: application/octet-stream").append(Constant.CR_LF).toString());
				dataOutputStream.writeBytes(Constant.CR_LF);
				fileInputStream = new FileInputStream(file);
				buffer = new byte[bufferSize];
				while ((readBytes = fileInputStream.read(buffer)) > 0)
				{
					dataOutputStream.write(buffer, 0, readBytes);
				}
				dataOutputStream.writeBytes(Constant.CR_LF);
				fileInputStream.close();
			}
			dataOutputStream.writeBytes(new StringBuilder(Constant.TWO_HYPHEN).append(Constant.BOUNDARY).append(Constant.TWO_HYPHEN).append(Constant.CR_LF).toString());
			dataOutputStream.flush();
			// get response
			getCookie(httpURLConnection);
			outputStream = new ByteArrayOutputStream();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				while ((readBytes = inputStream.read(buffer)) > 0)
				{
					outputStream.write(buffer, 0, readBytes);
				}
			}
			contentEncoding = httpURLConnection.getContentEncoding();
			if (contentEncoding == null || contentEncoding.trim().endsWith(""))
			{
				contentEncoding = Constant.ENCODING;
			}
			result = new String(outputStream.toByteArray(), contentEncoding);
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (fileInputStream != null)
			{
				try
				{
					fileInputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (dataOutputStream != null)
			{
				try
				{
					dataOutputStream.close();
				}
				catch (Exception ex)
				{
				}
			}
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	public static long getFileSize(String url)
	{
		long result = -1;
		HttpURLConnection httpURLConnection = null;
		try
		{
			httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
			httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			if (cookie != null)
			{
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			getCookie(httpURLConnection);
			result = httpURLConnection.getContentLength();
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
		finally
		{
			if (httpURLConnection != null)
			{
				try
				{
					httpURLConnection.disconnect();
				}
				catch (Exception ex)
				{
				}
			}
		}
		return result;
	}

	public static void multiThreadDownload(String url, String localFile, int threadNum)
	{
		long fileSize = getFileSize(url);
		long partSize;
		long leftSize;
		long from = 0;
		long end = 0;
		RandomAccessFile file;
		if (fileSize < 0) { return; }
		partSize = fileSize / threadNum;
		leftSize = fileSize % threadNum;
		try
		{
			file = new RandomAccessFile(localFile, "rw");
			int i = 0;
			for (i = 0; i < threadNum; i++)
			{
				end = from + partSize;
				if (leftSize > 0)
				{
					end++;
					leftSize--;
				}
				new DownloadThread(url, file, from, end).start();
				from = end;
			}
			if (leftSize > 0)
			{
				end = from + leftSize;
				new DownloadThread(url, file, from, end).start();
			}
		}
		catch (Exception ex)
		{
			Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
		}
	}

	// 进行下载的线程
	static class DownloadThread extends Thread
	{
		private String url;
		private RandomAccessFile file;
		private long from;
		private long end;

		/**
		 * @param url
		 *            下载的URL
		 * @param file
		 *            下载完成之后存储的文件
		 * @param from
		 *            当前线程对应文件的起始位置
		 * @param end
		 *            当前线程对应文件的结束位置
		 */
		DownloadThread(String url, RandomAccessFile file, long from, long end)
		{
			this.url = url;
			this.file = file;
			this.from = from;
			this.end = end;
		}

		public void run()
		{
			HttpURLConnection httpURLConnection = null;
			InputStream inputStream = null;
			byte[] buffer = new byte[1024];
			int readBytes;
			try
			{
				httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
				// 设置请求的起始和结束位置。
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setRequestProperty("Range", new StringBuilder("bytes=").append(from).append("-").append(end).toString());
				if (cookie != null)
				{
					httpURLConnection.setRequestProperty("Cookie", cookie);
				}
				getCookie(httpURLConnection);
				// 如果请求错误，重试。
				if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK || httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL)
				{
					inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
					
					Log.v("inputStream","=========" +inputStream);
					
					while ((readBytes = inputStream.read(buffer)) > 0)
					{
						synchronized (file)
						{
							file.seek(from);
							file.write(buffer, 0, readBytes);
						}
						from += readBytes;
					}
				}
			}
			catch (Exception ex)
			{
				Log.e(Constant.TAG_NETWORK_HTTP, ex.getMessage(), ex);
			}
			finally
			{
				if (inputStream != null)
				{
					try
					{
						inputStream.close();
					}
					catch (Exception ex)
					{
					}
				}
				if (httpURLConnection != null)
				{
					try
					{
						httpURLConnection.disconnect();
					}
					catch (Exception ex)
					{
					}
				}
			}
		}
	}

	// post not form data in thread
	public static String postInThread(final String url, final byte[] data)
	{
		Future<String> future = ThreadPool.submit(new Callable<String>()
		{
			public String call() throws Exception
			{
				return post(url, data);
			};
		});
		try
		{
			return future.get();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	public static String uploadInThread(final String url, final Map<String, String> formParameters, final byte[][] datas, final Handler handler)
	{
		String result = null;
		if (null != handler)
		{
			ThreadPool.execute(new Runnable()
			{
				public void run()
				{
					// TODO Auto-generated method stub
					String resultString = upload(url, formParameters, datas);
					if (null != resultString)
					{
						Message message = handler.obtainMessage(MessageWhat.UPLOADRESP, resultString);
						handler.sendMessage(message);
					}
				}
			});
		}
		else
		{
			Future<String> future = ThreadPool.submit(new Callable<String>()
			{
				public String call() throws Exception
				{
					return upload(url, formParameters, datas);
				};
			});
			try
			{
				result = future.get();
			}
			catch (Exception ex)
			{
				return null;
			}
		}
		return result;
	}

	// post not form data in thread
	public static String postInThread(final String url, final IPacket packet)
	{
		return postInThread(url, packet.wrap());
	}
}
