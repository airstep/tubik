package com.tgs.tubik.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import java.io.File;

public class SettingsManager {
    private static final String PREFS_NAME = "tubik_settings";
    public static final java.lang.String TAG_IMAGE_CACHE_DIR = "image_cache_dir";

    public static boolean isValue(Context c, String key) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getBoolean(key, true);
    }

    public static boolean isValue(Context c, String key, boolean def) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getBoolean(key, def);
    }

    public static String getString(Context c, String key) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getString(key, null);
    }

    public static String getString(Context c, String key, String def) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getString(key, def);
    }

    public static int getInt(Context c, String key) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getInt(key, -1);
    }

    public static int getInt(Context c, String key, int def) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getInt(key, def);
    }

    public static long getLong(Context c, String key) {
        SharedPreferences data = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return data.getLong(key, -1);
    }

    public static void setValue(Context c, String key, boolean value) {
        SharedPreferences.Editor out = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        out.putBoolean(key, value);
        out.commit();
    }

    public static void setValue(Context c, String key, String value) {
        SharedPreferences.Editor out = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        out.putString(key, value);
        out.commit();
    }

    public static void setValue(Context c, String key, int value) {
        SharedPreferences.Editor out = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        out.putInt(key, value);
        out.commit();
    }

    public static void setValue(Context c, String key, long value) {
        SharedPreferences.Editor out = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        out.putLong(key, value);
        out.commit();
    }
}
