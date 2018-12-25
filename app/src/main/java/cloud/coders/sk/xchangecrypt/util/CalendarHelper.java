package cloud.coders.sk.xchangecrypt.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class CalendarHelper {
    /* Log tag */
    public static final String TAG = CalendarHelper.class.getSimpleName();

    /* Kalendar */
    // pre Ice Cream Sandwich a vyssie
    public static final String CAL_DISPLAY_NAME_ICS_AND_ABOVE = "calendar_displayName";
    public static final String CAL_ACCESS_LEVEL_ICS_AND_ABOVE = "calendar_access_level";
    // pre verzie nizsie ako ICS
    public static final String CAL_DISPLAY_NAME_ICS_BELOW = "displayName";
    public static final String CAL_ACCESS_LEVEL_ICS_BELOW = "access_level";
    // spolocne pre vsetky verzie
    public static final String CAL_ID = "_id";
    public static final Uri CAL_CONTENT_URI = Uri.parse("content://com.android.calendar/calendars");

    /**
     * Aktivny kalendar v mobilnom zariadeni
     */
    public static class Calendar {
        public long id;
        public String name;
        public int accessLevel;

        public Calendar(long id, String name, int accessLevel) {
            this.id = id;
            this.name = name;
            this.accessLevel = accessLevel;
        }

        public boolean canBeModified() {
            if (accessLevel >= 500) return true;
            else return false;
        }
    }

    public static float getHueColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        float hsv[] = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        return hsv[0];
    }

    /**
     * Vyhlada aktivne kalendare v zariadeni
     *
     * @return zoznam kalendarov
     */
    public static List<Calendar> getCalendars(Context ctxt) {
        List<Calendar> calendars = new ArrayList<Calendar>();
        Uri contentUri;
        String calendarId, displayName, accessLevelString;
        Cursor cursor = null;
        ContentResolver cr = ctxt.getContentResolver();

        if (Build.VERSION.SDK_INT < 14) {
            displayName = CAL_DISPLAY_NAME_ICS_BELOW;
            accessLevelString = CAL_ACCESS_LEVEL_ICS_BELOW;
        } else {
            displayName = CAL_DISPLAY_NAME_ICS_AND_ABOVE;
            accessLevelString = CAL_ACCESS_LEVEL_ICS_AND_ABOVE;
        }
        contentUri = CAL_CONTENT_URI;
        calendarId = CAL_ID;
        String[] projection = {calendarId, displayName, accessLevelString};

        cursor = cr.query(contentUri, projection, null, null, null);
        if (null == cursor) {
            Log.w(TAG, "V zariadeni nie je ziadny kalendar");
            return calendars;
        }

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(calendarId));
            String name = cursor.getString(cursor.getColumnIndex(displayName));
            int accessLevel = cursor.getInt(cursor.getColumnIndex(accessLevelString));
            calendars.add(new Calendar(id, name, accessLevel));
        }
        cursor.close();

        return calendars;
    }
}
