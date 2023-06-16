package com.example.insta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AppController {



    public static boolean enableAdmobOpenAdShow = true;
    public static String AdmobOpenAdId = "ca-app-pub-3940256099942544/3419835294";
    public static boolean enableGotoServerActivityInter = true;
    public static boolean enableStartAppBanner = false;
    public static boolean enableChangeServerBanner = true;
    public static boolean enableChoosServerInter = true;
    public static boolean enableStartAppBtnInter = false;
    public static boolean enableMainNative = true;
    public static boolean enableStartVPNInter = true;
    public static String SplashAdmobInterid = "ca-app-pub-3940256099942544/1033173712";
    public static String SplashApplovinInterId = "SplashApplovinInterId";
    public static boolean isShow = false;
    public static String Update_URL = "";
    public static long App_Version = 1;
    public static String Title = "Update";
    public static String Message = "Newer version of app available now.";
    public static boolean IsCancelable = true;
    public static boolean enableCustom_Ad = false;
    public static String custom_image = "custom_image";
    public static String custom_url = "custom_url";
    public static long customImageHeight = 400;
    public static String ApplovinNativeId = "ApplovinNativeId";
    public static String country = "pk";
    public static String TAGBanner = "Call_Back";

    Context context;


    public AppController(Context context) {
        this.context = context;
    }

    public AppController() {
    }

    public static boolean ExitNativeEnable = false;
    public static String ExitNativePriority = "applovin";

    public static boolean enableAdmobBannerAds = false;
    public static boolean enableApplovinBannerAds = true;
    public static boolean EnableMainActivityBanner = true;
    public static boolean EnableDownloadActivityBanner = false;
    public static boolean EnableWhatsappActivityBanner = false;
    public static boolean EnableWebInter = false;
    public static boolean EnableAdmobInter = true;
    public static boolean EnableApplovinInter = true;
    public static boolean EnableSplashInter = false;
    public static long WebInterCount = 2;
    public static long StartWebCount = 1;

    public static String AdmobBannerMobid = "ca-app-pub-3940256099942544/6300978111";
    public static String AdmobInterid = "ca-app-pub-3940256099942544/1033173712";
    public static String AdmobAppId = "ca-app-pub-3940256099942544~3347511713";
    public static String Admob_native_Id = "ca-app-pub-3940256099942544/2247696110";
    public static boolean SplashAdmobPriority = false;
    public static boolean SplashApplovinPriority = false;

    public static String ApplovinBannerId = "671458f33b59d2bd";
    public static String ApplovinInterId = "808b9c2b7eae741f";
    public static NativeAd mNativeAd;



    static InterstitialAd mInterstitialAd;
    public static boolean admobInterLoaded = false;
    public static boolean applovinInterLoaded = false;
    public static String TAG = "InterAdsCallBack";


    public static void showAdmobBanner(Activity activity) {
        MyApplication.AdmobInitialize(activity, AppController.AdmobAppId);
        final AdView adView = new AdView(activity);
        adView.setAdUnitId(AppController.AdmobBannerMobid);
        adView.setAdSize(AdSize.BANNER);

        final RelativeLayout adLinLay = (RelativeLayout) activity.findViewById(R.id.adView);
        adLinLay.addView(adView);

        final AdRequest.Builder adReq = new AdRequest.Builder();
        final AdRequest adRequest = adReq.build();


        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d(TAGBanner, adError.getMessage());
            }

            @Override
            public void onAdImpression() {
            }

            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
            }
        });

        adView.loadAd(adRequest);
    }


    @SuppressLint("MissingPermission")
    public static void loadAdmobNativeAd(Activity activity) {

        AdLoader.Builder builder = new AdLoader.Builder(activity, AppController.Admob_native_Id);

        builder.forNativeAd(nativeAd ->

        {
            boolean isDestroyed = false;
            isDestroyed = activity.isDestroyed();
            if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                nativeAd.destroy();
                return;
            }
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            //if (nativeAd != null) { nativeAd.destroy(); }
            mNativeAd = nativeAd;
            LinearLayout frameLayout = activity.findViewById(R.id.native_ad_container);
            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_ad_unified_small, null);
            populateUnifiedNativeAdView(mNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.d(TAGBanner, loadAdError.getMessage());

                @SuppressLint("DefaultLocale") String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
            }
        }).build();

        Log.d("NativeLoad", "NativeLoad");
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private static void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }


    public static void interstitialAdLoad(Activity context, boolean enableAdmobInter, boolean enableApplovinInter) {


        if (enableAdmobInter) {
            AdRequest adRequest = new AdRequest.Builder().build();

            Log.d(TAG, "AdmobInterRequested");
            AppController.admobInterLoaded = true;
            InterstitialAd.load(context, AppController.AdmobInterid, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    AppController.admobInterLoaded = true;
                    Log.d(TAG, "AdmobInterLoaded");
                    super.onAdLoaded(interstitialAd);
                    mInterstitialAd = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    AppController.admobInterLoaded = false;
                    Log.d(TAG, "AdmobInterLoadedFailed");
                }
            });
        }


    }


    public static void interstitialAdShow(Activity context, final GetBackPointer getBackPointer, boolean enableAdmobInter, boolean enableApplovinInter) {


            if (mInterstitialAd != null) {
                mInterstitialAd.show(context);

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Log.d(TAG, "AdmobonAdFailedToShowFullScreenContent");
                        interstitialAdLoad(context, true, enableApplovinInter);
                        if (getBackPointer != null) {
                            getBackPointer.returnAction();
                        }

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Log.d(TAG, "AdmobonAdShowedFullScreenContent");

                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        AppController.admobInterLoaded = false;
                        Log.d(TAG, "AdmobonAdDismissedFullScreenContent");
                        interstitialAdLoad(context, true, enableApplovinInter);
                        if (getBackPointer != null) {
                            getBackPointer.returnAction();
                        }
                    }

                    @Override
                    public void onAdImpression() {
                        Log.d(TAG, "AdmobonAdImpression");
                        super.onAdImpression();
                    }

                });
            } else {
                if (getBackPointer != null) {
                    getBackPointer.returnAction();
                }
            }





    }


    public interface GetBackPointer {
        public void returnAction();
    }


}
