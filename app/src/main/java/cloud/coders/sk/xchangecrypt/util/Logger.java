package cloud.coders.sk.xchangecrypt.util;

import android.util.Log;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
@Deprecated
public class Logger {
    private static boolean isDebugEnabled = false;
    private static final String TAG = "LocalVocal";
    private static int bufferSize = 4055;

    public static void initialize(boolean isDebugEnabled) {
        Logger.isDebugEnabled = isDebugEnabled;
    }

    public static void i(String tag, String msg) {
        if (isDebugEnabled) {
            do {
                Log.i(tag, msg.substring(0, msg.length() > bufferSize ? bufferSize : msg.length()));
                msg = msg.substring(msg.length() > bufferSize ? bufferSize : msg.length(), msg.length());
            } while (msg.length() > 0);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebugEnabled) {
            do {
                Log.e(tag, msg.substring(0, msg.length() > bufferSize ? bufferSize : msg.length()));
                msg = msg.substring(msg.length() > bufferSize ? bufferSize : msg.length(), msg.length());
            } while (msg.length() > 0);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebugEnabled) {
            do {
                Log.d(tag, msg.substring(0, msg.length() > bufferSize ? bufferSize : msg.length()));
                msg = msg.substring(msg.length() > bufferSize ? bufferSize : msg.length(), msg.length());
            } while (msg.length() > 0);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }
}
