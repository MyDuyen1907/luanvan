package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import static android.content.ContentValues.TAG;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.project.R;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView id,name, age, gender, height, weight,
            heart, bloodpressure, bloodsugar, cholesterol,
            medicalhistory, choricdisease,vaccination, medicalite
            ,maxCalories,maxWater,bmi,exerciseFrequency,txv_bmi,ttde;
    ;
    ImageView imv_bmi;
    Button btnBackUser,btnUpdate ;
    FirebaseUser user;
    FirebaseFirestore db;
    User p;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    ToggleButton carret;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        name = findViewById(R.id.txv_name);
        maxCalories = findViewById(R.id.txv_max_calories);
        maxWater = findViewById(R.id.txv_max_water);
        bmi = findViewById(R.id.txv_bmi);
        age = findViewById(R.id.txv_age);
        ttde = findViewById(R.id.txv_ttde);
        gender = findViewById(R.id.txv_gender);
        height = findViewById(R.id.txv_height);
        weight = findViewById(R.id.txv_weight);
        medicalhistory = findViewById(R.id.txv_disease_history);
        choricdisease = findViewById(R.id.txv_chronic_diseases);
        vaccination = findViewById(R.id.txv_vaccination_history);
        medicalite = findViewById(R.id.txv_medical_interventions);
        heart = findViewById(R.id.txv_heart_rate);
        bloodpressure = findViewById(R.id.txv_blood_pressure);
        bloodsugar = findViewById(R.id.txv_blood_sugar);
        cholesterol = findViewById(R.id.txv_cholesterol);
        btnBackUser = findViewById(R.id.btn_back_user);
        txv_bmi = findViewById(R.id.txv_bmi123);
        imv_bmi = findViewById(R.id.imv_bmi123);
        carret = findViewById(R.id.carret);
        btnUpdate = findViewById(R.id.btn_update);
        exerciseFrequency = findViewById(R.id.txv_exercise);

        carret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){
                    txv_bmi.setVisibility(View.VISIBLE);
                    imv_bmi.setVisibility(View.VISIBLE);
                }
                else {
                    imv_bmi.setVisibility(View.GONE);
                    txv_bmi.setVisibility(View.GONE);
                }
            }
        });
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setInfoUser();
        btnBackUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdate();
            }
        });
    }

    private void setInfoUser() {
        db = FirebaseFirestore.getInstance();
        Log.e(TAG, currentUser.getUid());
        db.collection("user").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null) {
                                p = documentSnapshot.toObject(User.class);
                                // Set text views with the user data
                                name.setText(p.getName());
                                age.setText(String.valueOf(p.getAge()));
                                height.setText(String.valueOf(p.getHeight()));
                                weight.setText(String.valueOf(p.getWeight()));
                                ttde.setText(String.valueOf(p.TTDECal()));
                                bmi.setText(String.valueOf(p.BMICal())); // Use BMICal method
                                maxWater.setText(String.valueOf(p.WaterCal())); // Use WaterCal method
                                medicalhistory.setText(p.getMedical_history());
                                choricdisease.setText(p.getChronic_disease());
                                vaccination.setText(p.getVaccination());
                                medicalite.setText(p.getMedical_interventions());
                                heart.setText(String.valueOf(p.getHeart_rate()));
                                bloodpressure.setText(String.valueOf(p.getBlood_pressure()));
                                bloodsugar.setText(String.valueOf(p.getBlood_sugar()));
                                cholesterol.setText(String.valueOf(p.getCholesterol()));

                                // Gender
                                switch (p.getGender()) {
                                    case 0:
                                        gender.setText("Nữ");
                                        break;
                                    case 1:
                                        gender.setText("Nam");
                                        break;
                                }

                                // Exercise Frequency
                                switch (p.getExerciseFrequency()) {
                                    case 0:
                                        exerciseFrequency.setText("Không vận động");
                                        break;
                                    case 1:
                                        exerciseFrequency.setText("Vận động nhẹ (1-3 ngày/tuần)");
                                        break;
                                    case 2:
                                        exerciseFrequency.setText("Vận động vừa (4-5 ngày/tuần)");
                                        break;
                                    case 3:
                                        exerciseFrequency.setText("Vận động nặng (6-7 ngày/tuần)");
                                        break;
                                }
                            }
                            }
                        }
                });
    }

    private void showDialogUpdate() {

        User updateUser = p;
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_info_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();

        EditText editAge = view.findViewById(R.id.edit_age);
        RadioGroup rdgGender = view.findViewById(R.id.rdg_gender);
        RadioButton rdFemale = view.findViewById(R.id.rd_female);
        RadioButton rdMale = view.findViewById(R.id.rd_male);
        EditText editHeight = view.findViewById(R.id.edit_height);
        EditText editWeight = view.findViewById(R.id.edit_weight);
        RadioGroup rdgActivityFrequency = view.findViewById(R.id.rdg_exercise);
        RadioButton rdKhong = view.findViewById(R.id.rd_khong);
        RadioButton rdNhe = view.findViewById(R.id.rd_nhe);
        RadioButton rdVua = view.findViewById(R.id.rd_vua);
        RadioButton rdNang = view.findViewById(R.id.rd_nang);
        Button btnReup = view.findViewById(R.id.btn_reup);

        editAge.setText(String.valueOf(p.getAge()));
        editHeight.setText(String.valueOf(p.getHeight()));
        editWeight.setText(String.valueOf(p.getWeight()));

        if(p.getGender()==0) {
            rdFemale.setChecked(true);
        }else{
            rdMale.setChecked(true);
        }
        if(p.getExerciseFrequency()==0) {
            rdKhong.setChecked(true);
        }else if (p.getExerciseFrequency()==1) {
            rdNhe.setChecked(true);
        }else if(p.getExerciseFrequency()==2) {
            rdVua.setChecked(true);
        }else{
            rdNang.setChecked(true);
        }

        rdgActivityFrequency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_khong) {
                    updateUser.setExerciseFrequency(0);
                } else if (checkedId == R.id.rd_nhe) {
                    updateUser.setExerciseFrequency(1);
                } else if (checkedId == R.id.rd_vua) {
                    updateUser.setExerciseFrequency(2);
                } else if (checkedId == R.id.rd_nang) {
                    updateUser.setExerciseFrequency(3);
                }

            }
        });
        rdgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_female) {
                    updateUser.setGender(0);
                } else if (checkedId == R.id.rd_male) {
                    updateUser.setGender(1);
                }
            }
        });
        btnReup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser.setAge(Integer.parseInt(String.valueOf(editAge.getText())));
                updateUser.setHeight(Integer.parseInt(String.valueOf(editHeight.getText())));
                updateUser.setWeight(Integer.parseInt(String.valueOf(editWeight.getText())));

                db.collection("user").document(user.getUid()).set(updateUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Success Update!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Fail Update", e);
                            }
                        });
                dialog.dismiss();
                recreate();

            }
        });


    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}