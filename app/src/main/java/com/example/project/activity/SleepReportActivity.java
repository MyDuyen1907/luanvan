package com.example.project.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.model.SleepData;
import com.example.project.adapter.SleepDataAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class SleepReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SleepDataAdapter adapter;
    private List<SleepData> sleepDataList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_report);

        recyclerView = findViewById(R.id.recyclerViewSleepReport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sleepDataList = new ArrayList<>();
        adapter = new SleepDataAdapter(sleepDataList);
        recyclerView.setAdapter(adapter);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Nhận dữ liệu từ Firestore
        fetchSleepData();
    }

    private void fetchSleepData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("SleepData")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sleepDataList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            SleepData sleepData = document.toObject(SleepData.class);
                            sleepDataList.add(sleepData);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SleepReportActivity.this, "Lấy dữ liệu thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
