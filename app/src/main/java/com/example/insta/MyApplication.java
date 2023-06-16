package com.example.insta;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.DateUtils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.ArrayMap;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;



public class MyApplication extends Application {


    private static MyApplication sInstance;



    private static ApplicationInfo ai;





    private static AppOpenManager appOpenManager;

    private static MyApplication app;














    public static void AdmobOpenAd()
    {
        appOpenManager = new AppOpenManager(MyApplication.sInstance);
    }
    public static AppOpenManager getAppOpenManagerInstance() {
        return appOpenManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        sInstance = this;


    }

    public static void AdmobInitialize(Activity activity, String appid)
    {

        if(!(AppController.AdmobAppId.isEmpty()))
        {
            try {
                Bundle bundle = MyApplication.ai.metaData;
                String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                //Log.d(TAG, "Name Found: " + myApiKey);
                MyApplication.ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", appid);
                String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                //Log.d(TAG, "ReNamed Found: " + ApiKey);
            } catch (NullPointerException e) {
                //Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }


        MobileAds.initialize(
                activity,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });
    }





    public static synchronized MyApplication getInstance() {
        return sInstance;
    }


}
