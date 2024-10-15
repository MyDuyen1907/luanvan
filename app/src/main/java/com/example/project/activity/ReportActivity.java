package com.example.project.activity;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Truy vấn tất cả các bản ghi sức khỏe theo ngày từ Firestore
            CollectionReference healthDataRef = db.collection("healthData").document(userId).collection("dailyRecords");
            healthDataRef.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        StringBuilder report = new StringBuilder();

                        // Lấy giờ hiện tại
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            HealthData healthData = document.toObject(HealthData.class);
                            report.append("Ngày: ").append(healthData.getDate()).append(" - ").append(currentTime).append("\n") // Thêm giờ hiện tại
                                    .append("Huyết áp: ").append(healthData.getSystolic()).append("/").append(healthData.getDiastolic()).append("\n")
                                    .append("Đường huyết lúc đói: ").append(healthData.getBloodSugar()).append("\n")
                                    .append("Đường huyết lúc no: ").append(healthData.getBloodSugarPP()).append("\n")
                                    .append("Cholesterol: ").append(healthData.getCholesterol()).append("\n\n");
                        }
                        reportTextView.setText(report.toString());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ReportActivity.this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Người dùng chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }
}
