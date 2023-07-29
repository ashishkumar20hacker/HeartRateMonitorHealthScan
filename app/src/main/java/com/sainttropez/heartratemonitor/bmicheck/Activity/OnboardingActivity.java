package com.sainttropez.heartratemonitor.bmicheck.Activity;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.AdsModule.AdUtils;
//import com.sainttropez.heartratemonitor.bmicheck.AdsUtils.FirebaseADHandlers.AppCompatActivity;
import com.adsmodule.api.AdsModule.Interfaces.AppInterfaces;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.R;

public class OnboardingActivity extends AppCompatActivity {


    ImageView imageView, nextbtn, dots;
    TextView tv, tv1;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(1);
//        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_onboarding);
        imageView = findViewById(R.id.onBoarding_iv);
        nextbtn = findViewById(R.id.nextbtn);
        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);
        dots = findViewById(R.id.dots);

        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),OnboardingActivity.this, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        if (i == 0) {
                            i = 1;
                            imageView.setImageResource(R.drawable.onboarding_two);
                            dots.setImageResource(R.drawable.dot_two);
                            tv.setText(R.string.ob_two_txt);
                            tv1.setText("BMI Calculator");
                        } else {
                            startActivity(new Intent(getApplicationContext(), TermsAndUse.class).putExtra("activity","ob"));

                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (i == 0) {
            exitDialog();
        } else {
            i = 0;
            imageView.setImageResource(R.drawable.onboarding_one);
            dots.setImageResource(R.drawable.dot_one);
            tv.setText(R.string.ob_one_txt);
            tv1.setText("Monitor your Heart rate");
        }
    }

    public void rateApp() {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


    private void exitDialog() {

        Dialog dialog = new Dialog(OnboardingActivity.this, R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(this);

        View lay = inflater.inflate(R.layout.exit_dialog, null);
        TextView goback, exit, rate_txt, do_you;
        ImageView rateUs;
        RelativeLayout exit_bg;
        goback = lay.findViewById(R.id.goback);
        exit = lay.findViewById(R.id.exit);
        rateUs = lay.findViewById(R.id.rate_us);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), lay.findViewById(R.id.native_ad).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        dialog.setContentView(lay);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finishAffinity();
            }
        });

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                rateApp();
            }
        });

        dialog.show();

    }

}