package com.example.project.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.project.R;
import com.example.project.adapter.PlanAdapter;
import com.example.project.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class plansFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlanAdapter planAdapter;
    private List<Plan> planList; // Danh sách lưu kế hoạch
    private LinearLayout addPlanButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);

        // Khởi tạo các thành phần
        recyclerView = view.findViewById(R.id.todolist);
        addPlanButton = view.findViewById(R.id.add_plan);

        // Khởi tạo danh sách kế hoạch và adapter
        planList = new ArrayList<>();
        planAdapter = new PlanAdapter(planList); // Bạn cần tạo PlanAdapter để hiển thị kế hoạch
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(planAdapter);

        // Thiết lập sự kiện cho nút thêm kế hoạch
        addPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlanInputDialog();
            }
        });

        return view;
    }

    private void showPlanInputDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_plan_input, null);

        EditText planDescription = dialogView.findViewById(R.id.et_plan_description);
        EditText startDate = dialogView.findViewById(R.id.et_start_date);
        EditText endDate = dialogView.findViewById(R.id.et_end_date);
        EditText startTime = dialogView.findViewById(R.id.et_start_time);
        EditText endTime = dialogView.findViewById(R.id.et_end_time);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Nhập thông tin kế hoạch")
                .setView(dialogView)
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = planDescription.getText().toString();
                        String start = startDate.getText().toString() + " " + startTime.getText().toString();
                        String end = endDate.getText().toString() + " " + endTime.getText().toString();

                        // Thêm kế hoạch vào danh sách và cập nhật adapter
                        addPlanToTodoList(description, start, end);
                    }
                })
                .setNegativeButton("Hủy", null)
                .create();

        dialog.show();
    }

    private void addPlanToTodoList(String description, String start, String end) {
        // Tạo đối tượng Plan mới và thêm vào danh sách
        Plan newPlan = new Plan(description, start, end);
        planList.add(newPlan);
        planAdapter.notifyItemInserted(planList.size() - 1); // Cập nhật RecyclerView
    }
}