package com.sainttropez.heartratemonitor.bmicheck.Activity;

import static android.os.Build.VERSION.SDK_INT;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Interfaces.AppInterfaces;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.BuildConfig;
import com.sainttropez.heartratemonitor.bmicheck.R;

public class DashboardActivity extends AppCompatActivity {

    ImageView appLogo, menu, close_drawer, heartRate, bmiCal, tracker, history;
    DrawerLayout drawerLayout;

    private final int REQUEST_CODE_STORAGE_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(1);
//        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_dashboard);
        appLogo = findViewById(R.id.app_logo);
        menu = findViewById(R.id.toggle_drawer);
        drawerLayout = findViewById(R.id.drawerLayout);
        close_drawer = findViewById(R.id.close_drawer);
        heartRate = findViewById(R.id.heartRate);
        bmiCal = findViewById(R.id.bmiCal);
        tracker = findViewById(R.id.tracker);
        history = findViewById(R.id.history);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), findViewById(R.id.native_ad).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),DashboardActivity.this, findViewById(R.id.native_ad), 2,null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),DashboardActivity.this, findViewById(R.id.native_ads), 1,null);

        mCheckPermission();


        menu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        close_drawer.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));

        history.setOnClickListener(view -> AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fragmentName", "history"));
            }
        }));

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fragmentName", "tracker"));
                    }
                });
            }
        });

        bmiCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fragmentName", "bmiCal"));
                    }
                });
            }
        });

        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fragmentName", "heartRate"));
                    }
                });
            }
        });
        
    }

    public boolean isLatestVersion() {
        return SDK_INT >= Build.VERSION_CODES.R;
    }

    private void mCheckPermission() {

        if (isLatestVersion()) {
            if (/*ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || */ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        } else {

            if (/*ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||*/ ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with reading and writing external storage
//                OpenImageChooser();
                System.out.println("Permission Already Granted");
            } else {
                mCheckPermission();
                // Permission denied, you cannot proceed with reading and writing external storage
//                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
//                DetailsDialog.showDetailsDialog(EditProfileActivity.this);
            }
        }
    }


    @Override
    public void onBackPressed() {
        exitDialog();
    }


    public void rateapp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }

    public void askPermissions() {

        if (isLatestVersion()) {
            if (/*ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || */ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{/*android.Manifest.permission.READ_EXTERNAL_STORAGE,*/ Manifest.permission.CAMERA}, REQUEST_CODE_STORAGE_PERMISSION);
//                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        } else {

            if (/*ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || */ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{/*android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, */Manifest.permission.CAMERA}, REQUEST_CODE_STORAGE_PERMISSION);
//                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        }
    }

    private void permissionDialog() {
        Dialog dialogOnBackPressed = new Dialog(DashboardActivity.this);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setCancelable(false);
        dialogOnBackPressed.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogOnBackPressed.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialogOnBackPressed.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogOnBackPressed.setContentView(R.layout.dialog_permission);
        ImageView textViewDeny, textViewAllow;
        textViewDeny = dialogOnBackPressed.findViewById(R.id.textViewDeny);
        textViewAllow = dialogOnBackPressed.findViewById(R.id.textViewAllow);

        textViewDeny.setOnClickListener(view -> {
            mCheckPermission();
            dialogOnBackPressed.dismiss();
        });

        textViewAllow.setOnClickListener(view -> {
            askPermissions();
            dialogOnBackPressed.dismiss();
        });

        dialogOnBackPressed.show();
    }

    public void shareApp(View view) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage = "\nUnlock a healthier, happier you with our app. Calculate and evaluate your BMI effortlessly. Download now and begin your journey to a better you.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public void rateApp(View view) {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    public void tAndU(View view) {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                startActivity(new Intent(getApplicationContext(), TermsConditions.class));

            }
        });
    }

    public void aboutUS(View view) {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));

            }
        });
    }

    public void history(View view) {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),DashboardActivity.this, new AppInterfaces.InterstitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fragmentName", "history"));
            }
        });
    }

    public void pp(View view) {
//        gotoUrl("https://egnitomedia.blogspot.com/p/privacy-policy_1.html");
        startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void exitDialog() {

        Dialog dialog = new Dialog(DashboardActivity.this, R.style.SheetDialog);
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

}