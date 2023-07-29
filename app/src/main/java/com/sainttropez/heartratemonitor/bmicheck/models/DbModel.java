package com.sainttropez.heartratemonitor.bmicheck.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HeartRateData")
public class DbModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int heartBeat;
    private String bodyState;
    private String date;
    private String time;

    public DbModel(int heartBeat, String bodyState, String date, String time) {
        this.heartBeat = heartBeat;
        this.bodyState = bodyState;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }

    public String getBodyState() {
        return bodyState;
    }

    public void setBodyState(String bodyState) {
        this.bodyState = bodyState;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
