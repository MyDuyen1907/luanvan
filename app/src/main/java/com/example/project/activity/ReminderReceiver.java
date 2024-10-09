package com.example.project.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.project.R;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String medicineName = intent.getStringExtra("medicineName");
        String dosage = intent.getStringExtra("dosage");
        String time = intent.getStringExtra("time");

        // Tạo thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Kênh thông báo cho Android 8.0 trở lên
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("medication_reminder", "Nhắc nhở uống thuốc", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "medication_reminder")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Nhắc nhở uống thuốc")
                    .setContentText("Tên thuốc: " + medicineName + ", Liều lượng: " + dosage + ", Thời gian dùng trước đó: " + time)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());
        }
    }
}
