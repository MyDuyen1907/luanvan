package com.example.project.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapter.UserAdapter;
import com.example.project.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;



public class AccountAdminFragment extends Fragment {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore firestore;
    private SearchView searchViewUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_admin, container, false);

        firestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();

        recyclerViewUsers = view.findViewById(R.id.recyclerViewUser);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        userAdapter = new UserAdapter(userList, getContext());
        recyclerViewUsers.setAdapter(userAdapter);

        searchViewUser = view.findViewById(R.id.searchViewUser);
        setupSearchView();

        loadUsersFromFirestore();
        searchViewUser.setOnClickListener(v -> searchViewUser.setIconified(false));
        return view;
    }

    private void loadUsersFromFirestore() {
        firestore.collection("account")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear(); // Xóa danh sách cũ
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("userName");
                            String email = document.getString("email");
                            String userID = document.getId();
                            String role = document.getString("role");

                            // Tạo đối tượng User và thêm vào danh sách
                            User user = new User(name, email, userID, role);
                            userList.add(user);
                        }
                        userAdapter.filter(""); // Hiển thị lại toàn bộ danh sách sau khi tải
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi tải dữ liệu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setupSearchView() {
        searchViewUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.filter(newText); // Lọc danh sách
                return true;
            }
        });
    }
}
