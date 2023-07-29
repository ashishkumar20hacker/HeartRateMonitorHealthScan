package com.sainttropez.heartratemonitor.bmicheck.Fragments;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.Adapter.HistoryAdapter;
import com.adsmodule.api.AdsModule.AdUtils;
//import com.sainttropez.heartratemonitor.bmicheck.AdsUtils.FirebaseADHandlers.Fragment;
import com.sainttropez.heartratemonitor.bmicheck.R;
import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;
import com.sainttropez.heartratemonitor.bmicheck.viewModels.BMIViewModel;

import java.util.List;

public class HistoryFragment extends Fragment {

    BMIViewModel viewModel;

    HistoryAdapter adapter;
    RecyclerView historyRv;
    TextView empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        viewModel = new ViewModelProvider(this).get(BMIViewModel.class);
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), root.findViewById(R.id.native_ad).findViewById(com.adsmodule.api.R.id.native_ad_small), 2, null);//        AdUtils.showNativeAd(Constants.adsResponseModel.getNative_ads().getAdx(),requireActivity(), root.findViewById(R.id.native_ads), true);
        historyRv = root.findViewById(R.id.history_rv);
        empty = root.findViewById(R.id.empty_txt);

        historyRv.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
        viewModel.getAllData().observe(getViewLifecycleOwner(), new Observer<List<DbModel>>() {
            @Override
            public void onChanged(List<DbModel> dbModels) {

                if (dbModels.size() != 0) {
                    empty.setVisibility(View.GONE);
                    historyRv.setVisibility(View.VISIBLE);
                    adapter = new HistoryAdapter(requireActivity(),dbModels);
                    historyRv.setAdapter(adapter);
                } else {
                    empty.setVisibility(View.VISIBLE);
                    historyRv.setVisibility(View.GONE);
                }

            }
        });

        return root;
    }

}