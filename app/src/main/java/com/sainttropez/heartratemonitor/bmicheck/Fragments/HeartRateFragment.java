package com.sainttropez.heartratemonitor.bmicheck.Fragments;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adsmodule.api.AdsModule.Utils.Constants;
import com.airbnb.lottie.LottieAnimationView;
import com.adsmodule.api.AdsModule.AdUtils;
//import com.sainttropez.heartratemonitor.bmicheck.AdsUtils.FirebaseADHandlers.Fragment;
import com.sainttropez.heartratemonitor.bmicheck.HelperClasses.ImageProcessing;
import com.sainttropez.heartratemonitor.bmicheck.R;
import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;
import com.sainttropez.heartratemonitor.bmicheck.viewModels.BMIViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeartRateFragment extends Fragment {

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static TextView res = null;
    private static Button start;
    private int progress = 0;
    private static LottieAnimationView startImg;
    private static int radiobtnNo = 1;
    private static RelativeLayout ll2, rest_rl, sit_rl, stand_rl, exercise_rl, meditation_rl, running_rl, cooking_rl, playing_rl, planting_rl, continuebtn_rl, selected_rl;
    private static ImageView rest_iv, sit_iv, stand_iv, exercise_iv, meditation_iv, running_iv, cooking_iv, playing_iv, planting_iv, selected_iv, color_heart, low, high, normal;
    private static LinearLayout ll1, ll3, radioll, nativead, nativeads, nativead2, nativead3, nativead4;
    private static TextView status, continuebtn_txt, selected_tv, report, measure;
    private static boolean isRadiobtnChecked = false;
    //    private static CircularProgressBar progressBar;
    private static TextView currentTime, currentDate;
    private static PowerManager.WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        GREEN, RED
    }

    private static TYPE currentType = TYPE.GREEN;

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    //    private FirebaseDatabase mdb ;
//    private static DatabaseReference heartrate_ref ;
    private SharedPreferences user;
    private static Context context;
    BMIViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ads_3).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ad).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);

//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ad), 2, null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ads), 1, null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ads_3), 1,null);

        viewModel = new ViewModelProvider(this).get(BMIViewModel.class);


        return root;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        preview = view.findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        currentDate = view.findViewById(R.id.date);
        currentTime = view.findViewById(R.id.time);
        res = view.findViewById(R.id.bmpRes);
        start = view.findViewById(R.id.startButton);
        startImg = view.findViewById(R.id.lottie_animation_view);
        status = view.findViewById(R.id.status);
        ll1 = view.findViewById(R.id.ll1);
        ll2 = view.findViewById(R.id.ll2);
        ll3 = view.findViewById(R.id.ll3);

        rest_rl = view.findViewById(R.id.resting_rl);
        rest_iv = view.findViewById(R.id.resting_iv);
        sit_rl = view.findViewById(R.id.sitting_rl);
        sit_iv = view.findViewById(R.id.sitting_iv);
        stand_rl = view.findViewById(R.id.standing_rl);
        stand_iv = view.findViewById(R.id.standing_iv);
        exercise_rl = view.findViewById(R.id.exercise_rl);
        exercise_iv = view.findViewById(R.id.exercise_iv);
        meditation_rl = view.findViewById(R.id.med_rl);
        meditation_iv = view.findViewById(R.id.med_iv);
        running_rl = view.findViewById(R.id.running_rl);
        running_iv = view.findViewById(R.id.running_iv);
        cooking_rl = view.findViewById(R.id.cooking_rl);
        cooking_iv = view.findViewById(R.id.cooking_iv);
        planting_rl = view.findViewById(R.id.planting_rl);
        planting_iv = view.findViewById(R.id.planting_iv);
        playing_rl = view.findViewById(R.id.playing_rl);
        playing_iv = view.findViewById(R.id.playing_iv);
        continuebtn_txt = view.findViewById(R.id.continue_btn_txt);
        continuebtn_rl = view.findViewById(R.id.continue_btn);
        selected_rl = view.findViewById(R.id.selected_rl);
        radioll = view.findViewById(R.id.radio_ll);
        selected_iv = view.findViewById(R.id.selected_iv);
        selected_tv = view.findViewById(R.id.selected_tv);
        color_heart = view.findViewById(R.id.color_heart);
        report = view.findViewById(R.id.report);
        nativead = view.findViewById(R.id.native_ad);
        nativeads = view.findViewById(R.id.native_ads);
        nativead2 = view.findViewById(R.id.native_ads_2);
        nativead3 = view.findViewById(R.id.native_ads_3);
        nativead4 = view.findViewById(R.id.native_ads4);
