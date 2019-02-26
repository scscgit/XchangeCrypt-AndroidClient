package bit.xchangecrypt.client.network;

import android.content.Context;
import bit.xchangecrypt.client.core.ContentProvider;
import bit.xchangecrypt.client.datamodel.User;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ContentRefresher {
    private static final int NUMBER_OF_THREADS = 5;
    private static final int DELAY_INTERVAL_SECONDS = 5;

    private static ContentRefresher instance;

    private Context context;
    private ScheduledThreadPoolExecutor executor;
    private boolean loaded;

    private ContentRefresher() {
        this.executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
    }

    public static ContentRefresher getInstance(Context context) {
        if (instance == null) {
            instance = new ContentRefresher();
        }
        return instance.withContext(context);
    }

    private ContentRefresher withContext(Context context) {
        this.context = context;
        return this;
    }

    private ContentProvider getContentProvider() {
        return ContentProvider.getInstance(this.context);
    }

    public void setUser(User user) {
        loaded = false;
        loaded = getContentProvider().setUserAndLoadCache(user);
    }

    private ScheduledFuture<?> runPeriodically(Runnable runnable) {
        // Schedules the task to run after a constant delay which starts after its previous run finishes
        return this.executor.scheduleWithFixedDelay(runnable, 0, DELAY_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }
}
