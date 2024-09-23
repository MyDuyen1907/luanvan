package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    }

    @Override
    public int getItemCount() {
        return filteredUserList.size(); // Trả về kích thước của danh sách lọc
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserRole;
        ImageButton btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
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
    }
}

