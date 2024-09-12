package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.R;
import com.example.project.model.SleepData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SleepTrackerActivity extends AppCompatActivity {

    EditText startSleepTime, wakeUpTime, awakenings, sleepCycles;
    Spinner sleepQuality;
    Button btnSaveSleepData, btnSleepData;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracker);

        // Khởi tạo các thành phần giao diện
        startSleepTime = findViewById(R.id.startSleepTime);
        wakeUpTime = findViewById(R.id.wakeUpTime);
        sleepQuality = findViewById(R.id.sleepQuality);
        awakenings = findViewById(R.id.awakenings);
        sleepCycles = findViewById(R.id.sleepCycles);
        btnSaveSleepData = findViewById(R.id.btnSaveSleepData);
        btnSleepData = findViewById(R.id.btnSleepData);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Xử lý sự kiện lưu dữ liệu giấc ngủ
        btnSaveSleepData.setOnClickListener(v -> saveSleepData());

        // Xử lý sự kiện xem dữ liệu giấc ngủ
        btnSleepData.setOnClickListener(v -> viewSleepData());
    }

    // Hàm lưu dữ liệu giấc ngủ
    private void saveSleepData() {
        String start = startSleepTime.getText().toString().trim();
        String wakeUp = wakeUpTime.getText().toString().trim();
        String quality = sleepQuality.getSelectedItem().toString();
        String awakeningsStr = awakenings.getText().toString().trim();
        String cycles = sleepCycles.getText().toString().trim();

        // Kiểm tra các trường bắt buộc
        if (start.isEmpty() || wakeUp.isEmpty() || awakeningsStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số lần thức có hợp lệ không
        int numAwakenings;
        try {
            numAwakenings = Integer.parseInt(awakeningsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lần thức phải là một số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ID người dùng hiện tại
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUserId = auth.getCurrentUser().getUid();

        // Tạo đối tượng SleepData với dữ liệu người dùng nhập
        SleepData sleepData = new SleepData(start, wakeUp, quality, numAwakenings, cycles, 0, 0, 0, currentUserId);

        // Lưu dữ liệu vào Firestore
        db.collection("SleepData").add(sleepData)
                .addOnSuccessListener(documentReference -> Toast.makeText(SleepTrackerActivity.this, "Đã lưu dữ liệu", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(SleepTrackerActivity.this, "Lưu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Hàm lấy dữ liệu giấc ngủ của người dùng hiện tại và chuyển sang SleepReportActivity
    private void viewSleepData() {
        Intent intent = new Intent(SleepTrackerActivity.this, SleepReportActivity.class);
        startActivity(intent);
    }
}
