package com.example.project.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class ContactDoctorActivity extends AppCompatActivity {

    private LinearLayout layoutHotlineContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_doctor);

        layoutHotlineContainer = findViewById(R.id.layout_hotline_container);

        // Add hotline numbers
        addHotline("Doctor 1", "123456789");
        addHotline("Doctor 2", "987654321");
        addHotline("Doctor 3", "555555555");
    }

    private void addHotline(final String name, final String phoneNumber) {
        // Create a new TextView for each hotline
        TextView hotlineTextView = new TextView(this);
        hotlineTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        hotlineTextView.setText(name + ": " + phoneNumber);
        hotlineTextView.setTextSize(16);
        hotlineTextView.setPadding(16, 16, 16, 16);
        hotlineTextView.setTextColor(getResources().getColor(R.color.black));

        // Set click listener to initiate a phone call
        hotlineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(phoneNumber);
            }
        });

        // Add the TextView to the container
        layoutHotlineContainer.addView(hotlineTextView);
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
