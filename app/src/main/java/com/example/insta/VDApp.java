package com.example.insta;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class VDApp extends Application {

    private static VDApp instance;
    private static ApplicationInfo ai;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }




    public static void AdmobInitialize(Activity activity, String appid)
    {

        if(!(AppController.AdmobAppId.isEmpty()))
        {
            try {
                Bundle bundle = VDApp.ai.metaData;
                String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                //Log.d(TAG, "Name Found: " + myApiKey);
                VDApp.ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", appid);
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



}
