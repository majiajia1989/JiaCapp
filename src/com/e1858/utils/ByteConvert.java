package com.e1858.utils;

public class ByteConvert
{
	// long类型转成byte数组
	public static byte[] longToByteArr(long number)
	{
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = new Long(temp & 0xff).byteValue();
			// 将最低位保存在最低位
			temp = temp >> 8;// 向右移8位
		}
		return b;
	}

	// byte数组转成long
	public static long byteArrToLong(byte[] b)
	{
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;

		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * 注释：int到字节数组的转换！
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToByteArr(int number)
	{
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8;// 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到int的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArrToInt(byte[] b)
	{
		int s = 0;
		int s0 = b[0] & 0xff;// 最低位
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}

	/**
	 * 注释：short到字节数组的转换！
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByteArr(short number)
	{
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8;// 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到short的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static short byteArrToShort(byte[] b)
	{
		short s = 0;
		short s0 = (short) (b[0] & 0xff);// 最低位
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * 注释：字节数组到String的转换！
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteArrToString(byte[] bytes)
	{

		String strValue = "";
		for (int i = 0; i < bytes.length; i++)
		{
			strValue += Integer.toHexString(bytes[i] & 0xFF);
		}

		return strValue;
	}

}
