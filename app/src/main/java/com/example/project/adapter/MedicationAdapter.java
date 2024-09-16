package com.example.project.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

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
        holder.tvMedicineName.setText(medication.getmedicineName());
        holder.tvDosage.setText(medication.getDosage());
        holder.tvTime.setText(medication.getTime());

        holder.itemView.setOnLongClickListener(view -> {
            // Show options for Edit/Delete
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Tùy chọn");
            builder.setItems(new CharSequence[]{"Chỉnh sửa", "Xóa"}, (dialog, which) -> {
                if (which == 0) {
                    showEditDialog(medication);
                } else if (which == 1) {
                    // Delete medication
                    deleteMedication(medication);
                }
            });
            builder.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvDosage, tvTime;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.et_medicine_name);
            tvDosage = itemView.findViewById(R.id.et_dosage);
            tvTime = itemView.findViewById(R.id.et_time);
        }
    }

    private void deleteMedication(Medication medication) {
        if (currentUser != null) {
            String medicationId = medication.getUserId(); // Get the ID of the medication to delete

            db.collection("medications")
                    .document(medicationId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đã xóa đơn thuốc", Toast.LENGTH_SHORT).show();
                            medicationList.remove(medication);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showEditDialog(Medication medication) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa đơn thuốc");

        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_medication, null);
        builder.setView(dialogView);

        // Initialize input fields
        TextInputEditText etMedicineName = dialogView.findViewById(R.id.et_medicine_name);
        TextInputEditText etDosage = dialogView.findViewById(R.id.et_dosage);
        TextInputEditText etTime = dialogView.findViewById(R.id.et_time);

        // Set current values to inputs
        etMedicineName.setText(medication.getmedicineName());
        etDosage.setText(medication.getDosage());
        etTime.setText(medication.getTime());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Retrieve updated values
            String newMedicineName = etMedicineName.getText().toString();
            String newDosage = etDosage.getText().toString();
            String newTime = etTime.getText().toString();

            // Check if any value has changed
            boolean hasChanges = !newMedicineName.equals(medication.getmedicineName()) ||
                    !newDosage.equals(medication.getDosage()) ||
                    !newTime.equals(medication.getTime());

            if (hasChanges) {
                // Update medication object
                medication.setmedicineName(newMedicineName);
                medication.setDosage(newDosage);
                medication.setTime(newTime);

                // Update Firestore
                updateMedication(medication);
            } else {
                Toast.makeText(context, "Không có thay đổi", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void updateMedication(Medication medication) {
        if (currentUser != null) {
            String medicationId = medication.getUserId(); // Use the existing ID to update

            db.collection("medications")
                    .document(medicationId)
                    .set(medication)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đã cập nhật đơn thuốc", Toast.LENGTH_SHORT).show();
                            // Optionally, you can refresh the RecyclerView here
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
