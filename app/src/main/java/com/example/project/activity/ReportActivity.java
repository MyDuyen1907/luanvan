package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class ReportActivity extends AppCompatActivity {

    private TextView reportTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportTextView = findViewById(R.id.reportTextView);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String systolic = intent.getStringExtra("systolic");
        String diastolic = intent.getStringExtra("diastolic");
        String bloodSugar = intent.getStringExtra("bloodSugar");
        String bloodSugarPP = intent.getStringExtra("bloodSugarPP");
        String cholesterol = intent.getStringExtra("cholesterol");


        // Hiển thị dữ liệu trong TextView
        String report = "Huyết áp: " + systolic + "/" + diastolic + "\n" +
                "Đường huyết lúc đói: " + bloodSugar + "\n" +
                "Đường huyết lúc no: " + bloodSugarPP + "\n" +
                "Cholesterol: " + cholesterol;



        reportTextView.setText(report);
    }
}
