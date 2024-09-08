package com.example.project.activity.basicinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.dao.UserDAO;
import com.example.project.model.User;


public class FinishActivity extends AppCompatActivity {

    Button btnHoanThanh;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        btnHoanThanh = findViewById(R.id.btnHoanThanh);
        user = new User();

        btnHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add user data to database
                user = (User) getIntent().getSerializableExtra("user");
                UserDAO userDAO = new UserDAO();
                userDAO.addUser(user);

                // Move to summary screen
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}