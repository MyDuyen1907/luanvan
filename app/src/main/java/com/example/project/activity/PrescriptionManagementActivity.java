package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapter.SideEffectAdapter;
import com.example.project.model.SideEffect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionManagementActivity extends AppCompatActivity {
    private EditText edtMedicineName, edtDosage, edtFrequency, edtTime;
    private Button btnSavePrescription, btnRecordSideEffect, medical;
    private RecyclerView recyclerViewSideEffects;
    private SideEffectAdapter sideEffectAdapter;
    private List<SideEffect> sideEffectList = new ArrayList<>();
    private FirebaseFirestore db;
    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null; // Handle the case where the user is not logged in
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_management);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Liên kết các view từ layout
        edtMedicineName = findViewById(R.id.edtMedicineName);
        edtDosage = findViewById(R.id.edtDosage);
        edtFrequency = findViewById(R.id.edtFrequency);
        edtTime = findViewById(R.id.edtTime);
        btnSavePrescription = findViewById(R.id.btnSavePrescription);
        btnRecordSideEffect = findViewById(R.id.btnRecordSideEffect);
        recyclerViewSideEffects = findViewById(R.id.recyclerViewSideEffects);
        medical = findViewById(R.id.btnmedical);

        // Thiết lập RecyclerView
        recyclerViewSideEffects.setLayoutManager(new LinearLayoutManager(this));
        sideEffectAdapter = new SideEffectAdapter(sideEffectList);
        recyclerViewSideEffects.setAdapter(sideEffectAdapter);

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện lưu đơn thuốc
        btnSavePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrescription();
            }
        });

        // Xử lý sự kiện ghi nhận tác dụng phụ
        btnRecordSideEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecordSideEffectDialog();
            }
        });

        // Tải danh sách tác dụng phụ
        loadSideEffects();
    }

    private void savePrescription() {
        String medicineName = edtMedicineName.getText().toString();
        String dosage = edtDosage.getText().toString();
        String frequency = edtFrequency.getText().toString();
        String time = edtTime.getText().toString();
        String userId = getCurrentUserId(); // Implement this method to get the current user ID

        if (medicineName.isEmpty() || dosage.isEmpty() || frequency.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> prescription = new HashMap<>();
        prescription.put("medicineName", medicineName);
        prescription.put("dosage", dosage);
        prescription.put("frequency", frequency);
        prescription.put("time", time);
        prescription.put("userId", userId);

        db.collection("prescriptions")
                .add(prescription)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PrescriptionManagementActivity.this, "Lưu đơn thuốc thành công", Toast.LENGTH_SHORT).show();
                        scheduleMedicationReminder(time);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PrescriptionManagementActivity.this, "Lưu đơn thuốc thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecordSideEffectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_record_side_effect, null);
        builder.setView(dialogView);

        final EditText edtSideEffect = dialogView.findViewById(R.id.edtSideEffect);

        builder.setTitle("Ghi nhận tác dụng phụ")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sideEffectDescription = edtSideEffect.getText().toString();
                        String userId = getCurrentUserId(); // Implement this method to get the current user ID

                        if (sideEffectDescription.isEmpty()) {
                            Toast.makeText(PrescriptionManagementActivity.this, "Vui lòng nhập tác dụng phụ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, Object> sideEffect = new HashMap<>();
                        sideEffect.put("description", sideEffectDescription);
                        sideEffect.put("userId", userId);

                        db.collection("sideEffects")
                                .add(sideEffect)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(PrescriptionManagementActivity.this, "Ghi nhận tác dụng phụ thành công", Toast.LENGTH_SHORT).show();
                                        loadSideEffects(); // Reload side effects
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PrescriptionManagementActivity.this, "Ghi nhận tác dụng phụ thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    private void saveSideEffect(String sideEffect) {
        Map<String, Object> data = new HashMap<>();
        data.put("description", sideEffect);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("sideEffects")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PrescriptionManagementActivity.this, "Lưu tác dụng phụ thành công", Toast.LENGTH_SHORT).show();
                        loadSideEffects(); // Tải lại danh sách sau khi lưu
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PrescriptionManagementActivity.this, "Lưu tác dụng phụ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadSideEffects() {
        String userId = getCurrentUserId(); // Implement this method to get the current user ID

        db.collection("sideEffects")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sideEffectList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SideEffect sideEffect = document.toObject(SideEffect.class);
                                sideEffectList.add(sideEffect);
                            }
                            sideEffectAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PrescriptionManagementActivity.this, "Lấy danh sách tác dụng phụ thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void scheduleMedicationReminder(String time) {
        // Implement scheduling reminders with Firebase Cloud Messaging (FCM) here
        // This is a placeholder. Actual implementation would involve setting up FCM and creating scheduled notifications.
    }
}
