package cn.zj90.memo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 2015/4/8.
 */
public class PreferencesUtil {

    private static SharedPreferences prefs;

    public static final class Settings {
        public static final String SORT_EVENT = "sort_event";
        public static final String SORT_TIME = "sort_time";
        public static final String ITEM_DISTANCE = "item_distance";
    }

    public static void initShardPreferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String readString(String key) {
        return prefs.getString(key, "2");
    }

    public static Boolean readBoolean(String key) {
        return prefs.getBoolean(key, false);
    }
}
