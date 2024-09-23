package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapter.UserAdapter;
import com.example.project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class AccountAdminFragment extends Fragment {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_admin, container, false);

        // Khởi tạo Firestore và danh sách người dùng
        firestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();

        // Thiết lập RecyclerView
        recyclerViewUsers = view.findViewById(R.id.recyclerViewUser);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo adapter và gắn vào RecyclerView
        userAdapter = new UserAdapter(userList, getContext());
        recyclerViewUsers.setAdapter(userAdapter);

        // Tải dữ liệu người dùng từ Firestore
        loadUsersFromFirestore();

        return view;
    }

    private void loadUsersFromFirestore() {
        firestore.collection("account")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("userName");
                            String email = document.getString("email");
                            String userID = document.getId();
                            String role = document.getString("role");

                            // Tạo đối tượng User và thêm vào danh sách
                            User user = new User(name, email, userID, role);
                            userList.add(user);
                        }

                        // Thông báo cho adapter rằng dữ liệu đã thay đổi
                        userAdapter.notifyDataSetChanged();
                    } else {
                        // Xử lý lỗi nếu không lấy được dữ liệu
                    }
                });
    }}