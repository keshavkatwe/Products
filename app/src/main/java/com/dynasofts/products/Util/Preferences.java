package com.dynasofts.products.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Keshav K on 5/20/2015.
 */
public class Preferences {
    public static void saveToPreferences(Context context, String prefFileName, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.commit();
    }

    public static String readFromPreferences(Context context, String prefFileName, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
