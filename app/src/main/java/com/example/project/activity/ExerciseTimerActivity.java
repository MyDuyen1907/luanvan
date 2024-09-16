package com.example.project.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.project.adapter.ExerciseAdapter;
import com.example.project.model.Exercise;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExerciseTimerActivity extends AppCompatActivity {
    Button btnBackToHome;
    RecyclerView recyclerView;
    ExerciseAdapter adapter;
    ArrayList<Exercise> list = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_timer);

        findView();

//        createExercise("1", "Chạy bộ", 400);
//        createExercise("2", "Đạp xe", 700);
//        createExercise("3", "Bóng chuyền", 600);
//        createExercise("4", "Đi bộ", 300);
//        createExercise("5", "Bóng đá", 600);
//        createExercise("6", "Bóng rỗ", 800);
//        createExercise("7", "Nhảy dây", 900);

        loadExercises();


        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadExercises() {
        db.collection("exercise").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Exercise exercise = document.toObject(Exercise.class);
                            list.add(exercise);
                        }
                        adapter.notifyDataSetChanged(); // Cập nhật adapter khi dữ liệu đã được tải
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }


    private void createExercise(String id, String name, int caloriesPerHour) {
        // Tạo document với ID và các trường theo model
        Exercise exercise = new Exercise();
        exercise.setId(id);
        exercise.setName(name);
        exercise.setCaloriesPerHour(caloriesPerHour);
        exercise.setImg(""); // Đặt img là rỗng

        db.collection("exercise").document(id).set(exercise)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }


    private void findView() {
        btnBackToHome = findViewById(R.id.btnBackVanDong);
        recyclerView = findViewById(R.id.rcExercise);
        adapter = new ExerciseAdapter(this, list);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Sử dụng 'this' thay vì 'getApplicationContext()'
        recyclerView.setAdapter(adapter);
    }
}