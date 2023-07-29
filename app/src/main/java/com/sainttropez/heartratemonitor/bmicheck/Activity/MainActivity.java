package com.sainttropez.heartratemonitor.bmicheck.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.adsmodule.api.AdsModule.Utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.adsmodule.api.AdsModule.AdUtils;
//import com.sainttropez.heartratemonitor.bmicheck.AdsUtils.FirebaseADHandlers.AppCompatActivity;
import com.adsmodule.api.AdsModule.Interfaces.AppInterfaces;
import com.sainttropez.heartratemonitor.bmicheck.Fragments.BmiCalFragment;
import com.sainttropez.heartratemonitor.bmicheck.Fragments.HeartRateFragment;
import com.sainttropez.heartratemonitor.bmicheck.Fragments.HistoryFragment;
import com.sainttropez.heartratemonitor.bmicheck.Fragments.TrackerFragment;
import com.sainttropez.heartratemonitor.bmicheck.R;

public class MainActivity extends AppCompatActivity {

    String fragmentName;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(1);
//        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);
        fragmentName = getIntent().getStringExtra("fragmentName");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setItemIconTintList(null);

        textView = findViewById(R.id.title);
        ImageView back = findViewById(R.id.backbt);
        back.setOnClickListener(view -> onBackPressed());

        switch (fragmentName) {
            case "heartRate":
                textView.setText("Heart Rate ");
                bottomNav.setSelectedItemId(R.id.nav_heart_rate);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HeartRateFragment()).commit();
                break;
            case "bmiCal":
                textView.setText("BMI Calculator");
                bottomNav.setSelectedItemId(R.id.nav_bmi_cal);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BmiCalFragment()).commit();
                break;
            case "tracker":
                textView.setText("Health Tracker");
                bottomNav.setSelectedItemId(R.id.nav_health);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrackerFragment()).commit();
                break;
            case "history":
                textView.setText("History");
                bottomNav.setSelectedItemId(R.id.nav_history);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
                break;

        }

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.nav_heart_rate) {
            textView.setText("Heart Rate ");
            selectedFragment = new HeartRateFragment();
        } else if (itemId == R.id.nav_bmi_cal) {
            textView.setText("BMI Calculator");
            selectedFragment = new BmiCalFragment();
        } else if (itemId == R.id.nav_health) {
            textView.setText("Health Tracker");
            selectedFragment = new TrackerFragment();
        }else if (itemId == R.id.nav_history) {
            textView.setText("History");
            selectedFragment = new HistoryFragment();
        }
        // It will help to replace the
        // one fragment to other.
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };


    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),MainActivity.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                MainActivity.super.onBackPressed();
            }
        });
    }


}