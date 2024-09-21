package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUserName.setText(user.getName());
        holder.tvUserEmail.setText(user.getEmail());

        // Xử lý khi nhấn nút "Xóa"
        holder.btnDeleteUser.setOnClickListener(v -> {
            deleteUser(user);
        });

        // Xử lý khi nhấn nút "Sửa"
        holder.btnEditUser.setOnClickListener(v -> {
            editUser(user);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail;
        Button btnEditUser, btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            btnEditUser = itemView.findViewById(R.id.btnEdit);
            btnDeleteUser = itemView.findViewById(R.id.btnDelete);
        }
    }

    // Hàm xóa người dùng từ Firestore
    private void deleteUser(User user) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("account").document(user.getEmail())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công, cập nhật danh sách
                    userList.remove(user);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi khi xóa người dùng.", Toast.LENGTH_SHORT).show();
                });
    }

    // Hàm chỉnh sửa người dùng
    private void editUser(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa người dùng");

        // Inflate layout cho dialog
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user, null);
        builder.setView(dialogView);

        EditText etUserName = dialogView.findViewById(R.id.etUserName);
        EditText etUserEmail = dialogView.findViewById(R.id.etUserEmail);

        // Điền thông tin hiện tại vào EditText
        etUserName.setText(user.getName());
        etUserEmail.setText(user.getEmail());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newUserName = etUserName.getText().toString().trim();
            String newUserEmail = etUserEmail.getText().toString().trim();

            // Cập nhật thông tin vào Firestore
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            // Sử dụng document ID là email để cập nhật
            firestore.collection("account").document(user.getEmail())
                    .update("userName", newUserName, "email", newUserEmail)
                    .addOnSuccessListener(aVoid -> {
                        // Cập nhật thành công
                        user.setName(newUserName);
                        user.setEmail(newUserEmail);
                        notifyDataSetChanged();  // Cập nhật RecyclerView
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}