//        progressBar = view.findViewById(R.id.progressBar);
        measure = view.findViewById(R.id.measure);
        low = view.findViewById(R.id.low);
        normal = view.findViewById(R.id.normal);
        high = view.findViewById(R.id.high);

        llThreeElements();

        context = getContext();
        user = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String address = user.getString("address", "");
//        mdb = FirebaseDatabase.getInstance();
//        heartrate_ref = mdb.getReference(address).child("heartratestats");

        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotDimScreen");


        startImg.setOnClickListener(view1 -> {
//            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
//                AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), nativead2, 1,null);
            AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), nativead2.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
//            startprogress();
//            progressBar.startProgress();
                start.performClick();
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(250); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                measure.startAnimation(anim);
//            });
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getText().toString().equalsIgnoreCase("START")) {
                    start.setText("STOP");
                    status.setText("Processing Heart Rate ...");
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    beatsIndex = 0;
                    averageIndex = 0;
                    averageArray = new int[averageArraySize];
                    beatsArray = new int[beatsArraySize];
                    try {
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                    } catch (Exception e) {
//                        throw new RuntimeException(e);
                        System.out.println("Exception>>>>>" + e);
                    }
                } else {
                    start.setText("START");
                    status.setText("Monitor Heart Rate");
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                }
            }
        });

    }

    private void startprogress() {
        /*int progressMax = 100;
        int duration = 10000; // 10 seconds in milliseconds
        int updateInterval = 1000; // 1 second in milliseconds
        int progressIncrement = progressMax / (duration / updateInterval);

        CountDownTimer countDownTimer = new CountDownTimer(duration, updateInterval) {
            int currentProgress = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                currentProgress += progressIncrement;
                progressBar.setProgress(currentProgress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(progressMax);
            }
        };

        countDownTimer.start(); // Start the countdown timer*/

// Set the maximum progress value
//        progressBar.setProgressMax(100);
//
//// Set the duration in milliseconds
//        progressBar.setDuration(10000);
//
//// Set the progress value (if desired)
//        progressBar.setProgress(50);
    }
/*
    private void startprogress() {
        int progressMax = 100;
        int duration = 10000; // 10 seconds in milliseconds
        int updateInterval = 1000; // 1 second in milliseconds
        int progressIncrement = progressMax / (duration / updateInterval);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentProgress = 0;

            @Override
            public void run() {
                currentProgress += progressIncrement;
                progressBar.setProgress(currentProgress);

                if (currentProgress < progressMax) {
                    handler.postDelayed(this, updateInterval);
                }
            }
        };

        handler.postDelayed(runnable, updateInterval); // Start the progress update
    }
*/


    private void llThreeElements() {

        rest_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 1;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_selected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        sit_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 2;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_selected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });

        stand_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 3;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_selected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        exercise_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 4;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_selected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        meditation_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 5;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_selected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        running_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 6;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_selected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        cooking_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 7;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_selected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        playing_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 8;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_selected);
            planting_iv.setImageResource(R.drawable.radio_unselected);
        });
        });
        planting_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
            radiobtnNo = 7;
            isRadiobtnChecked = true;
            rest_iv.setImageResource(R.drawable.radio_unselected);
            sit_iv.setImageResource(R.drawable.radio_unselected);
            stand_iv.setImageResource(R.drawable.radio_unselected);
            exercise_iv.setImageResource(R.drawable.radio_unselected);
            meditation_iv.setImageResource(R.drawable.radio_unselected);
            running_iv.setImageResource(R.drawable.radio_unselected);
            cooking_iv.setImageResource(R.drawable.radio_unselected);
            playing_iv.setImageResource(R.drawable.radio_unselected);
            planting_iv.setImageResource(R.drawable.radio_selected);
        });
        });

        continuebtn_rl.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
