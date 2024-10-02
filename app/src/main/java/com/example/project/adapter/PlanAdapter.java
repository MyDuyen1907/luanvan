package com.example.project.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private ArrayList<Plan> planList;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public PlanAdapter(ArrayList<Plan> planList, Context context) {
        this.planList = planList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.tvDescription.setText(plan.getDescription());
        holder.tvTime.setText(plan.getStartTime() + " - " + plan.getEndTime());
        holder.tvDate.setText(plan.getStartDate() + " - " + plan.getEndDate());

        // Xử lý sự kiện nhấn nút chỉnh sửa
        holder.btnEdit.setOnClickListener(v -> showEditDialog(plan));

        // Xử lý sự kiện nhấn nút xóa
        holder.btnDelete.setOnClickListener(v -> deletePlan(plan, position));
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    private void showEditDialog(Plan plan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa kế hoạch");

        // Inflate dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_plan, null);
        builder.setView(dialogView);

        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etTime = dialogView.findViewById(R.id.et_time);
        EditText etDate = dialogView.findViewById(R.id.et_date);

        // Đặt dữ liệu hiện tại vào dialog
        etDescription.setText(plan.getDescription());
        etTime.setText(plan.getStartTime() + " - " + plan.getEndTime());
        etDate.setText(plan.getStartDate() + " - " + plan.getEndDate());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật kế hoạch với thông tin mới
            plan.setDescription(etDescription.getText().toString());
            // Cập nhật giờ và ngày nếu cần thiết
            // Thực hiện cập nhật vào Firestore
            updatePlanInFirestore(plan);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Hàm xóa Plan
    private void deletePlan(Plan plan, int position) {
        if (currentUser != null) {
            String planId = plan.getId(); // Giả định rằng ID của kế hoạch được lưu trữ trong Plan
            Log.d("DeletePlan", "Attempting to delete plan with ID: " + planId);

            db.collection("plans")
                    .document(planId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đã xóa kế hoạch", Toast.LENGTH_SHORT).show();
                            planList.remove(position); // Xóa kế hoạch khỏi danh sách
                            notifyItemRemoved(position); // Thông báo adapter để cập nhật danh sách
                        } else {
                            Log.e("DeletePlan", "Error deleting plan: ", task.getException());
                            Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("DeletePlan", "Delete failed: ", e);
                        Toast.makeText(context, "Xóa không thành công: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePlanInFirestore(Plan plan) {
        // Cập nhật thông tin kế hoạch trong Firestore
        db.collection("plans").document(plan.getUserId()) // Giả định rằng ID của kế hoạch được lưu trữ trong Plan
                .set(plan)
                .addOnSuccessListener(aVoid -> {
                    notifyDataSetChanged(); // Cập nhật danh sách trong adapter
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi ở đây
                    Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                });
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvTime, tvDate;
        ImageButton btnEdit, btnDelete;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDate = itemView.findViewById(R.id.tv_date);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
