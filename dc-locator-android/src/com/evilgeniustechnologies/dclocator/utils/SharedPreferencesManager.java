package com.evilgeniustechnologies.dclocator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * @author khiemvx
 */
public class SharedPreferencesManager {
    public static final String FILE_NAME_SHARE = "Cache";
    private SharedPreferences sharedPreferences = null;
    private Editor editor;

    /**
     * @param context context
     */
    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(FILE_NAME_SHARE, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void clearCache() {
        sharedPreferences.edit().clear().commit();
    }

    /**
     * @param key ket
     * @return boolean
     */
    public boolean getPreference(String key) {
        try {
            return sharedPreferences.getBoolean(key, false);
        } catch (Exception e) {

            try {
                editor.putBoolean(key, true);
                editor.commit();
            } catch (Exception e2) {
                Log.d("Exception", e.getMessage());
            }
            return true;
        }
    }

    /**
     * @param key   key
     * @param value value
     */
    public void setValue(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
            editor.commit();
        }
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
            editor.commit();
        }

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
            editor.commit();
        }

    }

    public void removeValue(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * @param key key
     * @return String
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * @param key key
     * @return boolean
     */
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * @param key key
     * @return int
     */
    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }


}
