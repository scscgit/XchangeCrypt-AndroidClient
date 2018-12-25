package cloud.coders.sk.xchangecrypt.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import cloud.coders.sk.xchangecrypt.util.ConnectionHelper;

/**
 * Already implemented by ApiInvoker.
 * Created by V3502484 on 16. 9. 2016.
 */
@Deprecated
public class RestHelper {
    public static final String TAG = RestHelper.class.getSimpleName();
    private static RestHelper mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private RestHelper(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RestHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RestHelper(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        if (ConnectionHelper.isOnline(mContext))
            getRequestQueue().add(request);
        else {
            request.getErrorListener().onErrorResponse(new VolleyError("Connection not available"));
        }
    }

    public void cancelAllRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}
