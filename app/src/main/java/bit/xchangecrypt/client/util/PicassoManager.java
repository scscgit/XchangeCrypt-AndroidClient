package bit.xchangecrypt.client.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bit.xchangecrypt.client.BuildConfig;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class PicassoManager {
    private static final String TAG = PicassoManager.class.getSimpleName();
    private Context context;
    private static Picasso picassoInstance;
    private static PicassoManager instance;

    private PicassoManager(Context context) {
        this.context = context;
        // 10 MiB
//        long httpCacheSize = 10 * 1024 * 1024;
//        Downloader downloader = new OkHttpDownloader(((MainActivity) context).getCacheDir(), httpCacheSize);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(new okhttp3.OkHttpClient()));
        picassoInstance = builder.build();
        picassoInstance.setIndicatorsEnabled(BuildConfig.DEBUG);
    }

    public static PicassoManager getInstance(Context context) {
        if (instance == null) {
            instance = new PicassoManager(context);
        }
        return instance;
    }

    public void downloadImageWithPlaceholder(final String url, final ImageView imageView, final ProgressBar progressBar, int errrorResourceId, int placeholderResourceId) {
        startProgressLoad(progressBar);
        if (url == null || TextUtils.isEmpty(url)) {
            picassoInstance
                    .load(errrorResourceId)
                    .into(imageView, getCallback(url, progressBar));
        } else {
            picassoInstance
                    .load(url)
                    .placeholder(placeholderResourceId)
                    .error(errrorResourceId)
                    .into(imageView, getCallback(url, progressBar));
        }
    }

    public void downloadImage(final String url, final ImageView imageView, final ProgressBar progressBar, int errrorResourceId) {
        startProgressLoad(progressBar);
        if (url == null || TextUtils.isEmpty(url))
            picassoInstance
                    .load(errrorResourceId)
                    .into(imageView, getCallback(url, progressBar));
        else
            picassoInstance
                    .load(url)
                    .error(errrorResourceId)
                    .into(imageView, getCallback(url, progressBar));
    }

    public void setBackgroundResource(ImageView imageView, int resourceImageId) {
        picassoInstance
                .load(resourceImageId)
                .fit()
                .into(imageView);
    }

    private Callback getCallback(final String url, final ProgressBar progressBar) {
        return new Callback() {
            @Override
            public void onSuccess() {
                stopProgressLoad(progressBar);
            }

            @Override
            public void onError() {
                Log.e(TAG, "downloadImage: " + url + " failed downloading!");
                stopProgressLoad(progressBar);
            }
        };
    }

    private void startProgressLoad(ProgressBar progressBar) {
        if (progressBar != null && !progressBar.isIndeterminate()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopProgressLoad(ProgressBar progressBar) {
        if (progressBar != null && progressBar.isIndeterminate()) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void clearCache() {
//        picassoInstance.invalidate(((MainActivity) this.context).getCacheDir());
//        picassoInstance.invalidate();
    }
}
