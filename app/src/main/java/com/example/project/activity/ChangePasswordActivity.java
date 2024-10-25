package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPass, edtNewPass, edtConfirmPass;
    private Button btnChangePass,btn_back_change_pass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();

        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);
        btn_back_change_pass = findViewById(R.id.btn_back_change_pass);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        btn_back_change_pass.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void changePassword() {
        String oldPassword = edtOldPass.getText().toString().trim();
        String newPassword = edtNewPass.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        // Validate the input
        if (TextUtils.isEmpty(oldPassword)) {
            edtOldPass.setError("Nhập mật khẩu cũ");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            edtNewPass.setError("Nhập mật khẩu mới");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPass.setError("Nhập lại mật khẩu mới");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPass.setError("Mật khẩu không khớp");
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Get user credentials
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            // Reauthenticate the user
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // If reauthentication is successful, update the password
                    user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(ChangePasswordActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after successful password change
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Thay đổi mật khẩu thất bại. Thử lại", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
