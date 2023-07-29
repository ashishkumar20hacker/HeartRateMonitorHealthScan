package com.sainttropez.heartratemonitor.bmicheck.Fragments;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.adsmodule.api.AdsModule.Utils.Constants;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.adsmodule.api.AdsModule.AdUtils;
//import com.sainttropez.heartratemonitor.bmicheck.AdsUtils.FirebaseADHandlers.Fragment;
import com.sainttropez.heartratemonitor.bmicheck.R;
import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;
import com.sainttropez.heartratemonitor.bmicheck.viewModels.BMIViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrackerFragment extends Fragment {
    BMIViewModel viewModel;
    TextView heartbeat, date, time, state, report, min, max, avg, empty;
    ImageView color_heart;

    String label1, label2, label3, label4;
    float p1, p2, p3, p4;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tracker, container, false);
        viewModel = new ViewModelProvider(this).get(BMIViewModel.class);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ads).findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ad).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);

//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ad), 2, null);
//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ads), 1, null);
        heartbeat = root.findViewById(R.id.heart_beat);
        date = root.findViewById(R.id.date);
        time = root.findViewById(R.id.time);
        state = root.findViewById(R.id.state);
        report = root.findViewById(R.id.report);
        color_heart = root.findViewById(R.id.color_heart);
        max = root.findViewById(R.id.max);
        min = root.findViewById(R.id.min);
        avg = root.findViewById(R.id.avg);
        pieChart = root.findViewById(R.id.pieChart);
        empty = root.findViewById(R.id.empty_txt);
        viewModel.getAllData().observe(getViewLifecycleOwner(), new Observer<List<DbModel>>() {
            @Override
            public void onChanged(List<DbModel> dbModels) {

                String date1;
                String date2;
                String date3;
                String date4;
                float hbs1;
                float hbs2;
                float hbs3;
                float hbs4;
                if (dbModels.size() != 0) {
                    int hb = dbModels.get(0).getHeartBeat();
                    heartbeat.setText(String.valueOf(hb));
                    date.setText(dbModels.get(0).getDate());
                    time.setText(dbModels.get(0).getTime());
                    state.setText(dbModels.get(0).getBodyState());
                    if (dbModels.size() >= 4) {
                        empty.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);

                        int hb1 = dbModels.get(1).getHeartBeat();
                        int hb2 = dbModels.get(2).getHeartBeat();
                        int hb3 = dbModels.get(3).getHeartBeat();

                        if (hb >= 60 && hb <= 100) {
                            color_heart.setImageResource(R.drawable.green_heart);
                            report.setText("Normal");
                            report.setTextColor(Color.parseColor("#218A67"));
                        } else {
                            color_heart.setImageResource(R.drawable.red_heart);
                            if (hb < 60) {
                                report.setText("Low");
                                report.setTextColor(Color.parseColor("#1363D5"));
                                color_heart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1363D5")));
                            } else if (hb > 100) {
                                report.setText("High");
                                report.setTextColor(Color.parseColor("#F4281E"));
                            }
                        }

                        int total = hb + hb1 + hb2 + hb3;

                        double percentageHB = (hb * 100.0) / total;
                        double percentageHB1 = (hb1 * 100.0) / total;
                        double percentageHB2 = (hb2 * 100.0) / total;
                        double percentageHB3 = (hb3 * 100.0) / total;

                        p1 = Float.valueOf(String.valueOf(percentageHB));
                        p2 = Float.valueOf(String.valueOf(percentageHB1));
                        p3 = Float.valueOf(String.valueOf(percentageHB2));
                        p4 = Float.valueOf(String.valueOf(percentageHB3));
                        System.out.println(p1);
                        System.out.println(p2);
                        System.out.println(p3);
                        System.out.println(p4);

                        date1 = dbModels.get(0).getDate().substring(0, 5);
                        date2 = dbModels.get(1).getDate().substring(0, 5);
                        date3 = dbModels.get(2).getDate().substring(0, 5);
                        date4 = dbModels.get(3).getDate().substring(0, 5);
                        hbs1 = dbModels.get(0).getHeartBeat();
                        hbs2 = dbModels.get(1).getHeartBeat();
                        hbs3 = dbModels.get(2).getHeartBeat();
                        hbs4 = dbModels.get(3).getHeartBeat();

                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(hbs1, date1));
                        entries.add(new PieEntry(hbs2, date2));
                        entries.add(new PieEntry(hbs3, date3));
                        entries.add(new PieEntry(hbs4, date4));
                        setupPieChart(entries);
                    } else if (dbModels.size() == 3) {
                        empty.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);

                        int hb1 = dbModels.get(1).getHeartBeat();
                        int hb2 = dbModels.get(2).getHeartBeat();
//                        int hb3 = dbModels.get(3).getHeartBeat();

                        if (hb >= 60 && hb <= 100) {
                            color_heart.setImageResource(R.drawable.green_heart);
                            report.setText("Normal");
                            report.setTextColor(Color.parseColor("#218A67"));
                        } else {
                            color_heart.setImageResource(R.drawable.red_heart);
                            if (hb < 60) {
                                report.setText("Low");
                                report.setTextColor(Color.parseColor("#1363D5"));
                                color_heart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1363D5")));
                            } else if (hb > 100) {
                                report.setText("High");
                                report.setTextColor(Color.parseColor("#F4281E"));
                            }
                        }

                        int total = hb + hb1 + hb2;

                        double percentageHB = (hb * 100.0) / total;
                        double percentageHB1 = (hb1 * 100.0) / total;
                        double percentageHB2 = (hb2 * 100.0) / total;
//                        double percentageHB3 = (hb3 * 100.0) / total;

                        p1 = Float.valueOf(String.valueOf(percentageHB));
                        p2 = Float.valueOf(String.valueOf(percentageHB1));
                        p3 = Float.valueOf(String.valueOf(percentageHB2));
//                        p4 = Float.valueOf(String.valueOf(percentageHB3));
                        System.out.println(p1);
                        System.out.println(p2);
                        System.out.println(p3);
//                        System.out.println(p4);

                        date1 = dbModels.get(0).getDate().substring(0, 5);
                        date2 = dbModels.get(1).getDate().substring(0, 5);
                        date3 = dbModels.get(2).getDate().substring(0, 5);
//                        date4 = dbModels.get(3).getDate().substring(0, 5);
                        hbs1 = dbModels.get(0).getHeartBeat();
                        hbs2 = dbModels.get(1).getHeartBeat();
                        hbs3 = dbModels.get(2).getHeartBeat();
//                        hbs4 = dbModels.get(3).getHeartBeat();
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(hbs1, date1));
                        entries.add(new PieEntry(hbs2, date2));
                        entries.add(new PieEntry(hbs3, date3));
                        setupPieChart(entries);
                    } else if (dbModels.size() == 2) {
                        empty.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);

                        int hb1 = dbModels.get(1).getHeartBeat();
