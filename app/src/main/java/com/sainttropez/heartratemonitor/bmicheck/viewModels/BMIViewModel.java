package com.sainttropez.heartratemonitor.bmicheck.viewModels;

import static com.sainttropez.heartratemonitor.bmicheck.db.HeartRateDatabase.heartRateWrite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sainttropez.heartratemonitor.bmicheck.dao.HeartRateDao;
import com.sainttropez.heartratemonitor.bmicheck.db.HeartRateDatabase;
import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;

import java.util.List;

public class BMIViewModel extends AndroidViewModel {

    HeartRateDao heartRateDao;

    public BMIViewModel(@NonNull Application application) {
        super(application);
        heartRateDao = HeartRateDatabase.getInstance(application.getApplicationContext()).dao();
    }

    public void addToDB(DbModel model) {
        heartRateWrite.execute(new Runnable() {
            @Override
            public void run() {
                heartRateDao.insert(model);
            }
        });
    }

    public LiveData<List<DbModel>> getAllData() {
        return heartRateDao.getData();
    }

    public LiveData<Integer> getMiniData() {
        return heartRateDao.getMinData();
    }

    public LiveData<Integer> getMaxiData() {
        return heartRateDao.getMaxData();
    }

    public LiveData<Integer> getAvgData() {
        return heartRateDao.getAVGData();
    }

}
