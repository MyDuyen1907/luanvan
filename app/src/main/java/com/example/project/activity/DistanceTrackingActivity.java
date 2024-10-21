package com.example.project.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.project.R;
import com.example.project.model.DistanceRecord;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DistanceTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvDistance, tvCalories, tvTime;
    private Button btnStartPause,btn_back_distance_tracking;
    private boolean isTracking = false;
    private double totalDistance = 0.0;
    private long startTime = 0;
    private double caloriesBurned = 0.0;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Handler handler = new Handler();
    private Runnable runnable;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    // Map variables
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_tracking);

        tvDistance = findViewById(R.id.tvDistance);
        tvCalories = findViewById(R.id.tvCalories);
        tvTime = findViewById(R.id.tvTime);
        btnStartPause = findViewById(R.id.btnStartPause);
        mapView = findViewById(R.id.mapView);
        btn_back_distance_tracking = findViewById(R.id.btn_back_distance_tracking);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        btnStartPause.setOnClickListener(view -> {
            if (isTracking) {
                stopTracking();
            } else {
                startTracking();
            }
        });
        btn_back_distance_tracking.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void startTracking() {
        isTracking = true;
        btnStartPause.setText("Tạm dừng");
        startTime = System.currentTimeMillis();

        runnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                String time = formatTime(elapsedTime);
                tvTime.setText("Thời gian: " + time);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            startLocationUpdates();
        }
    }


    // Bắt đầu cập nhật vị trí
    private void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (lastLocation != null) {
                        totalDistance += lastLocation.distanceTo(location) / 1000;
                        tvDistance.setText(String.format(Locale.getDefault(), "Quãng đường: %.2f km", totalDistance));

                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Bạn đang ở đây"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                    }
                    lastLocation = location;
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        }
    }

    private void stopTracking() {
        isTracking = false;
        btnStartPause.setText("Bắt đầu");

        handler.removeCallbacks(runnable);

        long elapsedTime = System.currentTimeMillis() - startTime;

        String time = formatTime(elapsedTime);
        tvTime.setText("Thời gian: " + time);

        caloriesBurned = totalDistance * 43;
        tvCalories.setText(String.format(Locale.getDefault(), "Calo tiêu thụ: %.2f cal", caloriesBurned));

        // Lưu calo vào SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("calorieData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("caloriesBurned", (float) caloriesBurned);
        editor.apply();

        // Dừng cập nhật vị trí
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }



    // Tính toán và lưu vào Firestore
    private void saveToFirestore(String userId, double distance, double calories, long elapsedTime) {
        CollectionReference userDistanceRef = db.collection("distanceTracking").document(userId).collection("records");

        // Tạo bản ghi
        // Tạo bản ghi
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        DistanceRecord record = new DistanceRecord(distance, calories, elapsedTime, timestamp, new Date());

        // Lưu bản ghi vào Firestore
        userDistanceRef.add(record).addOnSuccessListener(documentReference -> {
            Toast.makeText(DistanceTrackingActivity.this, "Dữ liệu đã được lưu.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(DistanceTrackingActivity.this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Định dạng thời gian
    private String formatTime(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        // Trả về chuỗi thời gian định dạng "HH:mm:ss"
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Kiểm tra quyền truy cập trước khi gọi setMyLocationEnabled
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Quyền truy cập vị trí không được cấp", Toast.LENGTH_SHORT).show();
        }

        // Thiết lập các tùy chọn cho bản đồ
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }


}
