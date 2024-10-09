package com.example.project.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog; // Thêm import cho TimePickerDialog
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MedicationNotesApp extends AppCompatActivity {

    private EditText etMedicineName, etDosage, etTime, etReminder, etNote; // Sửa lại tên biến
    private Button btnSave, medical;
    private RecyclerView rvMedications;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private MedicationAdapter adapter;
    private ArrayList<Medication> medicationList = new ArrayList<>();
    private Switch switchReminder;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

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
        switchReminder = findViewById(R.id.switch_reminder);
        etNote = findViewById(R.id.et_note);

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
                String note = etNote.getText().toString().trim();

                if (TextUtils.isEmpty(medicineName) || TextUtils.isEmpty(dosage) || TextUtils.isEmpty(time) || TextUtils.isEmpty(reminder) || TextUtils.isEmpty(note)) {
                    Toast.makeText(MedicationNotesApp.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save medication to Firestore
                saveMedication(medicineName, dosage, time, reminder, note);
                // Đặt nhắc nhở nếu Switch được bật
                if (switchReminder.isChecked()) {
                    setReminder(medicineName, dosage, time, reminder);
                }
            }
        });

        // Sự kiện nhấn cho ô nhập thời gian
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(etTime);
            }
        });

        // Sự kiện nhấn cho ô nhập thời gian nhắc nhở
        etReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(etReminder);
            }
        });
    }

    private void showTimePickerDialog(EditText editText) {
        // Lấy giờ và phút hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Hiển thị TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            // Cập nhật ô nhập với giờ và phút đã chọn
            editText.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void setReminder(String medicineName, String dosage, String time, String reminder) {
        // Chuyển đổi thời gian nhắc nhở từ string thành Calendar
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(reminder);
            if (date != null) {
                // Lấy giờ và phút từ thời gian người dùng nhập
                Calendar reminderTime = Calendar.getInstance();
                reminderTime.setTime(date);
                int reminderHour = reminderTime.get(Calendar.HOUR_OF_DAY);
                int reminderMinute = reminderTime.get(Calendar.MINUTE);

                // Thiết lập giờ và phút vào Calendar cho nhắc nhở
                calendar.set(Calendar.HOUR_OF_DAY, reminderHour);
                calendar.set(Calendar.MINUTE, reminderMinute);
                calendar.set(Calendar.SECOND, 0);  // Đặt giây về 0

                // Nếu thời gian nhắc nhở đã trôi qua trong ngày hôm nay, chuyển sang ngày hôm sau
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);  // Cộng thêm 1 ngày
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Thiết lập AlarmManager để gửi thông báo
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("medicineName", medicineName);
        intent.putExtra("dosage", dosage);
        intent.putExtra("time", time);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Đặt báo thức
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            // Hiển thị thời gian nhắc nhở trong Toast
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            Toast.makeText(this, "Đã đặt nhắc nhở vào lúc " + formattedTime, Toast.LENGTH_SHORT).show();
        }
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
                                    medication.setUserId(userId); // Set userId
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


    private void saveMedication(String medicineName, String dosage, String time, String reminder, String note) {
        if (currentUser != null) {
            String userId = currentUser.getUid();  // Lấy userId của người dùng

            Map<String, Object> medication = new HashMap<>();
            medication.put("medicineName", medicineName);
            medication.put("dosage", dosage);
            medication.put("time", time);
            medication.put("userId", userId);  // Lưu userId
            medication.put("reminder", reminder);
            medication.put("note", note);

            db.collection("medications")
                    .add(medication)   // Sử dụng add() để tạo Document mới
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MedicationNotesApp.this, "Đã lưu đơn thuốc", Toast.LENGTH_SHORT).show();
                                loadMedications();  // Tải lại danh sách
                                clearInputs();  // Xóa các ô nhập sau khi lưu thành công
                            } else {
                                Toast.makeText(MedicationNotesApp.this, "Lưu đơn thuốc không thành công", Toast.LENGTH_SHORT).show();
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
        etNote.setText("");
    }
}