//            if (isRadiobtnChecked) {
            if (continuebtn_txt.getText().toString().equals("Continue")) {
                radioll.setVisibility(View.GONE);
                selected_rl.setVisibility(View.VISIBLE);
                switch (radiobtnNo) {
                    case 1:
                        selected_iv.setImageResource(R.drawable.resting);
                        selected_tv.setText("Resting");
                        break;
                    case 2:
                        selected_iv.setImageResource(R.drawable.sitting);
                        selected_tv.setText("Sitting");
                        break;
                    case 3:
                        selected_iv.setImageResource(R.drawable.standing);
                        selected_tv.setText("Standing");
                        break;
                    case 4:
                        selected_iv.setImageResource(R.drawable.excercise);
                        selected_tv.setText("Exercise");
                        break;
                    case 5:
                        selected_iv.setImageResource(R.drawable.medi);
                        selected_tv.setText("Meditation");
                        break;
                    case 6:
                        selected_iv.setImageResource(R.drawable.running);
                        selected_tv.setText("Running");
                        break;
                    case 7:
                        selected_iv.setImageResource(R.drawable.cooking);
                        selected_tv.setText("Cooking");
                        break;
                    case 8:
                        selected_iv.setImageResource(R.drawable.playing);
                        selected_tv.setText("Playing");
                        break;
                    case 9:
                        selected_iv.setImageResource(R.drawable.planting);
                        selected_tv.setText("Planting");
                        break;
                    default:
                        break;
                }
                continuebtn_txt.setText("Done");
                nativead4.setVisibility(View.VISIBLE);
//                AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), nativead4, 1,null);
                AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), nativead4.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
                viewModel.addToDB(new DbModel(Integer.parseInt(res.getText().toString()), selected_tv.getText().toString(), currentDate.getText().toString(), currentTime.getText().toString()));
//                    insertIntoDB(Integer.parseInt(res.getText().toString()),selected_tv.getText().toString(),currentDate.getText().toString(),currentTime.getText().toString());
            } else {
                nativead4.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
                continuebtn_txt.setText("Continue");
                radioll.setVisibility(View.GONE);
                selected_rl.setVisibility(View.GONE);
                isRadiobtnChecked = false;
                rest_iv.setImageResource(R.drawable.radio_unselected);
                sit_iv.setImageResource(R.drawable.radio_unselected);
                stand_iv.setImageResource(R.drawable.radio_unselected);
                exercise_iv.setImageResource(R.drawable.radio_unselected);
                meditation_iv.setImageResource(R.drawable.radio_unselected);
                running_iv.setImageResource(R.drawable.radio_unselected);
                cooking_iv.setImageResource(R.drawable.radio_unselected);
                playing_iv.setImageResource(R.drawable.radio_unselected);
                planting_iv.setImageResource(R.drawable.radio_unselected);
            }
//            } else {
//                Toast.makeText(context, "Please select your current state.", Toast.LENGTH_SHORT).show();
//            }
        });
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();


        wakeLock.acquire();
        camera = Camera.open();

    }


    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static void logMonitoredRate(int rate) {

        final String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        final String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

       /* try {
            heartrate_ref.child(date).child(time).setValue(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Monitored Rate Logged ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Please Check your Internet Connection  , Couldn't log Rate ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){

        }*/
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    Log.d(TAG, "BEAT!! beats=" + beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                Log.d(TAG,
                        "totalTimeInSecs=" + totalTimeInSecs + " beats=" + beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = ((beatsArrayAvg) / beatsArrayCnt);
                Log.d("bpm", "" + beatsArrayAvg / beatsArrayCnt + "   : " + beatsAvg);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                currentDate.setText(dateFormat.format(date));
                currentTime.setText(dateFormat2.format(date));
                ll2.setVisibility(View.INVISIBLE);
                ll3.setVisibility(View.VISIBLE);
                radioll.setVisibility(View.VISIBLE);

                res.setText("" + beatsAvg);
                if (beatsAvg >= 60 && beatsAvg <= 100) {
                    color_heart.setImageResource(R.drawable.green_heart);
                    report.setText("Normal");
                    report.setTextColor(Color.parseColor("#218A67"));
                    low.setVisibility(View.INVISIBLE);
                    normal.setVisibility(View.VISIBLE);
                    high.setVisibility(View.INVISIBLE);
                } else {
                    color_heart.setImageResource(R.drawable.red_heart);
                    if (beatsAvg < 60) {
                        report.setText("Low");
                        report.setTextColor(Color.parseColor("#1363D5"));
                        color_heart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1363D5")));
                        low.setVisibility(View.VISIBLE);
                        normal.setVisibility(View.INVISIBLE);
                        high.setVisibility(View.INVISIBLE);
                    } else if (beatsAvg > 100) {
                        report.setText("High");
                        report.setTextColor(Color.parseColor("#F4281E"));
                        low.setVisibility(View.INVISIBLE);
                        normal.setVisibility(View.INVISIBLE);
                        high.setVisibility(View.VISIBLE);
                    }
                }
                logMonitoredRate(beatsAvg);
                start.setText("START");
                status.setText("Monitor Heart Rate");
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }
}