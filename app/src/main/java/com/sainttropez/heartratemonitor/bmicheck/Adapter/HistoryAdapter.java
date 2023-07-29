package com.sainttropez.heartratemonitor.bmicheck.Adapter;

import static com.sainttropez.heartratemonitor.bmicheck.SingletonClasses.AppOpenAds.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.sainttropez.heartratemonitor.bmicheck.R;
import com.sainttropez.heartratemonitor.bmicheck.models.DbModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    List<DbModel> dbModels;

    public HistoryAdapter(Context context, List<DbModel> dbModels) {
        this.context = context;
        this.dbModels = dbModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int hb = dbModels.get(position).getHeartBeat();
        holder.heartbeat.setText(String.valueOf(hb));
        holder.date.setText(dbModels.get(position).getDate());
        holder.time.setText(dbModels.get(position).getTime());
        holder.state.setText(dbModels.get(position).getBodyState());

        String val = String.valueOf(holder.getAdapterPosition());
        if (val.endsWith("4") || val.endsWith("9")) {
            Log.e("onBindViewHolder: ", position + "   " + val + "   "+val.endsWith("4")+ "  "+val.endsWith("9"));
            holder.nativeads.setVisibility(View.VISIBLE);
            AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), holder.nativeads.findViewById(com.adsmodule.api.R.id.native_ad1), 1, null);        } else {
            holder.nativeads.setVisibility(View.GONE);
        }

        if (dbModels.get(position).getHeartBeat() >= 60 && dbModels.get(position).getHeartBeat() <= 100) {
            holder.color_heart.setImageResource(R.drawable.green_heart);
            holder.report.setText("Normal");
            holder.report.setTextColor(Color.parseColor("#218A67"));
        } else {
            holder.color_heart.setImageResource(R.drawable.red_heart);
            if (dbModels.get(position).getHeartBeat() < 60) {
                holder.report.setText("Low");
                holder.report.setTextColor(Color.parseColor("#1363D5"));
                holder.color_heart.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1363D5")));            } else if (dbModels.get(position).getHeartBeat() > 100) {
                holder.report.setText("High");
                holder.report.setTextColor(Color.parseColor("#F4281E"));
            }
        }

    }

    @Override
    public int getItemCount() {
        return dbModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView heartbeat, date, time, state, report;
        ImageView color_heart;
        LinearLayout nativeads;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heartbeat = itemView.findViewById(R.id.heart_beat);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            state = itemView.findViewById(R.id.state);
            report = itemView.findViewById(R.id.report);
            color_heart = itemView.findViewById(R.id.color_heart);
            nativeads = itemView.findViewById(R.id.native_ads);

        }
    }
}
