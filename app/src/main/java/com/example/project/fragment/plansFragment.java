package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.adapter.PlanAdapter;
import com.example.project.model.Plan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.Calendar;

public class plansFragment extends Fragment {

    private RecyclerView rvPlans;
    private ImageButton btnAddPlan;
    private Button btnBackMain;
    private PlanAdapter planAdapter;
    private ArrayList<Plan> planList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);

        // Khởi tạo Firebase Auth và Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnBackMain = view.findViewById(R.id.btn_back_main);
        rvPlans = view.findViewById(R.id.rv_plans);
        btnAddPlan = view.findViewById(R.id.btn_add_plan);

        // Thiết lập RecyclerView
        rvPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        planAdapter = new PlanAdapter(planList, getContext());
        rvPlans.setAdapter(planAdapter);

        // Nút "Add" để mở Dialog thêm kế hoạch
        btnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPlanDialog();
            }
        });

        btnBackMain.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        // Load dữ liệu kế hoạch từ Firestore
        loadPlans();

        return view;
    }

    // Hiển thị dialog thêm kế hoạch
    private void showAddPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_plan, null);
        builder.setView(dialogView);

        TextView etPlanDescription = dialogView.findViewById(R.id.et_plan_description);
        TextView tvStartTime = dialogView.findViewById(R.id.tv_start_time);
        TextView tvEndTime = dialogView.findViewById(R.id.tv_end_time);
        TextView tvStartDate = dialogView.findViewById(R.id.tv_start_date);
        TextView tvEndDate = dialogView.findViewById(R.id.tv_end_date);
        Button btnAddPlanDialog = dialogView.findViewById(R.id.btn_add_plan);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Thiết lập sự kiện nhấn cho thời gian bắt đầu
        tvStartTime.setOnClickListener(v -> showTimePickerDialog(tvStartTime));
        tvEndTime.setOnClickListener(v -> showTimePickerDialog(tvEndTime));
        tvStartDate.setOnClickListener(v -> showDatePickerDialog(tvStartDate));
        tvEndDate.setOnClickListener(v -> showDatePickerDialog(tvEndDate));

        btnAddPlanDialog.setOnClickListener(v -> {
            String description = etPlanDescription.getText().toString().trim();
            String startTime = tvStartTime.getText().toString().trim();
            String endTime = tvEndTime.getText().toString().trim();
            String startDate = tvStartDate.getText().toString().trim();
            String endDate = tvEndDate.getText().toString().trim();

            if (TextUtils.isEmpty(description) || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) ||
                    TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu kế hoạch vào Firestore
            savePlan(description, startTime, endTime, startDate, endDate);
            dialog.dismiss();
        });
    }

    // Lưu kế hoạch vào Firestore
    private void savePlan(String description, String startTime, String endTime, String startDate, String endDate) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Tạo ID cho tài liệu
            String planId = db.collection("plans").document().getId(); // Tạo ID mới

            Map<String, Object> plan = new HashMap<>();
            plan.put("description", description);
            plan.put("startTime", startTime);
            plan.put("endTime", endTime);
            plan.put("startDate", startDate);
            plan.put("endDate", endDate);
            plan.put("userId", userId);
            plan.put("Id", planId); // Có thể cần thiết

            // Lưu kế hoạch với ID đã tạo
            db.collection("plans")
                    .document(planId) // Sử dụng ID đã tạo
                    .set(plan) // Sử dụng set thay vì add
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Cập nhật Id vào mô hình
                                plan.put("Id", planId); // Cập nhật Id trong HashMap nếu cần

                                Toast.makeText(getContext(), "Kế hoạch đã được thêm", Toast.LENGTH_SHORT).show();
                                loadPlans(); // Tải lại danh sách kế hoạch
                            } else {
                                Toast.makeText(getContext(), "Thêm kế hoạch không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    // Load kế hoạch từ Firestore
    private void loadPlans() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("plans")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                planList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Plan plan = document.toObject(Plan.class);
                                    planList.add(plan);
                                }
                                planAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Hiển thị TimePickerDialog
    private void showTimePickerDialog(TextView timeTextView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            timeTextView.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // Hiển thị DatePickerDialog
    private void showDatePickerDialog(TextView dateTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
            dateTextView.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }
}
