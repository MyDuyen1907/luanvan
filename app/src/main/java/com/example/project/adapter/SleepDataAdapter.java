package com.example.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.SleepData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SleepDataAdapter extends RecyclerView.Adapter<SleepDataAdapter.ViewHolder> {

    private List<SleepData> sleepDataList;

    public SleepDataAdapter(List<SleepData> sleepDataList) {
        this.sleepDataList = sleepDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sleep_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SleepData sleepData = sleepDataList.get(position);
        holder.startSleepTime.setText("Thời gian ngủ: " + sleepData.getStartSleepTime());
        holder.wakeUpTime.setText("Thời gian thức: " + sleepData.getWakeUpTime());
        holder.sleepQuality.setText("Chất lượng giấc ngủ: " + sleepData.getSleepQuality());
        holder.awakenings.setText("Số lần thức trong đêm: " + sleepData.getAwakenings());
        holder.sleepCycles.setText("Chu kỳ giấc ngủ: " + sleepData.getSleepCycles());

        // Cập nhật LineChart cho từng SleepData
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, sleepData.getSleepDurationDeep()));
        entries.add(new Entry(1, sleepData.getSleepDurationLight()));
        entries.add(new Entry(2, sleepData.getSleepDurationRem()));

        LineDataSet dataSet = new LineDataSet(entries, "Phân tích giấc ngủ");
        LineData lineData = new LineData(dataSet);
        holder.sleepChart.setData(lineData);
        holder.sleepChart.invalidate(); // Làm mới biểu đồ
    }

    @Override
    public int getItemCount() {
        return sleepDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView startSleepTime, wakeUpTime, sleepQuality, awakenings, sleepCycles;
        LineChart sleepChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startSleepTime = itemView.findViewById(R.id.startSleepTime);
            wakeUpTime = itemView.findViewById(R.id.wakeUpTime);
            sleepQuality = itemView.findViewById(R.id.sleepQuality);
            awakenings = itemView.findViewById(R.id.awakenings);
            sleepCycles = itemView.findViewById(R.id.sleepCycles);
            sleepChart = itemView.findViewById(R.id.sleepChart);
        }
    }
}
