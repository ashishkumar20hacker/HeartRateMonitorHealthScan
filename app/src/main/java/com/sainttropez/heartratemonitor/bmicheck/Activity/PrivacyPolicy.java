package com.sainttropez.heartratemonitor.bmicheck.Activity;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Interfaces.AppInterfaces;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.R;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(1);
//        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_privacy_policy);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);    }

    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),PrivacyPolicy.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                PrivacyPolicy.super.onBackPressed();
            }
        });
    }
}