package com.dynasofts.products.Core;

import android.app.Application;
import android.content.Context;


/**
 * Created by Keshav K on 5/13/2015.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
