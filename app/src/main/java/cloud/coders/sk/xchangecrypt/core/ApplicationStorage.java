package cloud.coders.sk.xchangecrypt.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import cloud.coders.sk.xchangecrypt.utils.Security;


/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class ApplicationStorage {
    private static final String APP_SHARED_PREFS = ApplicationStorage.class.getSimpleName();

    public static final String TAG_IS_DEVICE_REGISTERED = Security.sha1("registered");
    public static final String TAG_LATEST_CATEGORIES_VERSION = Security.sha1("latest_version");
    public static final String TAG_FIRST_START = Security.sha1("first_start");

    private static ApplicationStorage instance;
    private static Context context;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private ApplicationStorage(Context context) {
        ApplicationStorage.context = context;
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public boolean isFirstStart() {
        boolean result = sharedPrefs.getBoolean(TAG_FIRST_START, true);
        saveValue(TAG_FIRST_START, false);
        return result;
    }

    public static ApplicationStorage getInstance(Context context) {
        if (instance == null)
            instance = new ApplicationStorage(context);
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public void saveValue(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void saveValue(String key, Set<String> values) {
        prefsEditor.putStringSet(key, values);
        prefsEditor.commit();
    }

    public void saveValue(String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public void saveValue(String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public void saveValue(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public void saveValue(String key, float value) {
        prefsEditor.putFloat(key, value);
        prefsEditor.commit();
    }

    public void remove(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public boolean containsValue(String key) {
        return sharedPrefs.contains(key);
    }

    public String loadString(String key) {
        return sharedPrefs.getString(key, null);
    }

    public int loadInt(String key) {
        return sharedPrefs.getInt(key, 0);
    }

    public long loadLong(String key) {
        return sharedPrefs.getLong(key, 0);
    }

    public boolean loadBoolean(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public float loadFloat(String key) {
        return sharedPrefs.getFloat(key, 0);
    }

    public String loadString(String key, String defVal) {
        return sharedPrefs.getString(key, defVal);
    }

    public int loadInt(String key, int defVal) {
        return sharedPrefs.getInt(key, defVal);
    }

    public long loadLong(String key, Long defVal) {
        return sharedPrefs.getLong(key, defVal);
    }

    public boolean loadBoolean(String key, boolean defVal) {
        return sharedPrefs.getBoolean(key, defVal);
    }

    public float loadFloat(String key, float defVal) {
        return sharedPrefs.getFloat(key, defVal);
    }

    public Set<String> loadSet(String key) {
        return sharedPrefs.getStringSet(key, null);
    }

    public boolean contains(String key) {
        return sharedPrefs.contains(key);
    }
}