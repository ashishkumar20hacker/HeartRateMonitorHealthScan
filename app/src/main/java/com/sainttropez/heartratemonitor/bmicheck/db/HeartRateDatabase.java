package com.sainttropez.heartratemonitor.bmicheck.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sainttropez.heartratemonitor.bmicheck.dao.HeartRateDao;
import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DbModel.class}, version = 1, exportSchema = false)
public abstract class HeartRateDatabase extends RoomDatabase {

    private static HeartRateDatabase instance;

    public abstract HeartRateDao dao();

    public static ExecutorService heartRateWrite = Executors.newFixedThreadPool(4);

    public static synchronized HeartRateDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, HeartRateDatabase.class, "BMI.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
