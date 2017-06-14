/*
 * Copyright 2017, Solutech RMS
 * Licensed under the Apache License, Version 2.0, "Solutech Limited".
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.json;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import me.yoctopus.utils.LogUtil;

public class Config {

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Config config;
    private String mainUrl;
    private Context context;
    private String TAG = LogUtil.makeTag(Config.class);
    public static Config create(Context context, String mainUrl) {
        return new Config(mainUrl, context);
    }

    private Config(String mainUrl,
                   Context context) {
        this.mainUrl = mainUrl;
        this.context = context;
        config = this;
    }
    public static synchronized Config getInstance() {
        return config;
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
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
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        LogUtil.e(TAG, "adding request"+tag);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, " ");
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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
    protected String getUrl(String endpoint) {
        return mainUrl.concat(endpoint);
    }
}
