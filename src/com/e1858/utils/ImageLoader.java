package com.e1858.utils;

import java.io.ByteArrayInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.e1858.CEappApp;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.protocol.http.DownLoad;

public class ImageLoader {
	protected CEappApp	cEappApp;
	private   Drawable	defaultImage;

	public ImageLoader(CEappApp cEappApp)
	{
		this.cEappApp = cEappApp;
	}

	public ImageLoader(CEappApp cEappApp, Drawable defaultImage)
	{
		this.cEappApp = cEappApp;
		this.defaultImage = defaultImage;
	}
	
	public void loadDrawable(final ImageView imageView, final int downloadType, final long id, final boolean autoSize, final boolean changeGray, final boolean isSave)
	{
		imageView.setTag(0);
		final String key = String.valueOf(downloadType).concat("-").concat(String.valueOf(id));
		Drawable cacheDrawable = ImageCache.getDrawable(key);
		setImageViewDrawable(imageView, cacheDrawable, autoSize, changeGray);
		if (null != cacheDrawable)
		{
			return;
		}

		ThreadPool.execute(new Runnable()
		{
			public void run()
			{
				final Drawable drawable = loadImageFromUrl(downloadType, id, isSave);

				if (drawable != null)
				{
					imageView.post(new Runnable()
					{
						public void run()
						{
							if (changeGray)
							{
								drawable.mutate();
								drawable.clearColorFilter();
								drawable.setColorFilter(new ColorMatrixColorFilter(Constant.COLOR_SELECTED));
							}
							imageView.setImageDrawable(drawable);
							imageView.setTag(1);

							// if (null != Config.getCurrentActivity())
							// {
							// Message message =
							// Config.getCurrentActivity().getHandler().obtainMessage(MessageWhat.ASYNC_IMAGE_LOADER);
							// Config.getCurrentActivity().getHandler().sendMessage(message);
							// }
							if (autoSize)
							{
								if (DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity()) >= imageView.getLayoutParams().width)
								{
									imageView.getLayoutParams().height = (int) ((((float) (DisplayUtil.dip2px(defaultImage.getIntrinsicHeight(), ScreenInfo.getDensity()))) / ((float) (DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity())))) * imageView.getLayoutParams().width);
								}
								else if (DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity()) < imageView.getLayoutParams().width)
								{
									imageView.getLayoutParams().width = DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity());
									imageView.getLayoutParams().height = DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicHeight(), ScreenInfo.getDensity()), ScreenInfo.getDensity());
								}

							}

						}
					});
					return;
				}
			}
		});
	}
	public void loadDrawable(final ImageView imageView, final int downloadType, final long id)
	{
		loadDrawable(imageView, downloadType, id, false, false, true);
	}
	
	
	private void setImageViewDrawable(final ImageView imageView, final Drawable drawable, final boolean autoSize, final boolean changeGray)
	{
		if (drawable != null)
		{
			if ((Integer) (imageView.getTag()) == 1)
			{
				return;
			}

			if (changeGray)
			{
				drawable.mutate();
				drawable.clearColorFilter();
				drawable.setColorFilter(new ColorMatrixColorFilter(Constant.COLOR_SELECTED));
			}
			imageView.setTag(1);
			imageView.setImageDrawable(drawable);
			// if (null != Config.getCurrentActivity())
			// {
			// Message message =
			// Config.getCurrentActivity().getHandler().obtainMessage(MessageWhat.ASYNC_IMAGE_LOADER);
			// Config.getCurrentActivity().getHandler().sendMessage(message);
			// }
			if (autoSize)
			{
				if (DisplayUtil.dip2px(DisplayUtil.dip2px(drawable.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity()) >= imageView.getLayoutParams().width)
				{
					imageView.getLayoutParams().height = (int) ((((float) (DisplayUtil.dip2px(drawable.getIntrinsicHeight(), ScreenInfo.getDensity()))) / ((float) (DisplayUtil.dip2px(drawable.getIntrinsicWidth(), ScreenInfo.getDensity())))) * imageView.getLayoutParams().width);
				}
				else if (DisplayUtil.dip2px(DisplayUtil.dip2px(drawable.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity()) < imageView.getLayoutParams().width)
				{
					imageView.getLayoutParams().width = DisplayUtil.dip2px(DisplayUtil.dip2px(drawable.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity());
					imageView.getLayoutParams().height = DisplayUtil.dip2px(DisplayUtil.dip2px(drawable.getIntrinsicHeight(), ScreenInfo.getDensity()), ScreenInfo.getDensity());
				}

			}

		}
		else
		{
			imageView.post(new Runnable()
			{
				public void run()
				{
					if ((Integer) (imageView.getTag()) == 1)
					{
						return;
					}
					if (changeGray)
					{
						defaultImage.mutate();
						defaultImage.clearColorFilter();
						defaultImage.setColorFilter(new ColorMatrixColorFilter(Constant.COLOR_SELECTED));
					}
					imageView.setImageDrawable(defaultImage);
					imageView.setTag(0);
					// if (null != Config.getCurrentActivity())
					// {
					// Message message =
					// Config.getCurrentActivity().getHandler().obtainMessage(MessageWhat.ASYNC_IMAGE_LOADER);
					// Config.getCurrentActivity().getHandler().sendMessage(message);
					// }
					if (autoSize)
					{
						if (DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity()) >= imageView.getLayoutParams().width)
						{
							imageView.getLayoutParams().height = (int) ((((float) (DisplayUtil.dip2px(defaultImage.getIntrinsicHeight(), ScreenInfo.getDensity()))) / ((float) (DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity())))) * imageView.getLayoutParams().width);
						}
						else if (DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity()) < imageView.getLayoutParams().width)
						{
							imageView.getLayoutParams().width = DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicWidth(), ScreenInfo.getDensity()), ScreenInfo.getDensity());
							imageView.getLayoutParams().height = DisplayUtil.dip2px(DisplayUtil.dip2px(defaultImage.getIntrinsicHeight(), ScreenInfo.getDensity()), ScreenInfo.getDensity());
						}

					}
				}
			});
		}

	}
	private Drawable loadImageFromUrl(final int downLoadType, final long id, boolean isSave)
	{
		Drawable result = null;
		DownLoad download = new DownLoad();
//		download.setType(downLoadType);
		download.setId(id);
		download.setUser(cEappApp.getUser());
		download.setPass(cEappApp.getPass());
		byte[] data = NetUtil.download(Constant.BASE_URL, download.wrap());
		if (null != data)
		{
			result = Drawable.createFromStream(new ByteArrayInputStream(data), "src");
			if (null != result)
			{
				ImageCache.putDrawable(String.valueOf(downLoadType).concat("-").concat(String.valueOf(id)), result);
				if (isSave)
				{
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					FileUtil.saveBmpToSd(bitmap, String.valueOf(downLoadType).concat("-").concat(String.valueOf(id)).concat(Constant.PICTURE_EXTENSION));
				}
			}
		}
		return result;
	}

	public Drawable getDefaultImage()
	{
		return defaultImage;
	}

	public void setDefaultImage(Drawable defaultImage)
	{
		this.defaultImage = defaultImage;
	}
}
