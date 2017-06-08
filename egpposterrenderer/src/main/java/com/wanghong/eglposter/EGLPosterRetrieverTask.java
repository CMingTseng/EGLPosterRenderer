package com.wanghong.eglposter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.view.Surface;

import java.io.IOException;

/**
 * Created by Neo on 2017/6/6.
 */

public class EGLPosterRetrieverTask extends AsyncTask<Object, Integer, Bitmap> {
    private static final String TAG = EGLPosterRetrieverTask.class.getSimpleName();
    private Surface mSurface;

    @Override
    protected Bitmap doInBackground(Object... params) {
        mSurface = (Surface) params[1];
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        AssetFileDescriptor assetFileDescriptor = null;
        try {
            assetFileDescriptor = ((Context) params[0]).getAssets().openFd((String) params[2]);
            mediaMetadataRetriever.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            final Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(-1);
            if (bitmap != null) {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null && mSurface != null) {
            EGLPosterRenderer.render(bitmap).recycleBitmap(true).into(mSurface);
        }
    }
}
