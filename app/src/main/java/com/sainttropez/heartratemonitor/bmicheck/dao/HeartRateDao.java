package com.sainttropez.heartratemonitor.bmicheck.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;

import java.util.List;

@Dao
public interface HeartRateDao {

    @Insert(entity = DbModel.class, onConflict = OnConflictStrategy.REPLACE)
    void insert(DbModel model);

    @Delete
    void delete(DbModel model);

    @Update
    void update(DbModel model);

    @Query("SELECT * FROM HeartRateData GROUP BY id ORDER BY date DESC, time DESC")
    LiveData<List<DbModel>> getData();

    @Query("SELECT MIN(heartBeat) FROM HeartRateData")
    LiveData<Integer> getMinData();

    @Query("SELECT MAX(heartBeat) FROM HeartRateData")
    LiveData<Integer> getMaxData();

    @Query("SELECT AVG(heartBeat) FROM HeartRateData")
    LiveData<Integer> getAVGData();

}
