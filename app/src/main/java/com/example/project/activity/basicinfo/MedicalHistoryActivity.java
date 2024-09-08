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

public class MedicalHistoryActivity extends AppCompatActivity {
    Button btnTiepTuc;
    EditText edtMedicalHistory, edtChronicDisease, edtVaccinationHistory, edtMedicalInterventions;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);


        edtMedicalHistory = findViewById(R.id.edtMedicalHistory);
        edtChronicDisease = findViewById(R.id.edtChronicDisease);
        edtVaccinationHistory = findViewById(R.id.edtVaccinationHistory);
        edtMedicalInterventions = findViewById(R.id.edtMedicalInterventions);
        btnTiepTuc = findViewById(R.id.btnTiepTuc); // Cần ánh xạ nút bấm btnTiepTuc
        user = new User();

        // Thiết lập sự kiện cho nút "Tiếp tục"
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra thông tin đầu vào
                if (edtMedicalHistory.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MedicalHistoryActivity.this, "Vui lòng thêm tiền sử bệnh của bạn", Toast.LENGTH_SHORT).show();
                } else if (edtChronicDisease.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MedicalHistoryActivity.this, "Vui lòng thêm bệnh mãn tính của bạn", Toast.LENGTH_SHORT).show();
                } else if (edtVaccinationHistory.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MedicalHistoryActivity.this, "Vui lòng thêm lịch sử tiêm chủng của bạn", Toast.LENGTH_SHORT).show();
                } else if (edtMedicalInterventions.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MedicalHistoryActivity.this, "Vui lòng thêm lịch sử can thiệp y tế của bạn", Toast.LENGTH_SHORT).show();
                }
                // chuyển đến activity tiếp theo
                else {
                    user = (User) getIntent().getSerializableExtra("user");
                    assert user != null;
                    user.setMedical_history(edtMedicalHistory.getText().toString().trim());
                    user.setChronic_disease(edtChronicDisease.getText().toString().trim());
                    user.setVaccination(edtVaccinationHistory.getText().toString().trim());
                    user.setMedical_interventions(edtMedicalInterventions.getText().toString().trim());

                    System.out.println(user.getMedical_history() + " " + user.getChronic_disease() + " " + user.getVaccination() + "" + user.getMedical_interventions());
                    Intent intent = new Intent(getApplicationContext(), CurrenthealthActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }
}
