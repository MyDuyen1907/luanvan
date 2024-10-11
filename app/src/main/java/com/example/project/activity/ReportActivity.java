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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        String systolicStr = intent.getStringExtra("systolic");
        String diastolicStr = intent.getStringExtra("diastolic");
        String bloodSugarStr = intent.getStringExtra("bloodSugar");
        String bloodSugarPPStr = intent.getStringExtra("bloodSugarPP");
        String cholesterolStr = intent.getStringExtra("cholesterol");

        // Lấy ngày và giờ hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date()); // Ngày giờ hiện tại

        // Kiểm tra xem có dữ liệu hay không
        if (systolicStr == null || diastolicStr == null || bloodSugarStr == null ||
                bloodSugarPPStr == null || cholesterolStr == null) {
            // Nếu không có dữ liệu, hiển thị thông báo "Chưa có thông tin"
            reportTextView.setText("Chưa có thông tin");
        } else {
            try {
                // Chuyển đổi dữ liệu từ String sang Float
                float systolic = Float.parseFloat(systolicStr);
                float diastolic = Float.parseFloat(diastolicStr);
                float bloodSugar = Float.parseFloat(bloodSugarStr);
                float bloodSugarPP = Float.parseFloat(bloodSugarPPStr);
                float cholesterol = Float.parseFloat(cholesterolStr);

                // Hiển thị dữ liệu trong TextView, bao gồm ngày giờ hiện tại
                String report = "Báo cáo ngày: " + currentDateTime + "\n\n" +
                        "Huyết áp: " + systolic + "/" + diastolic + "\n" +
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
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ nếu dữ liệu không thể chuyển đổi thành số
                reportTextView.setText("Chưa có thông tin");
            }
        }
    }
}
