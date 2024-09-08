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


public class AgeHeightWeightActivity extends AppCompatActivity {
    Button btnTiepTuc;
    EditText edtAge, edtHeight, edtWeight;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_height_weight);

        edtWeight = findViewById(R.id.edtWeight);
        edtHeight = findViewById(R.id.edtHeight);
        edtAge = findViewById(R.id.edtAge);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        user = new User();

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem thông tin đã được điền chưa
                if(edtAge.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AgeHeightWeightActivity.this, "Vui lòng thêm tuổi của bạn", Toast.LENGTH_SHORT).show();
                }
                else if (edtHeight.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AgeHeightWeightActivity.this, "Vui lòng thêm chiều cao của bạn", Toast.LENGTH_SHORT).show();
                }
                else if (edtWeight.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AgeHeightWeightActivity.this, "Vui lòng thêm cân nặng của bạn", Toast.LENGTH_SHORT).show();
                }
                // chuyển đến activity tiếp theo
                else {
                    user = (User) getIntent().getSerializableExtra("user");
                    user.setAge(Integer.valueOf(edtAge.getText().toString().trim()));
                    user.setHeight(Integer.valueOf(edtHeight.getText().toString().trim()));
                    user.setWeight(Integer.valueOf(edtWeight.getText().toString().trim()));

                    System.out.println(user.getAge() + " " + user.getHeight() + " " + user.getGender());
                    Intent intent = new Intent(getApplicationContext(), ExerciseFrequentActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }
}