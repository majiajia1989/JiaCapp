package com.e1858.utils;

import android.app.Activity;

public class ScreenInfo
{
	private static int		widthPixels;
	private static int		heightPixels;
	private static int		widthDips;
	private static int		heightDips;
	private static float	density;
	private static float	scaledDensity;
	private static int		densityDpi;

	public static int getWidthPixels()
	{
		return widthPixels;
	}

	public static int getHeightPixels()
	{
		return heightPixels;
	}

	public static int getWidthDips()
	{
		return widthDips;
	}

	public static int getHeightDips()
	{
		return heightDips;
	}

	public static float getDensity()
	{
		return density;
	}

	public static float getScaledDensity()
	{
		return scaledDensity;
	}

	public static int getDensityDpi()
	{
		return densityDpi;
	}

	public static void InitValues(Activity activity)
	{
		widthPixels = DisplayUtil.getWidthPixels(activity);
		heightPixels = DisplayUtil.getWidthPixels(activity);
		widthDips = DisplayUtil.getWidthDips(activity);
		heightDips = DisplayUtil.getHeightDips(activity);
		density = DisplayUtil.getDensity(activity);
		scaledDensity = DisplayUtil.getScaleDensity(activity);
		densityDpi = DisplayUtil.getDensityDpi(activity);
	}
}
