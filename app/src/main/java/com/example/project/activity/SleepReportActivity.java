package com.example.project.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.adapter.SleepDataAdapter;
import com.example.project.model.SleepData;
import java.util.ArrayList;
import java.util.List;

public class SleepReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SleepDataAdapter adapter;
    private List<SleepData> sleepDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_report);

        recyclerView = findViewById(R.id.recyclerViewSleepReport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sleepDataList = new ArrayList<>();
        adapter = new SleepDataAdapter(sleepDataList);
        recyclerView.setAdapter(adapter);

        // Nhận dữ liệu từ Intent
        receiveSleepData();
    }

    // Hàm lấy dữ liệu từ Intent
    private void receiveSleepData() {
        // Nhận dữ liệu từ Intent
        String startSleepTime = getIntent().getStringExtra("startSleepTime");
        String wakeUpTime = getIntent().getStringExtra("wakeUpTime");
        String sleepQuality = getIntent().getStringExtra("sleepQuality");
        int awakenings = getIntent().getIntExtra("awakenings", 0);
        String sleepCycles = getIntent().getStringExtra("sleepCycles");

        // Tạo đối tượng SleepData với các giá trị nhận được
        SleepData sleepData = new SleepData();
        sleepData.setStartSleepTime(startSleepTime);
        sleepData.setWakeUpTime(wakeUpTime);
        sleepData.setSleepQuality(sleepQuality);
        sleepData.setAwakenings(awakenings);
        sleepData.setSleepCycles(sleepCycles);

        // Thêm dữ liệu vào danh sách và cập nhật adapter
        sleepDataList.add(sleepData);
        adapter.notifyDataSetChanged();
    }
}
