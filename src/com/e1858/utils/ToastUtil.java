package com.e1858.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil
{
	public static void show(Context context, CharSequence message, boolean _short)
	{
		if (_short)
		{
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	public static void showShort(Context context, CharSequence message)
	{
		show(context, message, true);
	}

	public static void showLong(Context context, CharSequence message)
	{
		show(context, message, false);
	}

	public static void showAtCenter(Context context, CharSequence message, boolean _short)
	{
		Toast toast = null;
		if (_short)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}
		else
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showAtCenterShort(Context context, CharSequence message)
	{
		showAtCenter(context, message, true);
	}

	public static void showAtCenterLong(Context context, CharSequence message)
	{
		showAtCenter(context, message, false);
	}

	public static void showWithPic(Context context, int drawable, CharSequence message, boolean _short)
	{
		Toast toast = null;
		if (_short)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}
		else
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		}
		ImageView image = new ImageView(context);
		image.setImageResource(drawable);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setHorizontalGravity(LinearLayout.HORIZONTAL);
		linearLayout.addView(image);
		linearLayout.addView(toast.getView());
		toast.setView(linearLayout);
		toast.show();
	}

	public static void showWithPicShort(Context context, int drawable, CharSequence message)
	{
		showWithPic(context, drawable, message, true);
	}

	public static void showWithPicLong(Context context, int drawable, CharSequence message)
	{
		showWithPic(context, drawable, message, false);
	}

	public static void showWithPicAtCenter(Context context, int drawable, CharSequence message, boolean _short)
	{
		Toast toast = null;
		if (_short)
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}
		else
		{
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		ImageView image = new ImageView(context);
		image.setImageResource(drawable);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setHorizontalGravity(LinearLayout.HORIZONTAL);
		linearLayout.addView(image);
		linearLayout.addView(toast.getView());
		toast.setView(linearLayout);
		toast.show();
	}

	public static void showWithPicAtCenterShort(Context context, int drawable, CharSequence message)
	{
		showWithPicAtCenter(context, drawable, message, true);
	}

	public static void showWithPicAtCenterLong(Context context, int drawable, CharSequence message)
	{
		showWithPicAtCenter(context, drawable, message, false);
	}
}
