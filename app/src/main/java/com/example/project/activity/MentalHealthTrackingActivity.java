package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private Button saveEmotionButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health_tracking); // Thay đổi với layout của bạn

        emotionInput = findViewById(R.id.emotion_input);
        saveEmotionButton = findViewById(R.id.save_emotion_button);

        // Khởi tạo Firestore
        firestore = FirebaseFirestore.getInstance();

        saveEmotionButton.setOnClickListener(v -> {
            String emotion = emotionInput.getText().toString().trim();
            if (!emotion.isEmpty()) {
                saveEmotion(emotion);
                emotionInput.setText(""); // Xóa ô nhập sau khi lưu
            } else {
                Toast.makeText(this, "Vui lòng nhập cảm xúc", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveEmotion(String emotion) {
        // Tạo một Document cho cảm xúc
        EmotionEntry emotionEntry = new EmotionEntry(emotion);

        // Lưu vào Firestore
        firestore.collection("emotions")
                .add(emotionEntry)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Cảm xúc đã được lưu", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lưu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Model cho cảm xúc
    public static class EmotionEntry {
        private String emotion;

        public EmotionEntry() {
            // Firebase Firestore cần một constructor không tham số
        }

        public EmotionEntry(String emotion) {
            this.emotion = emotion;
        }

        public String getEmotion() {
            return emotion;
        }

        public void setEmotion(String emotion) {
            this.emotion = emotion;
        }
    }
}