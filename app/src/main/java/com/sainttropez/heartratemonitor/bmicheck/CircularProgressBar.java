package com.sainttropez.heartratemonitor.bmicheck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressBar extends View {
    private Paint progressPaint;
    private Paint backgroundPaint;
    private RectF arcRect;

    private int progress;
    private int progressMax;
    private int duration;
    private int updateInterval;
    private int progressIncrement;
    private int backgroundColor;

    public CircularProgressBar(Context context) {
        super(context);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*private void init() {
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.RED);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(10f);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundColor = Color.LTGRAY;

        arcRect = new RectF();

        progress = 0;
        progressMax = 100;
        duration = 10000;
        updateInterval = 1000;
        progressIncrement = progressMax / (duration / updateInterval);

        startProgress();
    }*/

    private void init() {
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.RED);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(12f);

        arcRect = new RectF();

        progress = 0;
        progressMax = 500;
        duration = 20000;
        updateInterval = 2000;
        progressIncrement = progressMax / (duration / updateInterval);

        // Set the background color of the progress path
        progressPaint.setColor(Color.WHITE);
        invalidate();

        startProgress();
    }


    public void startProgress() {
        new CountDownTimer(duration, updateInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress += progressIncrement;
                invalidate();
            }

            @Override
            public void onFinish() {
                progress = progressMax;
                invalidate();
            }
        }.start();
    }

   /* @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - (int) (progressPaint.getStrokeWidth() / 2f);

        arcRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float sweepAngle = (float) progress / progressMax * 360f;

        // Draw the background
        canvas.drawArc(arcRect, 0, 360, false, backgroundPaint);

        // Draw the progress
        canvas.drawArc(arcRect, -90f, sweepAngle, false, progressPaint);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - (int) (progressPaint.getStrokeWidth() / 2f);

        arcRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Set the background color of the progress path
        progressPaint.setColor(Color.GRAY);
        canvas.drawArc(arcRect, -90f, 360f, false, progressPaint);

        float sweepAngle = (float) progress / progressMax * 360f;
        progressPaint.setColor(Color.RED);
        canvas.drawArc(arcRect, -90f, sweepAngle, false, progressPaint);
    }


    public void setBackgroundColor(int color) {
       /* backgroundColor = color;
        backgroundPaint.setColor(backgroundColor);
        invalidate();*/

    }
}
