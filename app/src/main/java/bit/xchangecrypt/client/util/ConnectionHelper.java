package bit.xchangecrypt.client.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionHelper {
    /**
     * Checks whether an INTERNET connection is available.
     *
     * @return true if an INTERNET connection is available
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
