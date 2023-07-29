package com.sainttropez.heartratemonitor.bmicheck.Fragments;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.adsmodule.api.AdsModule.AdUtils;
//import com.sainttropez.heartratemonitor.bmicheck.AdsUtils.FirebaseADHandlers.Fragment;
import com.adsmodule.api.AdsModule.Interfaces.AppInterfaces;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.R;

public class BmiCalFragment extends Fragment {
    private static final String TAG = "BmiCalFragment";
    Spinner weight_spinner, height_spinner;
    ImageView female, male, gender_iv;
    String gender = "female";

    RelativeLayout nextbt, ll2;
    LinearLayout ll1;
    String weightUnit, heightUnit;

    EditText age_et, height_et, weight_et;
    TextView bmiValue, bmireport, gender_tv, age_tv, height_tv, weight_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_bmi_cal, container, false);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ad).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ad), 2, null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ads), 1, null);
        height_spinner = root.findViewById(R.id.height_spinner);
        weight_spinner = root.findViewById(R.id.weight_spinner);
        female = root.findViewById(R.id.female);
        male = root.findViewById(R.id.male);
        gender_iv = root.findViewById(R.id.gender_iv);
        nextbt = root.findViewById(R.id.next_btn);
        ll2 = root.findViewById(R.id.ll2);
        ll1 = root.findViewById(R.id.ll1);
        age_et = root.findViewById(R.id.age_et);
        height_et = root.findViewById(R.id.height_et);
        weight_et = root.findViewById(R.id.weight_et);
        bmireport = root.findViewById(R.id.bmi_report);
        bmiValue = root.findViewById(R.id.bmi);
        gender_tv = root.findViewById(R.id.gender_tv);
        age_tv = root.findViewById(R.id.age_tv);
        height_tv = root.findViewById(R.id.height_tv);
        weight_tv = root.findViewById(R.id.weight_tv);

        String weight[] = {getActivity().getResources().getString(R.string.unit_kilogram), getActivity().getResources().getString(R.string.unit_pounds)};
        String height[] = {getActivity().getResources().getString(R.string.unit_centimeters), getActivity().getResources().getString(R.string.unit_inches)};

        ArrayAdapter ad = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, height);
        ad.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        height_spinner.setAdapter(ad);

        ArrayAdapter ad2 = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, weight);
        ad2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        weight_spinner.setAdapter(ad2);

        height_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                heightUnit = height[i];
                if (heightUnit.equals("cm")) {
                    weight_spinner.setSelection(0);
                } else {
                    weight_spinner.setSelection(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                heightUnit = height[0];
            }
        });

        weight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weightUnit = weight[i];
                if (weightUnit.equals("kg")) {
                    height_spinner.setSelection(0);
                } else {
                    height_spinner.setSelection(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                weightUnit = weight[0];
            }
        });

        female.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
                gender = "Female";
                female.setImageResource(R.drawable.female_selected);
                male.setImageResource(R.drawable.male_unselected);
                gender_iv.setImageResource(R.drawable.ladki);
            });
        });
        male.setOnClickListener(view -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), isLoaded -> {
                gender = "Male";
                female.setImageResource(R.drawable.female_unselected);
                male.setImageResource(R.drawable.male_selected);
                gender_iv.setImageResource(R.drawable.ladka);
            });
        });

        nextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(),requireActivity(), new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        if (validateData()) {
                            double heights = Double.parseDouble(height_et.getText().toString());
                            double weights = Double.parseDouble(weight_et.getText().toString());
                            ll2.setVisibility(View.VISIBLE);
//                            AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ads2), 1,null);
                            AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ads2).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
                            ll1.setVisibility(View.GONE);
                            double bmivalue = calculateBMI(weights, weightUnit, heights, heightUnit);
                            bmiValue.setText(String.format("%.2f", bmivalue));
                            reprt(bmivalue);

                        }
                    }
                });
            }
        });

        return root;
    }

    private void reprt(double bmivalue) {
        if (bmivalue < 18.5) {
            bmireport.setText("Underweight");
            bmireport.setTextColor(Color.parseColor("#172BE1"));
            bmiValue.setTextColor(Color.parseColor("#172BE1"));
        } else if (bmivalue >= 18.5 && bmivalue <= 24.9) {
            bmireport.setText("Healthy");
            bmireport.setTextColor(Color.parseColor("#1ECD93"));
            bmiValue.setTextColor(Color.parseColor("#1ECD93"));
        } else if (bmivalue >= 25 && bmivalue <= 29.9) {
            bmireport.setText("Overweight");
            bmireport.setTextColor(Color.parseColor("#EBCC5D"));
            bmiValue.setTextColor(Color.parseColor("#EBCC5D"));
        } else if (bmivalue >= 30) {
            bmireport.setText("Obesity");
            bmireport.setTextColor(Color.parseColor("#F4281E"));
            bmiValue.setTextColor(Color.parseColor("#F4281E"));
        }
        age_tv.setText(age_et.getText().toString());
        gender_tv.setText(gender);
        height_tv.setText(height_et.getText().toString());
        weight_tv.setText(weight_et.getText().toString());
    }

    /*
        private Double calculateBMI(Double height, Double weight, String heightUnit, String weightUnit) {
            Double bmiHeightMetric, bmiWeightMetric, userBMI = 0.0;
            if (heightUnit.equals(getResources().getString(R.string.unit_centimeters))) {
                Log.i(TAG, "Height Unit in Centimeters: " + heightUnit);
                bmiHeightMetric = height;
            } else {
                Log.i(TAG, "Height Unit in Inches: " + heightUnit);
                bmiHeightMetric = height * 2.54;
            }
            if (weightUnit.equals(getResources().getString(R.string.unit_kilogram))) {
                Log.i(TAG, "Weight Unit in Kilogram: " + heightUnit);
                bmiWeightMetric = weight;
            } else {
                Log.i(TAG, "Weight Unit in Pounds: " + heightUnit);
                bmiWeightMetric = weight * 0.453592;
            }
            userBMI = Double.valueOf(bmiWeightMetric / (Math.pow((bmiHeightMetric / 100), 2)));
            Log.i(TAG, "final BMI HEIGHT: " + bmiHeightMetric);
            Log.i(TAG, "final BMI WEIGHT: " + bmiWeightMetric);
            Log.i(TAG, "final USER BMI: " + userBMI);
            return userBMI;
        }
    */
    private Boolean validateData() {
        Boolean isDataValid = true;
        //Check whether Height or Width is Empty of Not
        if (weight_et.getText().toString().equals("") || height_et.getText().toString().equals("") || age_et.getText().toString().equals("")) {
            isDataValid = false;
            if (weight_et.getText().toString().equals("")) {
                weight_et.setError("Please Enter your Weight");
                weight_et.requestFocus();
            }
            if (height_et.getText().toString().equals("")) {
                height_et.setError("Please Enter your Height");
                height_et.requestFocus();
            }
            if (age_et.getText().toString().equals("")) {
                age_et.setError("Please Enter your Age");
                age_et.requestFocus();
            }
        }

        //Check whether the Weight given is correct or not

        if (!weight_et.getText().toString().equals("")) {
            double userWeight = Double.valueOf(weight_et.getText().toString());
            if (weightUnit.equals(getActivity().getResources().getString(R.string.unit_kilogram))) {
                double weightKiloLimit = Double.valueOf(getActivity().getResources().getInteger(R.integer.kilogram_weight_limit));
                if ((userWeight > weightKiloLimit) || (userWeight <= 2)) {
                    isDataValid = false;
                    weight_et.setError("Invalid Weight Entered");
                    weight_et.requestFocus();
                }
            } else {
                double weightPoundLimit = Double.valueOf(getActivity().getResources().getInteger(R.integer.pounds_weight_limit));
                if ((userWeight > weightPoundLimit) || (userWeight <= 4.5)) {
                    isDataValid = false;
                    weight_et.setError("Invalid Weight Entered");
                    weight_et.requestFocus();
                }
            }
        }
        //Check whether the height given is Correct or Not
        if (!height_et.getText().toString().equals("")) {
            double userHeight = Double.valueOf(height_et.getText().toString());
            if (heightUnit.equals(getActivity().getResources().getString(R.string.unit_inches))) {
                double heightInchLimit = Double.valueOf(getActivity().getResources().getInteger(R.integer.inches_height_limit));
                if ((userHeight > heightInchLimit) || (userHeight <= 2)) {
                    isDataValid = false;
                    height_et.setError("Invalid Height Entered");
                    height_et.requestFocus();
                }
            } else {
                double heightCentiMeterLimit = Double.valueOf(getActivity().getResources().getInteger(R.integer.centimeter_height_limit));
                if ((userHeight > heightCentiMeterLimit) || (userHeight <= 54.5)) {
                    isDataValid = false;
                    height_et.setError("Invalid Height Entered");
                    height_et.requestFocus();
                }
            }
        }

/*        if (!weight_et.getText().toString().equals("")) {
            Float userWeight = Float.valueOf(weight_et.getText().toString().trim());
            if (weightUnit.equals(getActivity().getResources().getString(R.string.unit_kilogram))) {
                Float weightKiloLimit = Float.valueOf(getResources().getInteger(R.integer.kilogram_weight_limit));
                if ((userWeight > weightKiloLimit) || (userWeight <= 0)) {
                    isDataValid = false;
                    weight_et.setError("Invalid Weight Entered");
                    weight_et.requestFocus();
                }
            } else {
                Float weightPoundLimit = Float.valueOf(getActivity().getResources().getInteger(R.integer.pounds_weight_limit));
                if ((userWeight > weightPoundLimit) || (userWeight <= 0)) {
                    isDataValid = false;
                    weight_et.setError("Invalid Weight Entered");
                    weight_et.requestFocus();
                }
            }
        }
        //Check whether the height given is Correct or Not
        if (!height_et.getText().toString().equals("")) {
            Float userHeight = Float.valueOf(height_et.getText().toString().trim());
            if (heightUnit.equals(getResources().getString(R.string.unit_inches))) {
                Float heightInchLimit = Float.valueOf(getActivity().getResources().getInteger(R.integer.inches_height_limit));
                if ((userHeight > heightInchLimit) || (userHeight <= 0)) {
                    isDataValid = false;
                    height_et.setError("Invalid Height Entered");
                    height_et.requestFocus();
                }
            } else {
                Float heightCentiMeterLimit = Float.valueOf(getActivity().getResources().getInteger(R.integer.centimeter_height_limit));
                if ((userHeight > heightCentiMeterLimit) || (userHeight <= 0)) {
                    isDataValid = false;
                    height_et.setError("Invalid Height Entered");
                    height_et.requestFocus();
                }
            }*/
//        }
        return isDataValid;
    }

    public double calculateBMI(double weight, String weightUnit, double height, String heightUnit) {
        // Convert weight to kilograms
        if (weightUnit.equalsIgnoreCase("lb")) {
            weight *= 0.45359237;
        }

        // Convert height to meters
        if (heightUnit.equalsIgnoreCase("ft")) {
            height *= 30.48;
        } else if (heightUnit.equalsIgnoreCase("in")) {
            height *= 2.54;
        }

        // Calculate BMI using the formula: BMI = weight / (height * height)
        double bmi = weight / (height * height / 10000);

        return bmi;
    }


}