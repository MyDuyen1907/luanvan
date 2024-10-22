package com.example.project.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Database.PasswordHasher;
import com.example.project.R;
import com.example.project.activity.basicinfo.GenderActivity;
import com.example.project.dao.AccountDAO;
import com.example.project.model.Account;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText email, username, password, password2;
    TextView txvDangNhap;
    Button btnDangKy;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        btnDangKy = findViewById(R.id.btnDangKy);
        email = findViewById(R.id.edtEmail);
        username = findViewById(R.id.edtName);
        password = findViewById(R.id.edtPassword);
        password2 = findViewById(R.id.edtPassword2);
        txvDangNhap = findViewById(R.id.txvDangNhap);

        txvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(email) && isEmpty(username) && isEmpty(password) && isEmpty(password2)) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập tất cả thông tin", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(email))
                    Toast.makeText(RegisterActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
                else if (isEmpty(username))
                    Toast.makeText(RegisterActivity.this, "Nhập username", Toast.LENGTH_SHORT).show();
                else if (username.getText().toString().trim().length() < 2)
                    Toast.makeText(RegisterActivity.this, "Username phải có ít nhất 2 ký tự", Toast.LENGTH_SHORT).show();
                else if (isEmpty(password))
                    Toast.makeText(RegisterActivity.this, "Nhập password", Toast.LENGTH_SHORT).show();
                else if (isEmpty(password2))
                    Toast.makeText(RegisterActivity.this, "Nhập mật khẩu xác nhận", Toast.LENGTH_SHORT).show();
                else if (!password.getText().toString().trim().equals(password2.getText().toString().trim()))
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                else {
                    String user_email = email.getText().toString().trim();
                    String user_name = username.getText().toString().trim();
                    String user_password = password.getText().toString().trim();

                    // Tạo tài khoản mới cho Firebase Authentication
                    auth.createUserWithEmailAndPassword(user_email, user_password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Đăng ký thành công, cập nhật giao diện người dùng với thông tin người dùng đã đăng nhập
                                        Log.d(TAG, "createUserWithEmail:success");

                                        FirebaseUser currentUser = auth.getCurrentUser();

                                        // Tạo Người dùng mới để sử dụng trong gói hoạt động BasicInfo
                                        User user = new User();
                                        user.setId(currentUser.getUid());
                                        user.setName(user_name);

                                        // Tạo Account mới và thêm vào Firestore với UID làm document ID
                                        Account account = new Account();
                                        PasswordHasher passwordHasher = new PasswordHasher();
                                        String hashPassword = passwordHasher.hashPassword(user_password);
                                        account.setEmail(user_email);
                                        account.setPassword(hashPassword);
                                        account.setUserName(user_name);
                                        account.setUserID(currentUser.getUid());
                                        account.setRole("user");

                                        AccountDAO accountDAO = new AccountDAO();
                                        accountDAO.addAccountWithID(currentUser.getUid(), account); // Sử dụng UID làm ID cho document

                                        // Hiển thị thông báo đăng ký thành công
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                                        // Điều hướng tới màn hình GenderActivity
                                        Intent intent = new Intent(getApplicationContext(), GenderActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    } else {
                                        // Xử lý lỗi khi tạo tài khoản
                                        Exception exception = task.getException();
                                        if (exception instanceof FirebaseAuthException) {
                                            String errorCode = ((FirebaseAuthException) exception).getErrorCode();

                                            if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                                                Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                                            } else if (errorCode.equals("ERROR_WEAK_PASSWORD")) {
                                                Toast.makeText(RegisterActivity.this, "Mật khẩu phải tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
                                            } else if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                                Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                exception.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
