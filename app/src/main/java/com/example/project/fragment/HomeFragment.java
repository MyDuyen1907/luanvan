package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.activity.ControlCaloriesActivity;
import com.example.project.activity.ControlWaterActivity;
import com.example.project.activity.SleepTrackerActivity;
import com.example.project.activity.UserActivity;
import com.example.project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {
    private CardView cvUser, controlCaloriesCardView, controlWaterCardView, cv_sleep;
    private TextView txvHello;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Link views with their corresponding IDs
        cvUser = view.findViewById(R.id.cv_user);
        txvHello = view.findViewById(R.id.txvHello);
        controlCaloriesCardView = view.findViewById(R.id.cv_control_calories);
        controlWaterCardView= view.findViewById(R.id.cv_control_water);
        cv_sleep = view.findViewById(R.id.cv_sleep);


        // Fetch user data from Firestore and set the greeting text
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = u.getUid();
        db.collection("user").document(user_id) // Replace "user_id" with the actual user ID
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
        controlCaloriesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ControlCaloriesActivity.class);
                startActivity(intent);
            }
        });
        controlWaterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ControlWaterActivity.class);
                startActivity(intent);
            }
        });
        cv_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SleepTrackerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
