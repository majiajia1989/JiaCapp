package com.e1858.widget;

import java.io.ByteArrayInputStream;

import android.graphics.drawable.Drawable;

import com.e1858.CEappApp;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.protocol.http.DownLoad;
import com.e1858.utils.ImageCache;
import com.e1858.utils.ThreadPool;

public class GalleryFlowImageLoader
{
	CEappApp cEappApp;
    GalleryFlowAdapter galleryFlowAdapter;

    public GalleryFlowImageLoader(CEappApp cEappApp, GalleryFlowAdapter galleryFlowAdapter)
    {
    	this.cEappApp = cEappApp;
        this.galleryFlowAdapter = galleryFlowAdapter;
    }

    public void loadDrawable(final int downloadType, final long id)
    {
        final String key = String.valueOf(downloadType).concat("-").concat(String.valueOf(id));
        if (ImageCache.containsKey(key))
        {
            Drawable drawable = ImageCache.getDrawable(key);
            if (null != drawable)
            {
                galleryFlowAdapter.addImage(drawable, id);
                return;
            }
        }
        ThreadPool.execute(new Runnable()
        {
            public void run()
            {
                final Drawable drawable = loadImageFromUrl(downloadType, id);
                ImageCache.putDrawable(key, drawable);
                if (drawable != null)
                {
                    galleryFlowAdapter.addImage(ImageCache.getDrawable(key), id);
                }
            }
        });
    }

    private Drawable loadImageFromUrl(final int downLoadType, final long id)
    {
        Drawable result = null;
        DownLoad download = new DownLoad();
//      download.setType(downLoadType);
        download.setId(id);
        byte[] data = NetUtil.download(Constant.BASE_URL, download.wrap());
        if (null != data)
        {
            result = Drawable.createFromStream(new ByteArrayInputStream(data), "src");
        }
        return result;
    }
}
