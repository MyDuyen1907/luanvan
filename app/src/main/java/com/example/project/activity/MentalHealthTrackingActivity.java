package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MentalHealthTrackingActivity extends AppCompatActivity {

    private EditText emotionInput;
    private Button saveEmotionButton, startAssessmentButton, startMeditationButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health_tracking);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Ghi nhận cảm xúc
        emotionInput = findViewById(R.id.emotion_input);
        saveEmotionButton = findViewById(R.id.save_emotion_button);
        saveEmotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmotion();
            }
        });

        // Đánh giá stress và lo âu
        startAssessmentButton = findViewById(R.id.start_assessment_button);
        startAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAssessment();
            }
        });

        // Hướng dẫn thiền
        startMeditationButton = findViewById(R.id.start_meditation_button);
        startMeditationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeditation();
            }
        });
    }

    // Hàm lưu cảm xúc vào Firestore
    private void saveEmotion() {
        String emotion = emotionInput.getText().toString().trim();

        if (!emotion.isEmpty()) {
            Map<String, Object> emotionData = new HashMap<>();
            emotionData.put("emotion", emotion);
            emotionData.put("timestamp", System.currentTimeMillis());

            // Lưu vào Firestore
            db.collection("emotions").add(emotionData)
                    .addOnSuccessListener(documentReference -> Toast.makeText(MentalHealthTrackingActivity.this, "Cảm xúc đã được lưu", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(MentalHealthTrackingActivity.this, "Lỗi khi lưu cảm xúc", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Hãy nhập cảm xúc của bạn", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm bắt đầu đánh giá stress và lo âu
    private void startAssessment() {
        // Chuyển sang một activity mới hoặc mở dialog đánh giá
        Toast.makeText(this, "Bắt đầu đánh giá stress và lo âu", Toast.LENGTH_SHORT).show();
    }

    // Hàm bắt đầu thiền
    private void startMeditation() {
        // Chuyển sang một activity mới hoặc mở hướng dẫn thiền
        Toast.makeText(this, "Bắt đầu thiền", Toast.LENGTH_SHORT).show();
    }
}