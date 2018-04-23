package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cloud.coders.sk.BuildConfig;
import cloud.coders.sk.xchangecrypt.utils.Logger;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class PicassoManager {

    private Context context;
    private static Picasso picassoInstance = null;
    private static PicassoManager instance;
    private okhttp3.OkHttpClient okHttp3Client;

    private PicassoManager(Context context) {
        this.context = context;
        okHttp3Client = new okhttp3.OkHttpClient();
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttp3Client);
//        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
//        Downloader downloader = new OkHttpDownloader(((MainActivity) context).getCacheDir(), httpCacheSize);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(okHttp3Downloader);
        picassoInstance = builder.build();
        picassoInstance.setIndicatorsEnabled(BuildConfig.DEBUG);
    }

    public static PicassoManager getInstance(Context context) {
        if (instance == null)
            instance = new PicassoManager(context);
        return instance;
    }

    public void downloadImageWithPlaceholder(final String url, final ImageView imageView, final ProgressBar progressBar, int errrorResourceId, int placeholderResourceId) {
        startProgressLoad(progressBar);
        if (url == null || TextUtils.isEmpty(url))
            picassoInstance
                    .load(errrorResourceId)
                    .into(imageView, getCallback(url, progressBar));
        else
            picassoInstance
                    .load(url)
                    .placeholder(placeholderResourceId)
                    .error(errrorResourceId)
                    .into(imageView, getCallback(url, progressBar));
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
                Logger.e("[PicassoManager] downloadImage: " + url + " failed downloading !");
                stopProgressLoad(progressBar);
            }
        };
    }

    private void startProgressLoad(ProgressBar progressBar) {
        if (progressBar != null && !progressBar.isIndeterminate())
            progressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgressLoad(ProgressBar progressBar) {
        if (progressBar != null && progressBar.isIndeterminate())
            progressBar.setVisibility(View.INVISIBLE);
    }

    public void clearCache() {
//        picassoInstance.invalidate(((MainActivity) this.context).getCacheDir());
//        picassoInstance.invalidate();
    }
}
