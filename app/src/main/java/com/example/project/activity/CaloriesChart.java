package com.example.project.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.project.model.User;
import com.example.project.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CaloriesChart extends AppCompatActivity {
    CardView cv1Calo, cv4Calo;
    LinearLayout cv2Calo, cv3Calo ;
    User u;
    FirebaseFirestore db;
    DocumentReference userStatRef;
    CollectionReference collectionRef;
    QuerySnapshot dailySnapshot;
    List <String> date_label = new ArrayList<>();
    List <Long> calories = new ArrayList<>();
    RelativeLayout relativeLayout;
    BarChart barChart;
    LineChart lineChart;
    TextView txvStreak;
    TextView txv7calo, txv30calo, txv7thamHut, txv30thamHut,
            txvTotalDate, txvCurrentStreak,
            txvBigTdee, txvSmallTdee,
            txvWarning;
    Button btnBack;
    long tdee;
    int animationDuration = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_chart);

        findView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        u = (User) getIntent().getSerializableExtra("user");
        db = FirebaseFirestore.getInstance();
        userStatRef = db.collection("statistic").document(u.getId());
        collectionRef = userStatRef.collection("dailyData");

//        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
//                setAnimation();
//            }, 50);

        getDataStringList(date_label ->{
            populateChart();
            setAnimation();
            showLongestStreak(date_label);
            showTotalDate_CurrentStreak();
            showTotal();
        });

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        if(hasFocus) setAnimation();
//    }

    private void setAnimation() {


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cv1Calo, "translationX", -cv1Calo.getWidth(), 0);
        animator1.setDuration(animationDuration);
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cv2Calo, "translationX", -cv2Calo.getWidth(), 0);
        animator2.setDuration(animationDuration);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(cv3Calo, "translationX", -cv3Calo.getWidth(), 0);
        animator3.setDuration(animationDuration);
        animator3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(cv4Calo, "translationX", -cv4Calo.getWidth(), 0);
        animator4.setDuration(animationDuration);
        animator4.setInterpolator(new AccelerateDecelerateInterpolator());

        animator1.start();

        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
            animator2.start();
        }, 200);

        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
            animator3.start();
        }, 400);

        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
            animator4.start();
        }, 600);
    }


    private void showTotalDate_CurrentStreak() {
        int totalDate = date_label.size();
        txvTotalDate.setText(Integer.toString(totalDate));

        int currentStreak = getCurrentStreak();
        txvCurrentStreak.setText(Integer.toString(currentStreak));
    }

    private int getCurrentStreak() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Collections.sort(date_label, (date1, date2) -> {
            LocalDate localDate1 = LocalDate.parse(date1, formatter);
            LocalDate localDate2 = LocalDate.parse(date2, formatter);
            return localDate1.compareTo(localDate2);
        });

        LocalDate previousDate = LocalDate.now();
        String preDate = formatter.format(previousDate);
        int index = -1;
        if(date_label.contains(preDate)) index = date_label.indexOf(preDate);

        int length = 0;

        if(index == -1){
            String tempPreDate = formatter.format(previousDate.minusDays(1));
            if(date_label.contains(tempPreDate))
                index = date_label.indexOf(preDate);
            else
                return 0;
        }

        for (int i = index - 1; i >= 0; i--) {
            LocalDate entryDate = LocalDate.parse(date_label.get(i), formatter);
            if(previousDate.minusDays(1).equals(entryDate)){
                length++;
                previousDate = entryDate;
            }
        }

        return length + 1;
    }

    private void showTotal() {
        int cntBig = 0, cntSmall = 0;
        for(long i: calories){
            if(i < tdee)
                cntSmall ++;
            else
                cntBig ++;
        }
        txvBigTdee.setText(Long.toString(cntBig));
        txvSmallTdee.setText(Long.toString(cntSmall));
    }



    private long getTotalCaloriesLastThirtyDays(List<String> date, List<Long> calories) {
        long totalCalories = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate currentDate = LocalDate.now();

        for (int i = date.size() - 1; i >= 0 && i >= date.size() - 30; i--) {
            try {
                LocalDate entryDate = LocalDate.parse(date.get(i), formatter);
                long entryCalories = calories.get(i);

                if (currentDate.minusDays(30).isBefore(entryDate)) {
                    totalCalories += entryCalories - tdee;
                }
            } catch (Exception e) {
                System.err.println("Error parsing date: " + date.get(i));
                Log.e(TAG, e.toString());
            }
        }

        return totalCalories;
    }

    private long getTotalCaloriesLastSevenDays(List<String> date, List<Long> calories) {
        long totalCalories = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate currentDate = LocalDate.now();

        for (int i = date.size() - 1; i >= 0 && i >= date.size() - 7; i--) {
            try {
                LocalDate entryDate = LocalDate.parse(date.get(i), formatter);
                long entryCalories = calories.get(i);

                if (currentDate.minusDays(7).isBefore(entryDate)) {
                    totalCalories += entryCalories - tdee;
                }
            } catch (DateTimeParseException e) {
                // Handle parsing exception if the date format is invalid
                System.err.println("Error parsing date: " + date.get(i));
                Log.e(TAG, e.toString());
            }
        }

        return totalCalories;
    }


    private void showLongestStreak(List<String> date) {
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Convert strings to date objects
        for (String i : date) {
            try {
                Date dateObject = dateFormat.parse(i);
                dates.add(dateObject);
            } catch (ParseException e) {
                Log.e(TAG, e.toString());
                return;
            }
        }

        dates = findLongestStreak(dates);

        txvStreak.setText(Integer.toString(dates.size()));
        System.out.println(dates);

    }

    private List<Date> findLongestStreak(List<Date> dates) {
        int maxLength = 0;
        int startIndex = 0;
        int endIndex = 0;
        int currentStartIndex = 0;
        int currentLength = 1;

        for (int i = 1; i < dates.size(); i++) {
            long diff = dates.get(i).getTime() - dates.get(i - 1).getTime();
            if (diff == 86400000L) { // Difference of one day (24 * 60 * 60 * 1000 ms)
                currentLength++;
            } else {
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    startIndex = currentStartIndex;
                    endIndex = i - 1;
                }
                currentStartIndex = i;
                currentLength = 1;
            }
        }

        // Check if the last sequence is the longest
        if (currentLength > maxLength) {
            maxLength = currentLength;
            startIndex = currentStartIndex;
            endIndex = dates.size() - 1;
        }

        // Extract the longest sequence
        List<Date> longestSequence = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            longestSequence.add(dates.get(i));
        }

        return longestSequence;
    }

    private interface chartDataCallBack{
        void onCallBack(List <String> list);
    }

    private void populateChart() {

        // Get the user's TDEE (replace with your actual TDEE calculation)
        int tdee = u.TTDECal();


        List<Entry> dateEntries = new ArrayList<>();
        // set date line chart data
        for (int i = 0; i < calories.size(); i++) {
            dateEntries.add(new Entry(i, calories.get(i)));
        }

        LineDataSet dateData = new LineDataSet(dateEntries, "Calories");
        dateData.setAxisDependency(YAxis.AxisDependency.LEFT);
        dateData.setColor(ContextCompat.getColor(this, R.color.green_main));
        dateData.setLineWidth(2f);
        dateData.setCircleColor(ContextCompat.getColor(this, R.color.green_main));
        dateData.setCircleRadius(4f);
        dateData.setDrawCircleHole(false);

        // set tdee line chart data
        List <Entry> lineEntry =  new ArrayList<>();
        for(int i = 0; i < date_label.size(); i ++){
            lineEntry.add(new Entry(i, tdee));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntry, "TDEE");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ContextCompat.getColor(this, R.color.orange));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.orange));
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawCircleHole(false);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        dataSets.add(dateData);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        List <String> labels = date_label.stream().map(date -> date.substring(0, 5)).collect(Collectors.toList());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(tdee*2f);


        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        Description lineDesc = lineChart.getDescription();
        lineDesc.setEnabled(false);
        lineChart.invalidate();

    }

    private void getDataStringList(chartDataCallBack callBack) {
        //  dailySnapshot = ; // list of queryDocumentSnapshot

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult().getDocuments()){
                        Map <String, Object> data = doc.getData();
                        if(data != null){
                            if(data.containsKey("calories"))
                            {
                                date_label.add(doc.getId());
                                calories.add((long) data.get("calories"));
                                System.out.println(doc.getId());
                            }
                        }
                    }
                    callBack.onCallBack(date_label);
                }
            }
        });
    }

    private void findView() {
        relativeLayout = findViewById(R.id.chartContainer);
        lineChart = findViewById(R.id.lineChart);
        txvStreak = findViewById(R.id.txvStreak);
        txvTotalDate = findViewById(R.id.txvTotalDate);
        txvCurrentStreak = findViewById(R.id.txvCurrentStreak);
        txvBigTdee = findViewById(R.id.txvBigTdee);
        txvSmallTdee = findViewById(R.id.txvSmallTdee);
        btnBack = findViewById(R.id.btnBackChart);
        cv1Calo = findViewById(R.id.cv1Calo);
        cv2Calo = findViewById(R.id.cv2Calo);
        cv3Calo = findViewById(R.id.cv3Calo);
        cv4Calo = findViewById(R.id.cv4Calo);

        cv1Calo.setTranslationX(-10000);
        cv2Calo.setTranslationX(-10000);
        cv3Calo.setTranslationX(-10000);
        cv4Calo.setTranslationX(-10000);

    }

}