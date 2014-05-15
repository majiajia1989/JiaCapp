package com.e1858.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery
{

	private Camera	camera				= new Camera();
	private int		maxRotationAngle	= 60;
	private int		maxZoom				= -120;
	private int		coveflowCenter;

	public GalleryFlow(Context context)
	{
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);

	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	
	public int getMaxRotationAngle()
	{
		return maxRotationAngle;
	}

	
	public void setMaxRotationAngle(int maxRotationAngle)
	{
		maxRotationAngle = maxRotationAngle;
	}

	
	public int getMaxZoom()
	{
		return maxZoom;
	}

	
	public void setMaxZoom(int maxZoom)
	{
		maxZoom = maxZoom;
	}

	private int getCenterOfCoverflow()
	{
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	
	private static int getCenterOfView(View view)
	{
		return view.getLeft() + view.getWidth() / 2;
	}


	protected boolean getChildStaticTransformation(View child, Transformation t)
	{

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;

		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (childCenter == coveflowCenter)
		{
			transformImageBitmap((ImageView) child, t, 0);
		}
		else
		{
			rotationAngle = (int) (((float) (coveflowCenter - childCenter) / childWidth) * maxRotationAngle);
			if (Math.abs(rotationAngle) > maxRotationAngle)
			{
				rotationAngle = (rotationAngle < 0) ? -maxRotationAngle : maxRotationAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle);
		}

		return true;
	}


	protected void onSizeChanged(int width, int height, int oldwidth, int oldheight)
	{
		coveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(width, height, oldwidth, oldheight);
	}


	private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle)
	{
		camera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		camera.translate(0.0f, 0.0f, 100.0f);

		// As the angle of the view gets less, zoom in
		if (rotation < maxRotationAngle)
		{
			float zoomAmount = (float) (maxZoom + (rotation * 1.5));
			camera.translate(0.0f, 0.0f, zoomAmount);
		}

		camera.rotateY(rotationAngle);
		camera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		camera.restore();
	}
}
