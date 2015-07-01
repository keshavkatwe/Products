package com.dynasofts.products.Logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Keshav K on 5/13/2015.
 */
public class L {
    public static void m(String message) {
        Log.d("Sharemate", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
