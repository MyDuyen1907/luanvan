package com.example.project.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.model.MealAlarm;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MealAlarmActivity extends AppCompatActivity {

    private TextView tvBreakfastAlarm, tvLunchAlarm, tvDinnerAlarm;
    private Button btnSetBreakfastAlarm, btnSetLunchAlarm, btnSetDinnerAlarm;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_alarm); // Đảm bảo bạn đã tạo activity_meal_alarm.xml

        db = FirebaseFirestore.getInstance();
        tvBreakfastAlarm = findViewById(R.id.tv_breakfast_alarm);
        tvLunchAlarm = findViewById(R.id.tv_lunch_alarm);
        tvDinnerAlarm = findViewById(R.id.tv_dinner_alarm);
        btnSetBreakfastAlarm = findViewById(R.id.btn_set_breakfast_alarm);
        btnSetLunchAlarm = findViewById(R.id.btn_set_lunch_alarm);
        btnSetDinnerAlarm = findViewById(R.id.btn_set_dinner_alarm);

        // Thiết lập sự kiện click cho nút Đặt báo thức
        btnSetBreakfastAlarm.setOnClickListener(v -> showTimePickerDialog("Bữa sáng"));
        btnSetLunchAlarm.setOnClickListener(v -> showTimePickerDialog("Bữa trưa"));
        btnSetDinnerAlarm.setOnClickListener(v -> showTimePickerDialog("Bữa tối"));
    }

    private void showTimePickerDialog(String mealType) {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Hiển thị TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            saveMealAlarm(mealType, time);
        }, hour, minute, true);

        timePickerDialog.setTitle("Chọn thời gian cho " + mealType);
        timePickerDialog.show();
    }

    private void saveMealAlarm(String mealType, String time) {
        MealAlarm mealAlarm = new MealAlarm(mealType, time);

        // Lưu vào Firestore
        db.collection("meal_alarms")
                .add(mealAlarm)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MealAlarmActivity.this, "Đã lưu báo thức cho " + mealType, Toast.LENGTH_SHORT).show();
                    // Cập nhật TextView tương ứng
                    updateTextView(mealType, time);

                    // Đặt báo thức
                    setAlarm(mealType, time);
                })
                .addOnFailureListener(e -> {
                    Log.e("MealAlarmActivity", "Error saving meal alarm: ", e);
                    Toast.makeText(MealAlarmActivity.this, "Lưu không thành công: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setAlarm(String mealType, String time) {
        // Tách giờ và phút từ chuỗi thời gian
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Tạo đối tượng Calendar để đặt báo thức
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Tạo Intent để gửi đến AlarmReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("mealType", mealType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Đặt báo thức
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Báo thức đã được đặt cho " + mealType + " lúc " + time, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTextView(String mealType, String time) {
        switch (mealType) {
            case "Bữa sáng":
                tvBreakfastAlarm.setText("Báo thức: " + time);
                break;
            case "Bữa trưa":
                tvLunchAlarm.setText("Báo thức: " + time);
                break;
            case "Bữa tối":
                tvDinnerAlarm.setText("Báo thức: " + time);
                break;
        }
    }
}
