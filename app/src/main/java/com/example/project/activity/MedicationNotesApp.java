package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapter.MedicationAdapter;
import com.example.project.model.Medication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationNotesApp extends AppCompatActivity {

    private EditText etMedicineName, etDosage, etTime, etReminder;
    private Button btnSave, medical;
    private RecyclerView rvMedications;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private MedicationAdapter adapter;
    private ArrayList<Medication> medicationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_notes_app);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        etMedicineName = findViewById(R.id.et_medicine_name);
        etDosage = findViewById(R.id.et_dosage);
        etTime = findViewById(R.id.et_time);
        btnSave = findViewById(R.id.btn_save);
        medical = findViewById(R.id.btnmedical);
        rvMedications = findViewById(R.id.rv_medications);
        etReminder = findViewById(R.id.et_reminder);

        // Set up RecyclerView
        rvMedications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicationAdapter(medicationList, this);
        rvMedications.setAdapter(adapter);

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Load medications from Firestore
        loadMedications();

        // Save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicineName = etMedicineName.getText().toString().trim();
                String dosage = etDosage.getText().toString().trim();
                String time = etTime.getText().toString().trim();
                String reminder = etReminder.getText().toString().trim();

                if (TextUtils.isEmpty(medicineName) || TextUtils.isEmpty(dosage) || TextUtils.isEmpty(time) || TextUtils.isEmpty(reminder)) {
                    Toast.makeText(MedicationNotesApp.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save medication to Firestore
                saveMedication(medicineName, dosage, time, reminder);
            }
        });
    }

    private void loadMedications() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("medications")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                medicationList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Medication medication = document.toObject(Medication.class);
                                    medication.setUserId(document.getId()); // Set document ID
                                    medicationList.add(medication);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MedicationNotesApp.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void saveMedication(String medicineName, String dosage, String time, String reminder) {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> medication = new HashMap<>();
            medication.put("medicineName", medicineName);
            medication.put("dosage", dosage);
            medication.put("time", time);
            medication.put("userId", userId);// Store user ID
            medication.put("reminder",reminder);

            db.collection("medications")
                    .add(medication)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MedicationNotesApp.this, "Đã lưu đơn thuốc", Toast.LENGTH_SHORT).show();
                                loadMedications(); // Refresh the list
                                clearInputs();
                            } else {
                                Toast.makeText(MedicationNotesApp.this, "Lưu không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void clearInputs() {
        etMedicineName.setText("");
        etDosage.setText("");
        etTime.setText("");
        etReminder.setText("");
    }
}