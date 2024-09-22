package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.activity.ChangePasswordActivity;
import com.example.project.activity.ContactDoctorActivity;
import com.example.project.activity.LoginActivity;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;

public class ManageAdminFragment extends Fragment {

    private LinearLayout layoutSignOut;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_admin, container, false);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        layoutSignOut = view.findViewById(R.id.layout_sign_out);

        layoutSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng xuất và chuyển hướng sang trang đăng nhập
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}