//                        int hb2 = dbModels.get(2).getHeartBeat();
//                        int hb3 = dbModels.get(3).getHeartBeat();

                        if (hb >= 60 && hb <= 100) {
                            color_heart.setImageResource(R.drawable.green_heart);
                            report.setText("Normal");
                            report.setTextColor(Color.parseColor("#218A67"));
                        } else {
                            color_heart.setImageResource(R.drawable.red_heart);
                            if (hb < 60) {
                                report.setText("Low");
                                report.setTextColor(Color.parseColor("#1363D5"));
                                color_heart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1363D5")));
                            } else if (hb > 100) {
                                report.setText("High");
                                report.setTextColor(Color.parseColor("#F4281E"));
                            }
                        }

                        int total = hb + hb1;

                        double percentageHB = (hb * 100.0) / total;
                        double percentageHB1 = (hb1 * 100.0) / total;
//                        double percentageHB2 = (hb2 * 100.0) / total;
//                        double percentageHB3 = (hb3 * 100.0) / total;

                        p1 = Float.valueOf(String.valueOf(percentageHB));
                        p2 = Float.valueOf(String.valueOf(percentageHB1));
//                        p3 = Float.valueOf(String.valueOf(percentageHB2));
//                        p4 = Float.valueOf(String.valueOf(percentageHB3));
                        System.out.println(p1);
                        System.out.println(p2);
//                        System.out.println(p3);
//                        System.out.println(p4);

                        date1 = dbModels.get(0).getDate().substring(0, 5);
                        date2 = dbModels.get(1).getDate().substring(0, 5);
//                        date3 = dbModels.get(2).getDate().substring(0, 5);
//                        date4 = dbModels.get(3).getDate().substring(0, 5);
                        hbs1 = dbModels.get(0).getHeartBeat();
                        hbs2 = dbModels.get(1).getHeartBeat();
