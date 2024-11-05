package com.example.project.adapter;

import android.content.Context;
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
import com.example.project.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;             // Danh sách gốc
    private List<User> filteredUserList;     // Danh sách lọc
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.filteredUserList = new ArrayList<>(userList); // Khởi tạo danh sách lọc
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
        User user = filteredUserList.get(position); // Sử dụng danh sách lọc
        holder.tvUserName.setText(user.getName());
        holder.tvUserEmail.setText(user.getEmail());
        holder.tvUserRole.setText(user.getRole());

        holder.btnDeleteUser.setOnClickListener(v -> deleteUser(user));
        holder.btnOptionUser.setOnClickListener(v -> {
            showRoleChangeDialog(user);
        });
    }

    @Override
    public int getItemCount() {
        return filteredUserList.size(); // Trả về kích thước của danh sách lọc
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserRole;
        ImageButton btnDeleteUser,btnOptionUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
            btnOptionUser = itemView.findViewById(R.id.btnOptionUser);
        }
    }

    public void filter(String query) {
        filteredUserList.clear(); // Xóa danh sách lọc hiện tại

        if (query.isEmpty()) {
            filteredUserList.addAll(userList); // Khôi phục danh sách gốc
        } else {
            query = query.toLowerCase(); // Chuyển đổi từ khóa thành chữ thường
            for (User user : userList) {
                if (user.getName().toLowerCase().contains(query) ||
                        user.getEmail().toLowerCase().contains(query)
                        || user.getRole().toLowerCase().contains(query)){
                    filteredUserList.add(user); // Thêm vào danh sách lọc nếu phù hợp
                }
            }
        }
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    private void deleteUser(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("account").document(user.getUserID())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        userList.remove(user);
                        filteredUserList.remove(user); // Cập nhật danh sách lọc khi xóa
                        notifyDataSetChanged();
                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi khi xóa người dùng.", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss(); // Đóng hộp thoại nếu người dùng chọn "Không"
        });

        AlertDialog dialog = builder.create();
        dialog.show(); // Hiển thị hộp thoại xác nhận
    }

    private void showRoleChangeDialog(User user) {
        // Tạo dialog với các tùy chọn thay đổi role
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi phân quyền");

        // Kiểm tra role hiện tại để hiển thị tùy chọn phù hợp
        String[] options;
        if (user.getRole().equals("user")) {
            options = new String[]{"Phân quyền Admin"};
        } else {
            options = new String[]{"Phân quyền User"};
        }

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Thêm dialog xác nhận
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(context);
                confirmBuilder.setTitle("Xác nhận thay đổi phân quyền");
                confirmBuilder.setMessage("Bạn có chắc chắn muốn thay đổi phân quyền của người dùng này không?");

                confirmBuilder.setPositiveButton("Đồng ý", (confirmDialog, confirmWhich) -> {
                    // Thực hiện thay đổi role sau khi xác nhận
                    String newRole = user.getRole().equals("user") ? "admin" : "user";
                    updateRoleInFirestore(user, newRole);

                    // Hiển thị thông báo phân quyền thành công
                    Toast.makeText(context, "Phân quyền thành công!", Toast.LENGTH_SHORT).show();
                });

                confirmBuilder.setNegativeButton("Hủy", (confirmDialog, confirmWhich) -> confirmDialog.dismiss());
                confirmBuilder.create().show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    private void updateRoleInFirestore(User user, String newRole) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Cập nhật trường role trong Firestore
        firestore.collection("account").document(user.getUserID())
                .update("role", newRole)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật role trong danh sách và thông báo cho adapter
                    user.setRole(newRole);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Phân quyền đã được cập nhật.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi khi cập nhật phân quyền.", Toast.LENGTH_SHORT).show();
                });
    }


}

