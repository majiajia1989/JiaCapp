package com.e1858.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Locale;

import android.util.Log;

import com.e1858.common.Constant;

public class Util
{
	public static String byte2HexString(byte abyte0[])
	{
		StringBuffer stringbuffer = new StringBuffer();
		int i = abyte0.length;
		int j = 0;
		do
		{
			if (j >= i) return stringbuffer.toString();
			stringbuffer.append(Integer.toHexString(0x100 | 0xff & abyte0[j]).substring(1));
			j++;
		}
		while (true);
	}

	public static long getUTCTime()
	{
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTimeInMillis();
	}

	public static boolean intToBool(int source)
	{
		if (source == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static int boolToInt(boolean source)
	{
		if (source)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public static Object deepClone(Object src)
	{
		Object dst = null;

		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(src);

			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			ObjectInputStream oi = new ObjectInputStream(in);
			dst = oi.readObject();
		}
		catch (Exception e)
		{
			Log.e(Constant.TAG_APP, e.getMessage(), e.getCause());
		}

		return dst;
	}
}
