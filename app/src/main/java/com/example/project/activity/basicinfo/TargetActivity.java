package com.example.project.activity.basicinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.project.R;
import com.example.project.model.User;

public class TargetActivity extends AppCompatActivity {

    Button btnTiepTuc;
    RadioButton rbTang, rbDuyTri, rbGiam;
    RadioGroup radioGroup;
    View rbChecked;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        rbTang = findViewById(R.id.rbTangCan);
        rbDuyTri = findViewById(R.id.rbDuyTri);
        rbTang = findViewById(R.id.rbTangCan);
        radioGroup = findViewById(R.id.radioGroup);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        user = new User();

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = (User) getIntent().getSerializableExtra("user");

                int selectedID = radioGroup.getCheckedRadioButtonId();
                rbChecked = findViewById(selectedID);
                int position = radioGroup.indexOfChild(rbChecked);

                user.setAim(position);

                Intent intent = new Intent(getApplicationContext(), FinishActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
}
