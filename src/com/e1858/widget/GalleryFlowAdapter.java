package com.e1858.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.e1858.CEappApp;
import com.e1858.common.MessageWhat;
import com.e1858.utils.DisplayUtil;
import com.e1858.utils.ImageUtil;
import com.e1858.utils.ScreenInfo;
import com.e1858.utils.ThreadPool;

public class GalleryFlowAdapter extends BaseAdapter
{
    int                            galleryItemBackground;
    private Context                context;
    private Handler                handler;
    private ArrayList<ImageView>   images;
    private GalleryFlowImageLoader galleryFlowImageLoader;
    private CEappApp              cEappApp;

    public GalleryFlowAdapter(CEappApp cEappApp, Context context, Handler handler)
    {
        this.context = context;
        this.handler = handler;
        images = new ArrayList<ImageView>();
        this.cEappApp = cEappApp;
        galleryFlowImageLoader = new GalleryFlowImageLoader(cEappApp, this);
    }

    public void addImage(final int downloadType, final long id)
    {
        ThreadPool.execute(new Runnable()
        {
            public void run()
            {
                galleryFlowImageLoader.loadDrawable(downloadType, id);
            }
        });
    }

    public void removeImage(long index)
    {
        Object obj = images.remove((int) index);
        obj = null;      
        Message message = handler.obtainMessage(MessageWhat.DELETE_GALLERY, "");
        handler.sendMessage(message);
    }

    public void addImage(Drawable drawable, long id)
    {
        // The gap we want between the reflection and the original image

        final int reflectionGap = 4;
        int index = 0;
        Bitmap originalImage = ImageUtil.drawable2Bitmap(drawable);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // This will not scale but will flip on the Y axis
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        // Create a Bitmap with the flip matrix applied to it.
        // We only want the bottom half of the image
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);

        // Create a new bitmap with same width but taller to fit
        // reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        // Create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        Canvas canvas = new Canvas(bitmapWithReflection);
        // Draw in the original image
        canvas.drawBitmap(originalImage, 0, 0, null);
        // Draw in the gap
        Paint deafaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
        // Draw in the reflection
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        // Create a shader that is a linear gradient that covers the
        // reflection
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        // Set the paint to use this shader (linear gradient)
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        BitmapDrawable bd = new BitmapDrawable(bitmapWithReflection);
        bd.setAntiAlias(true);

        ImageView imageView = new ImageView(context);
        imageView.setTag(id);
        // imageView.setImageBitmap(bitmapWithReflection);
        imageView.setImageDrawable(bd);
        // imageView.setLayoutParams(new
        // GalleryFlow.LayoutParams(DisplayUtil.dip2px(140,
        // ScreenInfo.getDensity()), DisplayUtil.dip2px(240,
        // ScreenInfo.getDensity())));
        imageView.setLayoutParams(new GalleryFlow.LayoutParams(DisplayUtil.dip2px((ScreenInfo.getWidthDips() - 60) / 2, ScreenInfo.getDensity()), DisplayUtil.dip2px((ScreenInfo.getHeightDips()-70) / 2, ScreenInfo.getDensity())));

        // imageView.setScaleType(ScaleType.MATRIX);
        images.add(imageView);

        Message message = handler.obtainMessage(MessageWhat.UPDATE_GALLERY, "");
        handler.sendMessage(message);
    }

    public int getCount()
    {
        return images.size();
    }

    public Object getItem(int position)
    {
        return images.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        return images.get(position);
    }

    /**
     * Returns the size (0.0f to 1.0f) of the views depending on the 'offset' to
     * the center.
     */
    public float getScale(boolean focused, int offset)
    {
        /* Formula: 1 / (2 ^ offset) */
        return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
    }

}
