package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.model.HealthData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReportActivity extends AppCompatActivity {

    private TextView reportTextView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportTextView = findViewById(R.id.reportTextView);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        float systolic = Float.parseFloat(intent.getStringExtra("systolic"));
        float diastolic = Float.parseFloat(intent.getStringExtra("diastolic"));
        float bloodSugar = Float.parseFloat(intent.getStringExtra("bloodSugar"));
        float bloodSugarPP = Float.parseFloat(intent.getStringExtra("bloodSugarPP"));
        float cholesterol = Float.parseFloat(intent.getStringExtra("cholesterol"));

        // Hiển thị dữ liệu trong TextView
        String report = "Huyết áp: " + systolic + "/" + diastolic + "\n" +
                "Đường huyết lúc đói: " + bloodSugar + "\n" +
                "Đường huyết lúc no: " + bloodSugarPP + "\n" +
                "Cholesterol: " + cholesterol;
        reportTextView.setText(report);

        // Lấy User ID
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Lấy User ID

            // Tạo đối tượng HealthData
            HealthData healthData = new HealthData(systolic, diastolic, bloodSugar, bloodSugarPP, cholesterol, userId, "Bình thường");

            // Lưu đối tượng HealthData vào Firestore với Document ID tự động
            CollectionReference healthDataRef = db.collection("healthData");
            healthDataRef.add(healthData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(ReportActivity.this, "Dữ liệu đã được lưu thành công! ", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ReportActivity.this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Người dùng chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }
}
