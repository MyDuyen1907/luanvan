package com.example.project.adapter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;
    private ArrayList<Exercise> exerciseList;
    Handler handler;
    int hour, min, sec, milliSec;
    long tMilliSec, tStart, tStop, tBuff, tUpdate = 0L;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    FirebaseFirestore db;
    String uID;

    public ExerciseAdapter(Context context, ArrayList<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
        db = FirebaseFirestore.getInstance();
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        int caloriesPerHour = exercise.getCaloriesPerHour();
        holder.nameTextView.setText(exercise.getName());
        holder.caloriesTextView.setText(String.valueOf(exercise.getCaloriesPerHour()) + " calories/hour");

        // Tải ảnh từ URL (hoặc để rỗng nếu không có ảnh)
        if (!exercise.getImg().isEmpty()) {
            Glide.with(context).load(exercise.getImg()).into(holder.exerciseImageView);
        } else {
            Glide.with(context).load(exercise.getImg()).into(holder.exerciseImageView); // Hình ảnh mặc định
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showTimerDialog(caloriesPerHour);
            }
        });
    }
    private void showTimerDialog(int caloriesPerHour) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        builder1.setMessage("Bạn muốn nhập thời gian hay bấm giờ?");
        builder1.setPositiveButton("Nhập thời gian",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LayoutInflater builder2 = LayoutInflater.from(context);
                        View dialogView = builder2.inflate(R.layout.dialog_add_time, null);
                        AlertDialog dialog2 = new AlertDialog.Builder(context).create();

                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                        dialog2.setView(dialogView);

                        EditText edtCalo = dialogView.findViewById(R.id.edtTime);
                        TextView txvTotal = dialogView.findViewById(R.id.txvTotal);
                        Button btnCal  = dialogView.findViewById(R.id.btnCalCalo),
                                btnSave = dialogView.findViewById(R.id.btnSave);

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String timeInput = edtCalo.getText().toString();
                                if (timeInput.isEmpty()) {
                                    edtCalo.setError("Vui lòng nhập thời gian");
                                } else {
                                    try {
                                        int time = Integer.parseInt(timeInput);
                                        if (time <= 0) {
                                            edtCalo.setError("Thời gian phải là số dương");
                                        } else {
                                            int calo = (int) caloriesPerHour * time / 60;
                                            Log.d(TAG, calo + "");
                                            setExercise(calo);
                                            Toast.makeText(context, "Đã lưu", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (NumberFormatException e) {
                                        edtCalo.setError("Vui lòng nhập số hợp lệ");
                                    }
                                }
                                dialogInterface.cancel();
                            }
                        });


                        btnCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String timeInput = edtCalo.getText().toString();
                                if (timeInput.isEmpty()) {
                                    edtCalo.setError("Vui lòng nhập thời gian");
                                } else {
                                    try {
                                        int time = Integer.parseInt(timeInput);
                                        if (time <= 0) {
                                            edtCalo.setError("Thời gian phải là số dương");
                                        } else {
                                            int calo = (int) caloriesPerHour * time / 60;
                                            txvTotal.setText(String.format(Locale.getDefault(), "%d", calo));
                                        }
                                    } catch (NumberFormatException e) {
                                        edtCalo.setError("Vui lòng nhập số hợp lệ");
                                    }
                                }
                            }
                        });
                        dialog2.show();
                    }
                });
        builder1.setNegativeButton("Bấm giờ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        // Khởi tạo layout cho dialog
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogView = inflater.inflate(R.layout.dialog_timer, null);

                        Button btnRestart = dialogView.findViewById(R.id.btnRestart);
                        ToggleButton btnStart = dialogView.findViewById(R.id.btnStart);
                        TextView txvCalorie = dialogView.findViewById(R.id.txvTimerCalories);
                        Chronometer chronometer = dialogView.findViewById(R.id.timerChronometer);


                        handler = new Handler();

                        builder.setNegativeButton("Hủy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i){
                                        dialogInterface.cancel();
                                    }
                                });
                        builder.setPositiveButton("Lưu kết quả",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "\n" +
                                                "Đã lưu", Toast.LENGTH_SHORT).show();
                                        int calo = Integer.valueOf(txvCalorie.getText().toString());
                                        setExercise(calo);
                                        dialogInterface.cancel();
                                    }
                                });
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                        dialog.show();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                tMilliSec = SystemClock.uptimeMillis() - tStart;
                                tUpdate = tBuff + tMilliSec;
                                sec = (int) (tUpdate / 1000);
                                min = sec / 60;
                                sec = sec % 60;
                                hour = min / 60;
                                min = min % 60;
                                milliSec = (int) (tUpdate % 1000);

                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Tính toán calories theo thời gian đã chạy
                                        int totalSeconds = (int) (tUpdate / 1000);
                                        int calo = (caloriesPerHour * totalSeconds) / 3600;
                                        txvCalorie.setText(String.valueOf(calo));

                                        // Định dạng hiển thị thời gian
                                        String time;
                                        if (hour > 0) {
                                            time = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hour, min, sec);
                                        } else {
                                            time = String.format(Locale.ENGLISH, "%02d:%02d", min, sec);
                                        }
                                        chronometer.setText(time);
                                    }
                                });
                                handler.postDelayed(this, 100); // Cập nhật mỗi 100 mili giây
                            }
                        };
                        btnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b){
                                    tStart = SystemClock.uptimeMillis();
                                    handler.postDelayed(runnable, 0);
                                    chronometer.start();
                                }
                                else{
                                    tBuff += tMilliSec;
                                    handler.removeCallbacks(runnable);
                                    chronometer.stop();
                                }
                            }
                        });
                        btnRestart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tMilliSec = 0;
                                tStart = 0L;
                                tBuff = 0L;
                                tUpdate = 0L;
                                sec = min = milliSec = hour = 0;

                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        chronometer.setText("00:00:00");
                                    }
                                });
                            }
                        });

                    }
                });
        AlertDialog dialog1 = builder1.create();
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog1.show();

    }
    private void setExercise(int calo) {
        LocalDate today  = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);

        DocumentReference userStatRef = db.collection("statistic").document(uID);
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(formattedDate);

        db.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(dailyDataRef);
                        if(snapshot.exists()){
                            if(snapshot.getLong("calories") == null){
                                transaction.set(dailyDataRef, new HashMap<String, Object>() {{
                                    put("calories", -calo);
                                }}, SetOptions.merge());
                                Log.d(TAG, "Chua co calories cho ngay hom nay, tao moi");
                            }
                            else{
                                long total = snapshot.getLong("calories");
                                long updateTotal = total - calo;
                                transaction.update(dailyDataRef, "calories", updateTotal);
                                Log.d(TAG, "Cap nhat exercise cho calories hom nay");
                            }
                            if(snapshot.getLong("exercise") == null){
                                transaction.set(dailyDataRef, new HashMap<String, Object>() {{
                                    put("exercise", calo);
                                }}, SetOptions.merge());
                            }
                            else{
                                long totalCalo = snapshot.getLong("exercise");
                                transaction.update(dailyDataRef, "exercise", totalCalo + calo);
                            }
                        }
                        return null;
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Cap nhat exercise cho hom nay thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, caloriesTextView;
        ImageView exerciseImageView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txvExercise);
            caloriesTextView = itemView.findViewById(R.id.txvCalories);
            exerciseImageView = itemView.findViewById(R.id.imvExercise);
        }
    }
}