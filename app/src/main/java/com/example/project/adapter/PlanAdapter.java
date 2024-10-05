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
        holder.tvTimeStart.setText(plan.getStartTime());
        holder.tvDateStart.setText(plan.getStartDate());
        holder.tvTimeEnd.setText(plan.getEndTime());
        holder.tvDateEnd.setText(plan.getEndDate());

        // Xử lý sự kiện nhấn nút chỉnh sửa
        holder.btnEdit.setOnClickListener(v -> showEditDialog(plan, position));

        // Xử lý sự kiện nhấn nút xóa
        holder.btnDelete.setOnClickListener(v -> deletePlan(plan, position));
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    private void showEditDialog(Plan plan, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa kế hoạch");

        // Inflate dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_plan, null);
        builder.setView(dialogView);

        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etTimeStart = dialogView.findViewById(R.id.et_start_time);
        EditText etDateStart = dialogView.findViewById(R.id.et_start_date);
        EditText etTimeEnd = dialogView.findViewById(R.id.et_end_time);
        EditText etDateEnd = dialogView.findViewById(R.id.et_end_date);

        // Đặt dữ liệu hiện tại vào dialog
        etDescription.setText(plan.getDescription());
        etTimeStart.setText(plan.getStartTime());
        etDateStart.setText(plan.getStartDate());
        etTimeEnd.setText(plan.getEndTime());
        etDateEnd.setText(plan.getEndDate());


        // Xử lý sự kiện khi người dùng nhấn "Lưu"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật thông tin kế hoạch
            plan.setDescription(etDescription.getText().toString());
            plan.setStartTime(etTimeStart.getText().toString());
            plan.setStartDate(etDateStart.getText().toString());
            plan.setEndTime(etTimeEnd.getText().toString());
            plan.setEndDate(etDateEnd.getText().toString());
            updatePlanInFirestore(plan, position);
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

    private void updatePlanInFirestore(Plan plan, int position) {
        // Cập nhật thông tin kế hoạch trong Firestore
        db.collection("plans")
                .document(plan.getId()) // Sử dụng ID của kế hoạch để xác định tài liệu
                .set(plan)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật kế hoạch trong danh sách và UI
                    planList.set(position, plan);
                    notifyItemChanged(position); // Cập nhật mục đó trên giao diện
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu không cập nhật thành công
                    Toast.makeText(context, "Cập nhật không thành công: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    public static class PlanViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription, tvTimeStart, tvDateStart, tvTimeEnd, tvDateEnd;
        ImageButton btnEdit, btnDelete;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvTimeStart = itemView.findViewById(R.id.tv_start_time);
            tvDateStart = itemView.findViewById(R.id.tv_start_date);
            tvTimeEnd = itemView.findViewById(R.id.tv_end_time);
            tvDateEnd = itemView.findViewById(R.id.tv_end_date);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
