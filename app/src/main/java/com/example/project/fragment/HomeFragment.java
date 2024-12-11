package com.example.project.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.project.activity.DistanceTrackingActivity;
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class HomeFragment extends Fragment {
    private CardView cvUser, cv_control_calories, cv_medical, cv_control_water, cv_exercise, cv_sleep;
    private TextView txvHello;
    private FirebaseFirestore db;
    private ImageView food_nut, calimg;
    private CircularProgressBar calMeter;

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
        calimg = view.findViewById(R.id.calimg);
        calMeter = view.findViewById(R.id.cal_meter);

        // Fetch user data from Firestore and set the greeting text
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String user_id = user.getUid();
            db.collection("user").document(user_id)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User fetchedUser = document.toObject(User.class);
                                txvHello.setText("" + fetchedUser.getName());
                            }
                        }
                    });
        }

        // Set click listeners for each card to navigate to the appropriate activity
        cvUser.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), UserActivity.class)));
        cv_control_calories.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), ControlCaloriesActivity.class)));
        cv_medical.setOnClickListener(view13 -> startActivity(new Intent(getActivity(), MedicationNotesApp.class)));
        cv_control_water.setOnClickListener(view14 -> startActivity(new Intent(getActivity(), ControlWaterActivity.class)));
        cv_exercise.setOnClickListener(view15 -> startActivity(new Intent(getActivity(), ExerciseTimerActivity.class)));
        cv_sleep.setOnClickListener(view16 -> startActivity(new Intent(getActivity(), SleeptrackingActivity.class)));
        food_nut.setOnClickListener(view17 -> startActivity(new Intent(getActivity(), FoodNutritionActivity.class)));
        calimg.setOnClickListener(view18 -> startActivity(new Intent(getActivity(), DistanceTrackingActivity.class)));

        return view;
    }

    // Method to update the CircularProgressBar with calories burned
    private void updateCaloriesProgress() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("calorieData_" + userId, Context.MODE_PRIVATE);
        float caloriesBurned = sharedPreferences.getFloat("caloriesBurned", 0);

        int maxCalories = 500; // Example max calories
        calMeter.setProgress((caloriesBurned / maxCalories) * 100);
    }

    // Method to reset the CircularProgressBar
    private void resetCaloriesProgress() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("calorieData_" + userId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("caloriesBurned", 0);
        editor.apply();

        calMeter.setProgress(0);
    }

    // Method to add calories burned
    private void addCaloriesBurned(float calories) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("calorieData_" + userId, Context.MODE_PRIVATE);
        float currentCalories = sharedPreferences.getFloat("caloriesBurned", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("caloriesBurned", currentCalories + calories);
        editor.apply();

        updateCaloriesProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the CircularProgressBar whenever the fragment resumes
        updateCaloriesProgress();
    }
}