//                        hbs3 = dbModels.get(2).getHeartBeat();
//                        hbs4 = dbModels.get(3).getHeartBeat();
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(hbs1, date1));
                        entries.add(new PieEntry(hbs2, date2));
//                        entries.add(new PieEntry(hbs3, date3));
                        setupPieChart(entries);
                    } else if (dbModels.size() == 1) {
                        empty.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);

//                        int hb1 = dbModels.get(1).getHeartBeat();
//                        int hb2 = dbModels.get(2).getHeartBeat();
//                        int hb3 = dbModels.get(3).getHeartBeat();

                        if (hb >= 60 && hb <= 100) {
                            color_heart.setImageResource(R.drawable.green_heart);
                            report.setText("Normal");
                            report.setTextColor(Color.parseColor("#218A67"));
                        } else {
                            color_heart.setImageResource(R.drawable.red_heart);
                            if (hb < 60) {
                                report.setText("Low");
                                report.setTextColor(Color.parseColor("#1363D5"));
                                color_heart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1363D5")));
                            } else if (hb > 100) {
                                report.setText("High");
                                report.setTextColor(Color.parseColor("#F4281E"));
                            }
                        }

                        int total = hb;

                        double percentageHB = (hb * 100.0) / total;
//                        double percentageHB1 = (hb1 * 100.0) / total;
//                        double percentageHB2 = (hb2 * 100.0) / total;
//                        double percentageHB3 = (hb3 * 100.0) / total;

                        p1 = Float.valueOf(String.valueOf(percentageHB));
//                        p2 = Float.valueOf(String.valueOf(percentageHB1));
//                        p3 = Float.valueOf(String.valueOf(percentageHB2));
//                        p4 = Float.valueOf(String.valueOf(percentageHB3));
                        System.out.println(p1);
//                        System.out.println(p2);
//                        System.out.println(p3);
//                        System.out.println(p4);

                        date1 = dbModels.get(0).getDate().substring(0, 5);
//                        date2 = dbModels.get(1).getDate().substring(0, 5);
//                        date3 = dbModels.get(2).getDate().substring(0, 5);
//                        date4 = dbModels.get(3).getDate().substring(0, 5);
                        hbs1 = dbModels.get(0).getHeartBeat();
//                        hbs2 = dbModels.get(1).getHeartBeat();
//                        hbs3 = dbModels.get(2).getHeartBeat();
//                        hbs4 = dbModels.get(3).getHeartBeat();
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(hbs1, date1));
//                        entries.add(new PieEntry(hbs2, date2));
//                        entries.add(new PieEntry(hbs3, date3));
                        setupPieChart(entries);
                    }
                } else {
                    date1 = "";
                    date2 = "";
                    date3 = "";
                    date4 = "";
                    hbs1 = 0;
                    hbs2 = 0;
                    hbs3 = 0;
                    hbs4 = 0;
                    empty.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                }

//                label1 = hbs1 + "\n" + date1;
//                label2 = hbs2 + "\n" + date2;
//                label3 = hbs3 + "\n" + date3;
//                label4 = hbs4 + "\n" + date4;


            }
        });

        viewModel.getMiniData().

                observe(getViewLifecycleOwner(), data ->

                {
                    if (data != null)
                        min.setText(String.valueOf(data));
                });

        viewModel.getMaxiData().

                observe(getViewLifecycleOwner(), data ->

                {
                    if (data != null)
                        max.setText(String.valueOf(data));
                });
        viewModel.getAvgData().

                observe(getViewLifecycleOwner(), data ->

                {
                    if (data != null)
                        avg.setText(String.valueOf(data));
                });


        return root;
    }

    private void setupPieChart(ArrayList<PieEntry> entries) {


        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#80D14755"));
        colors.add(Color.parseColor("#33D14755"));
        colors.add(Color.parseColor("#CCD14755"));
        colors.add(Color.parseColor("#D14755"));

        PieDataSet dataSet = new PieDataSet(entries, "Chart Title");
        dataSet.setSliceSpace(2f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Convert the float value to an integer index
                int index = (int) value;
                if (index >= 0) {
                    // Return the custom label for the corresponding index
                    return index + " bpm";
                }
                return "~ bpm"; // Return an empty string if index is out of range
            }
        });
        data.setValueTextSize(12f);
        data.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.setPadding(8, 8, 8, 8);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        pieChart.setEntryLabelColor(Color.parseColor("#4D000000"));
    }

}