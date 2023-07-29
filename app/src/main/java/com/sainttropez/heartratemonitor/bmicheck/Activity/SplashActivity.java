package com.sainttropez.heartratemonitor.bmicheck.Activity;

import static com.adsmodule.api.AdsModule.Retrofit.APICallHandler.callAdsApi;
import static com.adsmodule.api.AdsModule.Utils.Global.checkAppVersion;
import static com.adsmodule.api.AdsModule.Utils.StringUtils.isNull;
import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;
import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.MyApplication.getConnectionStatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Retrofit.AdsDataRequestModel;
import com.adsmodule.api.AdsModule.Utils.ConnectionDetector;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.adsmodule.api.AdsModule.Utils.Global;
import com.sainttropez.heartratemonitor.bmicheck.BuildConfig;
import com.sainttropez.heartratemonitor.bmicheck.R;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    Activity splashActivity;

    //public static String testString = "{\n" + "    \"url\": \"http://192.168.0.178:8000/api/app_list/applist/14/\",\n" + "    \"app_name\": \"AdsModule\",\n" + "    \"package_name\": \"com.tools.downaloader.adsmodule\",\n" + "    \"show_ads\": true,\n" + "    \"app_open_ads\": {\n" + "        \"Facebook\": \"ca-app-pub-3940256099942544/3419835294\",\n" + "        \"Adx\": \"ca-app-pub-3940256099942544/3419835294\",\n" + "        \"Admob\": \"ca-app-pub-3940256099942544/3419835294\"\n" + "    },\n" + "    \"banner_ads\": {\n" + "        \"Facebook\": \"ca-app-pub-3940256099942544/6300978111\",\n" + "        \"Adx\": \"ca-app-pub-3940256099942544/6300978111\",\n" + "        \"Admob\": \"ca-app-pub-3940256099942544/6300978111\"\n" + "    },\n" + "    \"native_ads\": {\n" + "        \"Facebook\": \"ca-app-pub-3940256099942544/2247696110\",\n" + "        \"Adx\": \"ca-app-pub-3940256099942544/2247696110\",\n" + "        \"Admob\": \"ca-app-pub-3940256099942544/2247696110\"\n" + "    },\n" + "    \"interstitial_ads\": {\n" + "        \"Facebook\": \"ca-app-pub-3940256099942544/1033173712\",\n" + "        \"Adx\": \"ca-app-pub-3940256099942544/1033173712\",\n" + "        \"Admob\": \"ca-app-pub-3940256099942544/1033173712\"\n" + "    },\n" + "    \"rewarded_ads\": {},\n" + "    \"mobile_sticky_ads\": {},\n" + "    \"collapsible_ads\": {},\n" + "    \"custom_ads_json\": {},\n" + "    \"extra_data_field\": {},\n" + "    \"ads_count\": 0,\n" + "    \"ads_open_type\": \"app open ads\",\n" + "    \"ads_type\": \"Main Ads\",\n" + "    \"version_name\": \"1.0\",\n" + "    \"monetize_platform\": \"Adx\"\n" + "}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(1);
//        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        splashActivity = SplashActivity.this;

        if (getConnectionStatus().isConnectingToInternet()) {
            callAdsApi(Constants.MAIN_BASE_URL, new AdsDataRequestModel(this.getPackageName(), ""), adsResponseModel -> {
                if (adsResponseModel != null) {
                    Constants.adsResponseModel = adsResponseModel;
                    Constants.hitCounter = adsResponseModel.getAds_count();
                    Constants.BACKPRESS_COUNT = adsResponseModel.getBackpress_count();
                    if (!isNull(Constants.adsResponseModel.getMonetize_platform()))
                        Constants.platformList = Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(","));
                    if (checkAppVersion(adsResponseModel.getVersion_name(), activity)) {
                        Global.showUpdateAppDialog(activity);
                    } else {
                        AdUtils.buildAppOpenAdCache(activity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                        AdUtils.buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), activity);
                        AdUtils.buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity);
                        AdUtils.buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), activity);
                        AdUtils.showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdx(), activity, state_load -> {
                            nextActivity();
                        });


                    }
                }

            });
        } else {
            nextActivity();
        }

    }


    private void nextActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            boolean isFirstRun = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE).getBoolean("isFirstRun", true);
            Intent intent;
            if (isFirstRun) {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, DashboardActivity.class);
            }
            startActivity(intent);
            finish();

            getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();

        }, 1500);
    }
}