package com.example.project.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Medication;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private ArrayList<Medication> medicationList;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public MedicationAdapter(ArrayList<Medication> medicationList, Context context) {
        this.medicationList = medicationList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medicationList.get(position);

        // Gán giá trị cho các TextView từ đối tượng Medication
        holder.tvMedicineName.setText(medication.getmedicineName());
        holder.tvDosage.setText(medication.getDosage());
        holder.tvTime.setText(medication.getTime());
        holder.tvReminder.setText(medication.getReminder());
        holder.tvnote.setText(medication.getNote());
        holder.tvDate.setText(medication.getDate());


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        // Xử lý sự kiện nút "Chỉnh sửa"
        holder.btnEdit.setOnClickListener(v -> showEditDialog(medication));

        // Xử lý sự kiện nút "Xóa"
        holder.btnDelete.setOnClickListener(v -> deleteMedication(medication, position));
    }

    @Override
    public int getItemCount() {

        return medicationList.size();
    }

    // MedicationViewHolder class để ánh xạ với các view trong layout
    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvDosage, tvTime, tvReminder, tvnote,tvDate;
        ImageButton btnEdit, btnDelete;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các TextView và ImageButton
            tvMedicineName = itemView.findViewById(R.id.et_medicine_name);
            tvDosage = itemView.findViewById(R.id.et_dosage);
            tvTime = itemView.findViewById(R.id.et_time);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvReminder = itemView.findViewById(R.id.et_reminder);
            tvnote = itemView.findViewById(R.id.et_note);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    // Hàm xóa medication
    private void deleteMedication(Medication medication, int position) {
        if (currentUser != null) {
            String medicationId = medication.getId();

            db.collection("medications")
                    .document(medicationId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            medicationList.remove(position); // Xóa khỏi danh sách
                            notifyItemRemoved(position); // Cập nhật lại adapter
                            Toast.makeText(context, "Đã xóa đơn thuốc", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    // Hiển thị hộp thoại chỉnh sửa medication
    private void showEditDialog(Medication medication) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa đơn thuốc");

        // Inflate layout cho hộp thoại
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_medication, null);
        builder.setView(dialogView);

        // Ánh xạ các trường nhập liệu
        TextInputEditText etMedicineName = dialogView.findViewById(R.id.et_medicine_name);
        TextInputEditText etDosage = dialogView.findViewById(R.id.et_dosage);
        TextInputEditText etTime = dialogView.findViewById(R.id.et_time);
        TextInputEditText etReminder = dialogView.findViewById(R.id.et_reminder);
        TextInputEditText etNote = dialogView.findViewById(R.id.et_note);

        // Hiển thị giá trị hiện tại vào các trường
        etMedicineName.setText(medication.getmedicineName());
        etDosage.setText(medication.getDosage());
        etTime.setText(medication.getTime());
        etReminder.setText(medication.getReminder());
        etNote.setText(medication.getNote());

        // Nút "Lưu" khi chỉnh sửa
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Lấy giá trị mới từ người dùng
            String newMedicineName = etMedicineName.getText().toString();
            String newDosage = etDosage.getText().toString();
            String newTime = etTime.getText().toString();
            String newReminder = etReminder.getText().toString();
            String newNote = etNote.getText().toString();

            // Kiểm tra thay đổi
            boolean hasChanges = !newMedicineName.equals(medication.getmedicineName()) ||
                    !newDosage.equals(medication.getDosage()) ||
                    !newTime.equals(medication.getTime()) ||
                    !newReminder.equals(medication.getReminder()) ||
                    !newNote.equals(medication.getNote());


            if (hasChanges) {
                // Cập nhật đối tượng medication
                medication.setmedicineName(newMedicineName);
                medication.setDosage(newDosage);
                medication.setTime(newTime);
                medication.setReminder(newReminder);
                medication.setNote (newNote);

                // Cập nhật Firestore
                updateMedication(medication);
            } else {
                Toast.makeText(context, "Không có thay đổi", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút "Hủy" trong hộp thoại
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // Cập nhật đơn thuốc trong Firestore
    private void updateMedication(Medication medication) {
        if (currentUser != null) {
            String medicationId = medication.getId(); // Sử dụng ID của medication

            db.collection("medications")
                    .document(medicationId)
                    .set(medication)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đã cập nhật đơn thuốc", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged(); // Cập nhật lại adapter
                        } else {
                            Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
