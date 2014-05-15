package com.e1858.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.e1858.common.Constant;

public class ZipUtil
{
	public static byte[] compress(String source)
	{
		byte result[] = null;
		try
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			GZIPOutputStream gZIPPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
			gZIPPOutputStream.write(source.getBytes(Constant.ENCODING));
			gZIPPOutputStream.close();
			result = byteArrayOutputStream.toByteArray();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return result;
	}

	public static String decompress(byte[] resource)
	{
		String result = "";
		try
		{

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resource);
			GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gZIPInputStream.read(buffer)) >= 0)
			{
				byteArrayOutputStream.write(buffer, 0, n);
			}
			result = byteArrayOutputStream.toString(Constant.ENCODING);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		// toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
		return result;
	}
}
