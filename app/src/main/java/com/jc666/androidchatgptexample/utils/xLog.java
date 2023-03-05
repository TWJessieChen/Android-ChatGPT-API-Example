package com.jc666.androidchatgptexample.utils;


import com.jc666.androidchatgptexample.BuildConfig;

/**
 * xLog
 * 工作目的 :
 * 1.只有在debug mode下才會顯示，release mode基本上是不顯示任何有關於log資訊
 *
 * @author JC666
 */

public class xLog {

    static final boolean LOG = BuildConfig.DEBUG;

    static final String TAG = BuildConfig.TAG;

    public static void i(String string) {
        if (LOG) android.util.Log.i(TAG, string);
    }
    public static void e(String string) {
        if (LOG) android.util.Log.e(TAG, string);
    }
    public static void d(String string) {
        if (LOG) android.util.Log.d(TAG, string);
    }
    public static void v(String string) {
        if (LOG) android.util.Log.v(TAG, string);
    }
    public static void w(String string) {
        if (LOG) android.util.Log.w(TAG, string);
    }

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }
    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }
    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }
    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }
    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }
    
}
