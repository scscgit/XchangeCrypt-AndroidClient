package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Singleton Class for Picasso Downloading, Caching and Displaying Images Library
 * <p>
 * Created by V3502484 on 16. 9. 2016.
 */
public class PicassoSingleton {
    private static Picasso mInstance;
    private static long mDiskCacheSize = 50 * 1024 * 1024; //Disk Cache limit 50mb
    //private static int mMemoryCacheSize = 50*1024*1024; //Memory Cache 50mb, not currently using this. Using default implementation

    private static OkHttpClient mOkHttp3Client; //OK Http Client for downloading
    private static Cache diskCache;
    private static LruCache lruCache;//not using it currently

    public static synchronized Picasso getSharedInstance(Context context) {
        if (mInstance == null) {
            if (context != null) {
                //Create disk cache folder if does not exist
                File cache = new File(context.getApplicationContext().getCacheDir(), "picasso_cache");
                if (!cache.exists()) {
                    cache.mkdirs();
                }

                diskCache = new Cache(cache, mDiskCacheSize);
                //lruCache = new LruCache(mMemoryCacheSize);//not going to be using it, using default memory cache currently
                lruCache = new LruCache(context); // This is the default lrucache for picasso-> calculates and sets memory cache by itself

                //Create OK Http Client with retry enabled, timeout and disk cache
                mOkHttp3Client = new OkHttpClient.Builder().cache(diskCache).connectTimeout(6000, TimeUnit.SECONDS).build();  //100 min cache timeout

                //For better performance in Memory use set memoryCache(Cache.NONE) in this builder (If needed)
                mInstance = new Picasso.Builder(context).memoryCache(lruCache).downloader(new OkHttp3Downloader(mOkHttp3Client)).indicatorsEnabled(true).build();
            }
        }
        return mInstance;
    }

    public static void deletePicassoInstance() {
        mInstance = null;
    }

    public static void clearLRUCache() {
        if (lruCache != null) {
            lruCache.clear();
        }
        lruCache = null;
    }

    public static void clearDiskCache() {
        try {
            if (diskCache != null) {
                diskCache.evictAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        diskCache = null;
    }
}
