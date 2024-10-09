package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.SleepHistoryAdapter;
import com.example.project.model.SleepData;
import com.example.project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SleeptrackingActivity extends AppCompatActivity {

    private TimePicker timePickerSleep;
    private TimePicker timePickerWakeUp;
    private TextView tvTotalSleepTime;
    private TextView tvCaloriesBurned;
    private TextView tvSleepQuality;
    private Button btnSave,btnBackSleeptracking;
    private RecyclerView rvSleepHistory;
    private List<SleepData> sleepDataList;
    private SleepHistoryAdapter sleepHistoryAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private User u; // Đối tượng User

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeptracking); // Đảm bảo tên layout đúng

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Gọi hàm để lấy thông tin người dùng
        getUserInfo();

        // Khởi tạo view
        timePickerSleep = findViewById(R.id.timePickerSleep);
        timePickerWakeUp = findViewById(R.id.timePickerWakeUp);
        tvTotalSleepTime = findViewById(R.id.tvTotalSleepTime);
        tvCaloriesBurned = findViewById(R.id.tvCaloriesBurned);
        tvSleepQuality = findViewById(R.id.tvSleepQuality);
        btnSave = findViewById(R.id.btnSave);
        rvSleepHistory = findViewById(R.id.rvSleepHistory);
        btnBackSleeptracking = findViewById(R.id.sleep);

        // Thiết lập RecyclerView
        sleepDataList = new ArrayList<>();
        sleepHistoryAdapter = new SleepHistoryAdapter(sleepDataList);
        rvSleepHistory.setLayoutManager(new LinearLayoutManager(this));
        rvSleepHistory.setAdapter(sleepHistoryAdapter);


        // Thêm sự kiện cho nút "Lưu"
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSleepData();
            }
        });
        btnBackSleeptracking.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getUserInfo() {
        String userId = currentUser.getUid(); // Lấy userId từ FirebaseUser

        db.collection("user").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        u = documentSnapshot.toObject(User.class); // Chuyển đổi dữ liệu thành đối tượng User
                        Log.d("User Info", "User data: " + u.toString()); // Log thông tin người dùng
                    } else {
                        Toast.makeText(SleeptrackingActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error getting user info", e); // Log lỗi
                    Toast.makeText(SleeptrackingActivity.this, "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                });
    }


    private void saveSleepData() {
        int sleepHour = timePickerSleep.getCurrentHour();
        int sleepMinute = timePickerSleep.getCurrentMinute();
        int wakeHour = timePickerWakeUp.getCurrentHour();
        int wakeMinute = timePickerWakeUp.getCurrentMinute();

        // Tính toán thời gian ngủ
        Calendar sleepTime = Calendar.getInstance();
        sleepTime.set(Calendar.HOUR_OF_DAY, sleepHour);
        sleepTime.set(Calendar.MINUTE, sleepMinute);

        Calendar wakeUpTime = Calendar.getInstance();
        wakeUpTime.set(Calendar.HOUR_OF_DAY, wakeHour);
        wakeUpTime.set(Calendar.MINUTE, wakeMinute);

        long sleepDurationMillis = wakeUpTime.getTimeInMillis() - sleepTime.getTimeInMillis();
        if (sleepDurationMillis < 0) {
            Toast.makeText(this, "Thời gian thức phải sau thời gian ngủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        long totalSleepMinutes = sleepDurationMillis / (1000 * 60);
        int hoursSlept = (int) totalSleepMinutes / 60;
        int minutesSlept = (int) totalSleepMinutes % 60;

        // Cập nhật UI
        tvTotalSleepTime.setText(String.format("Tổng thời gian ngủ: %dh %d phút", hoursSlept, minutesSlept));

        // Tính lượng calo tiêu thụ
        int weight = u != null ? u.getWeight() : 0; // Nếu u không null, lấy trọng lượng
        int caloriesBurned = (int) (weight * totalSleepMinutes * 0.0138889); // Giả định 0.0138889 kcal/kg/phút
        tvCaloriesBurned.setText(String.format("Calories tiêu thụ: %d kcal", caloriesBurned));

        // Đánh giá chất lượng giấc ngủ
        String sleepQuality = assessSleepQuality(hoursSlept);
        tvSleepQuality.setText("Chất lượng giấc ngủ: " + sleepQuality); // Cập nhật tvSleepQuality

        // Lưu dữ liệu vào Firestore
        String userId = currentUser.getUid(); // Lấy userId từ đối tượng FirebaseUser

        SleepData sleepData = new SleepData(userId, sleepHour, sleepMinute, wakeHour, wakeMinute, hoursSlept, minutesSlept, caloriesBurned, sleepQuality);

        db.collection("sleep_data")
                .document(userId) // Sử dụng userId làm Document ID
                .set(sleepData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SleeptrackingActivity.this, "Dữ liệu giấc ngủ đã được lưu", Toast.LENGTH_SHORT).show();
                    sleepDataList.add(sleepData);
                    sleepHistoryAdapter.notifyDataSetChanged(); // Cập nhật danh sách
                })
                .addOnFailureListener(e -> Toast.makeText(SleeptrackingActivity.this, "Lỗi khi lưu dữ liệu", Toast.LENGTH_SHORT).show());
    }


    private String assessSleepQuality(int hoursSlept) {
        if (hoursSlept >= 7) {
            return "Tốt";
        } else if (hoursSlept >= 5) {
            return "Trung bình";
        } else {
            return "Kém";
        }
    }

    private void showReport(String reportType) {
        switch (reportType) {
            case "Hàng ngày":
                // Hiển thị báo cáo hàng ngày
                Toast.makeText(this, "Hiển thị báo cáo hàng ngày", Toast.LENGTH_SHORT).show();
                break;
            case "Hàng tuần":
                // Hiển thị báo cáo hàng tuần
                Toast.makeText(this, "Hiển thị báo cáo hàng tuần", Toast.LENGTH_SHORT).show();
                break;
            case "Hàng tháng":
                // Hiển thị báo cáo hàng tháng
                Toast.makeText(this, "Hiển thị báo cáo hàng tháng", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
