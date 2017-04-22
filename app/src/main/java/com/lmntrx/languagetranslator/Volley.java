package com.lmntrx.languagetranslator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

class Volley {
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private Context context;

    private Volley(Context context){
        this.context = context;
        volley = this;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    @SuppressLint("NewApi")
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }


    @SuppressLint("StaticFieldLeak")
    private static Volley volley;

    public static synchronized Volley getInstance(Context context){

        if (volley == null)
            volley = new Volley(context);

        return volley;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    @SuppressWarnings("unused")
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    @SuppressWarnings("unused")
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
