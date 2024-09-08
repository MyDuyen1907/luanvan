package com.example.project.activity.basicinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.model.User;

public class CurrenthealthActivity extends AppCompatActivity {
    Button btnTiepTuc;
    EditText edtHeartRate, edtBloodPressure, edtBloodSugar, edtCholesterol;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currenthealth);

        edtHeartRate = findViewById(R.id.edtHeartRate);
        edtBloodPressure = findViewById(R.id.edtBloodPressure);
        edtBloodSugar = findViewById(R.id.edtBloodSugar);
        edtCholesterol = findViewById(R.id.edtCholesterol);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        // Thiết lập sự kiện cho nút "Tiếp tục"
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra thông tin đầu vào
                if (edtHeartRate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CurrenthealthActivity.this, "Vui lòng thêm nhịp tim của bạn", Toast.LENGTH_SHORT).show();
                } else if (edtBloodPressure.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CurrenthealthActivity.this, "Vui lòng thêm huyết áp của bạn", Toast.LENGTH_SHORT).show();
                } else if (edtBloodSugar.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CurrenthealthActivity.this, "Vui lòng thêm chỉ số đường huyết của bạn", Toast.LENGTH_SHORT).show();
                } else if (edtCholesterol.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CurrenthealthActivity.this, "Vui lòng thêm mức cholesterol của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    user = (User) getIntent().getSerializableExtra("user");
                    assert user != null;
                    user.setHeart_rate(Integer.valueOf(edtHeartRate.getText().toString().trim()));
                    user.setBlood_pressure(Integer.valueOf(edtBloodPressure.getText().toString().trim()));
                    user.setBlood_sugar(Integer.valueOf(edtBloodSugar.getText().toString().trim()));
                    user.setCholesterol(Integer.valueOf(edtCholesterol.getText().toString().trim()));

                    System.out.println(user.getHeart_rate() + " " + user.getBlood_pressure() + " " + user.getBlood_sugar() + " " + user.getCholesterol());
                    Intent intent = new Intent(getApplicationContext(), FinishActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }
}
