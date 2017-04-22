/*
 * Copyright (C) 2017 Vincent Peter
 * Licensed under the Apache License, Version 2.0 Smart Bet Tips
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.smarttips;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by yoctopus on 2/18/17.
 */

public class SmartTipsApp extends MultiDexApplication {
    private String TAG =
            LogUtil.makeLogTag(
                    SmartTipsApp.class);
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static SmartTipsApp app;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MultiDex.install(this);
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
    public static synchronized SmartTipsApp getInstance() {
        return app;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(
                    getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new BitmapCache());
        }
        return this.imageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req,
                                      String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        LogUtil.d(TAG, "adding request"+tag);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, " ");
    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static class BitmapCache extends LruCache<String, Bitmap> implements
            ImageLoader.ImageCache {
        public BitmapCache() {
            this(getDefaultLruCacheSize());
        }

        public BitmapCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        public static int getDefaultLruCacheSize() {
            final int maxMemory = (int)
                    (Runtime.getRuntime().maxMemory() /
                            1024);
            return maxMemory /
                    8;
        }

        @Override
        protected int sizeOf(String key,
                             Bitmap value) {
            return value.getRowBytes() *
                    value.getHeight() /
                    1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url,
                              Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}
