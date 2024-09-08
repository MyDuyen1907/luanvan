package com.example.project.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtNewPass, edtConfirmPass;
    private Button btnChangePass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password); // Assuming your layout file is activity_change_password.xml

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind the views
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);

        // Set click listener for the Change Password button
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String newPassword = edtNewPass.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        // Validate the input
        if (TextUtils.isEmpty(newPassword)) {
            edtNewPass.setError("Please enter a new password");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPass.setError("Please confirm your password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPass.setError("Passwords do not match");
            return;
        }

        // Get the current user from Firebase Auth
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Update the user's password
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after successful password change
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Password change failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
