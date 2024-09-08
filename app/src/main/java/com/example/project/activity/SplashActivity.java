package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                if (currentUser != null) {
                    // User is signed in, check role
                    String userId = currentUser.getUid();
                    db.collection("account").document(userId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String role = document.getString("role");
                                            Intent intent;
                                            if ("admin".equals(role)) {
                                                intent = new Intent(SplashActivity.this, AdminActivity.class);
                                            } else {
                                                intent = new Intent(SplashActivity.this, MainActivity.class);
                                            }
                                            startActivity(intent);
                                        } else {
                                            Log.e(TAG, "Tài liệu không tồn tại");
                                            // Xử lý trường hợp tài liệu không tồn tại
                                            navigateToLogin();
                                        }
                                    } else {
                                        Log.e(TAG, "Lỗi tìm nạp tài liệu", task.getException());

                                        navigateToLogin();
                                    }
                                    finish();
                                }
                            });
                } else {
                    // Không có người dùng nào đăng nhập, điều hướng để đăng nhập
                    navigateToLogin();
                }
            }
        }, 3000);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
