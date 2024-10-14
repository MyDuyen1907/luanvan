package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.activity.ControlCaloriesActivity;
import com.example.project.activity.ControlWaterActivity;
import com.example.project.activity.ExerciseTimerActivity;
import com.example.project.activity.FoodNutritionActivity;
import com.example.project.activity.MedicationNotesApp;
import com.example.project.activity.SleeptrackingActivity;
import com.example.project.activity.UserActivity;
import com.example.project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {
    private CardView cvUser,cv_control_calories,cv_medical,cv_control_water,cv_exercise,cv_sleep;
    private TextView txvHello;
    private FirebaseFirestore db;
    private ImageView food_nut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Link views with their corresponding IDs
        cv_exercise = view.findViewById(R.id.cv_exercise);
        cv_control_water = view.findViewById(R.id.cv_control_water);
        cv_medical = view.findViewById(R.id.cv_medical);
        cv_control_calories = view.findViewById(R.id.cv_control_calories);
        cvUser = view.findViewById(R.id.cv_user);
        txvHello = view.findViewById(R.id.txvHello);
        cv_sleep = view.findViewById(R.id.cv_sleep);
        food_nut = view.findViewById(R.id.food_nut);

        // Fetch user data from Firestore and set the greeting text
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = u.getUid();
        db.collection("user").document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            txvHello.setText("" + user.getName());
                        }
                    } else {
                        // Handle the error here
                    }
                });

        // Set click listeners for each card to navigate to the appropriate activity
        cvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        });
        cv_control_calories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ControlCaloriesActivity.class);
                startActivity(intent);
            }
        });
        cv_medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MedicationNotesApp.class);
                startActivity(intent);
            }
        });
        cv_control_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ControlWaterActivity.class);
                startActivity(intent);
            }
        });
        cv_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExerciseTimerActivity.class);
                startActivity(intent);
            }
        });
        cv_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the SleepDataActivity
                Intent intent = new Intent(getActivity(), SleeptrackingActivity.class);
                startActivity(intent);
            }
        });
        food_nut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the FoodNutritionActivity
                Intent intent = new Intent(getActivity(), FoodNutritionActivity.class);
                startActivity(intent);
            }
        });

        return view; // Make sure to return the view at the end of onCreateView
    }
}
