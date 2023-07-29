package com.sainttropez.heartratemonitor.bmicheck.Activity;


import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Interfaces.AppInterfaces;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.R;

public class TermsAndUse extends AppCompatActivity {

    TextView agreebtn;
    String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(1);
//        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_terms_and_use);
        agreebtn = findViewById(R.id.agreebtn);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);        activityName = getIntent().getStringExtra("activity");
        if (activityName.equals("db")) {
            agreebtn.setVisibility(View.GONE);
        } else {
            agreebtn.setVisibility(View.VISIBLE);

        }

        agreebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), TermsAndUse.this, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    }
                });
            }
        });

    }


    public void tmc(View view) {
        startActivity(new Intent(getApplicationContext(), TermsConditions.class));
    }

    public void pp(View view) {
        startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
    }


    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), TermsAndUse.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                TermsAndUse.super.onBackPressed();
            }
        });
    }
}