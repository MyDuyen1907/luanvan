package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.activity.ReportActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
public class HealthFragment extends Fragment {

    private EditText inputBloodPressure, inputBloodPressurePP; // Tâm thu và tâm trương
    private EditText inputBloodSugar, inputBloodSugarPP; // Đường huyết lúc đói và lúc no
    private EditText inputCholesterol; // Cholesterol
    private BarChart barChartBloodPressure, barChartBloodSugar;
    private RadarChart radarChartCholesterol;
    private TextView alertText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        inputBloodPressure = view.findViewById(R.id.inputBloodPressure);
        inputBloodPressurePP = view.findViewById(R.id.inputBloodPressurePP);
        inputBloodSugar = view.findViewById(R.id.inputBloodSugar);
        inputBloodSugarPP = view.findViewById(R.id.inputBloodSugarPP);
        inputCholesterol = view.findViewById(R.id.inputCholesterol);
        barChartBloodPressure = view.findViewById(R.id.barChartBloodPressure);
        barChartBloodSugar = view.findViewById(R.id. barChartBloodSugar);
        radarChartCholesterol = view.findViewById(R.id.radarChartCholesterol);
        alertText = view.findViewById(R.id.alertText);
        Button btnSaveData = view.findViewById(R.id.btnSaveData);
        Button btnViewReport = view.findViewById(R.id.btnViewReport);

        btnViewReport.setOnClickListener(v -> viewReport());


        btnSaveData.setOnClickListener(v -> saveData());

        return view;
    }

    private void saveData() {
        String systolicStr = inputBloodPressure.getText().toString().trim(); // Tâm thu
        String diastolicStr = inputBloodPressurePP.getText().toString().trim(); // Tâm trương
        String bloodSugarStr = inputBloodSugar.getText().toString().trim(); // Đường huyết lúc đói
        String bloodSugarPPStr = inputBloodSugarPP.getText().toString().trim(); // Đường huyết lúc no
        String cholesterolStr = inputCholesterol.getText().toString().trim(); // Cholesterol

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(systolicStr) || TextUtils.isEmpty(diastolicStr) || TextUtils.isEmpty(bloodSugarStr)
                || TextUtils.isEmpty(bloodSugarPPStr) || TextUtils.isEmpty(cholesterolStr)) {
            alertText.setText("Vui lòng nhập tất cả dữ liệu.");
            return;
        }

        float systolic = Float.parseFloat(systolicStr);
        float diastolic = Float.parseFloat(diastolicStr);
        float bloodSugar = Float.parseFloat(bloodSugarStr);
        float bloodSugarPP = Float.parseFloat(bloodSugarPPStr);
        float cholesterol = Float.parseFloat(cholesterolStr);

        // Cập nhật biểu đồ huyết áp
        updateBloodPressureChart(systolic, diastolic);

        // Cập nhật biểu đồ đường huyết


        updateBloodSugarCharts(bloodSugar, bloodSugarPP);

        // Cập nhật biểu đồ cholesterol
        updateCholesterolChart(cholesterol);

        // Kiểm tra trạng thái sức khỏe
        checkHealthStatus(systolic, diastolic, bloodSugar, bloodSugarPP, cholesterol);
    }

    private void updateBloodPressureChart(float systolic, float diastolic) {
        ArrayList<BarEntry> systolicEntries = new ArrayList<>();
        ArrayList<BarEntry> diastolicEntries = new ArrayList<>();

        // Thêm giá trị vào từng danh sách
        systolicEntries.add(new BarEntry(0, systolic)); // Tâm thu
        diastolicEntries.add(new BarEntry(1, diastolic)); // Tâm trương

        // Tạo dữ liệu cho huyết áp tâm thu
        BarDataSet systolicDataSet = new BarDataSet(systolicEntries, "Tâm thu");
        systolicDataSet.setColor(getResources().getColor(R.color.red)); // Màu đỏ cho tâm thu
        systolicDataSet.setValueTextColor(getResources().getColor(R.color.black)); // Màu chữ

        // Tạo dữ liệu cho huyết áp tâm trương
        BarDataSet diastolicDataSet = new BarDataSet(diastolicEntries, "Tâm trương");
        diastolicDataSet.setColor(getResources().getColor(R.color.yellow)); // Màu vàng cho tâm trương
        diastolicDataSet.setValueTextColor(getResources().getColor(R.color.black)); // Màu chữ

        // Kết hợp hai DataSet vào BarData
        BarData data = new BarData(systolicDataSet, diastolicDataSet);

        // Đặt dữ liệu vào biểu đồ và làm mới
        barChartBloodPressure.setData(data);
        barChartBloodPressure.invalidate(); // Refresh biểu đồ
        barChartBloodPressure.getDescription().setEnabled(false);
    }



    private void updateBloodSugarCharts(float systolic, float diastolic) {
        ArrayList<BarEntry> systolicEntries = new ArrayList<>();
        ArrayList<BarEntry> diastolicEntries = new ArrayList<>();

        systolicEntries.add(new BarEntry(0, systolic));
        diastolicEntries.add(new BarEntry(1, diastolic));


        BarDataSet systolicDataSet = new BarDataSet(systolicEntries, "Đường huyết lúc đói");
        systolicDataSet.setColor(getResources().getColor(R.color.green_main));
        systolicDataSet.setValueTextColor(getResources().getColor(R.color.black));

        BarDataSet diastolicDataSet = new BarDataSet(diastolicEntries, "Đường huyết lúc no");
        diastolicDataSet.setColor(getResources().getColor(R.color.orange));
        diastolicDataSet.setValueTextColor(getResources().getColor(R.color.black));

        // Kết hợp hai DataSet vào BarData
        BarData data = new BarData(systolicDataSet, diastolicDataSet);

        // Đặt dữ liệu vào biểu đồ và làm mới
        barChartBloodSugar.setData(data);
        barChartBloodSugar.invalidate(); // Refresh biểu đồ
        barChartBloodSugar.getDescription().setEnabled(false);
    }

    private void updateCholesterolChart(float cholesterol) {
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        radarEntries.add(new RadarEntry(cholesterol)); // Sử dụng RadarEntry thay vì Entry

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "Cholesterol");
        radarDataSet.setColor(getResources().getColor(R.color.green_main)); // Màu sắc cho biểu đồ
        radarDataSet.setValueTextColor(getResources().getColor(R.color.black)); // Màu sắc cho giá trị

        RadarData radarData = new RadarData(radarDataSet);
        radarChartCholesterol.setData(radarData);
        radarChartCholesterol.invalidate(); // Refresh biểu đồ
        radarChartCholesterol.getDescription().setEnabled(false);
    }
    private void viewReport() {
        // Chuyển đến Activity hoặc Fragment hiển thị báo cáo
        // Sử dụng Intent để chuyển dữ liệu nếu cần
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra("systolic", inputBloodPressure.getText().toString().trim());
        intent.putExtra("diastolic", inputBloodPressurePP.getText().toString().trim());
        intent.putExtra("bloodSugar", inputBloodSugar.getText().toString().trim());
        intent.putExtra("bloodSugarPP", inputBloodSugarPP.getText().toString().trim());
        intent.putExtra("cholesterol", inputCholesterol.getText().toString().trim());
        startActivity(intent);
    }


    private void checkHealthStatus(float systolic, float diastolic, float bloodSugar, float bloodSugarPP, float cholesterol) {
        StringBuilder status = new StringBuilder();

        // Cảnh báo huyết áp
        if (systolic < 120 && diastolic < 80) {
            status.append("Huyết áp bình thường (Dưới 120/80 mmHg).\n");
        } else if (systolic <= 129 && diastolic < 80) {
            status.append("Huyết áp bình thường cao (120-129/80 mmHg).\n");
        } else if (systolic >= 130 || diastolic >= 80) {
            status.append("Huyết áp cao (≥ 130/80 mmHg). Cần chú ý đến sức khỏe tim mạch.\n");
        }

        // Cảnh báo đường huyết lúc đói
        if (bloodSugar < 100) {
            status.append("Đường huyết bình thường lúc đói (Dưới 100 mg/dL).\n");
        } else if (bloodSugar < 126) {
            status.append("Đường huyết cao lúc đói (100-125 mg/dL). Cần theo dõi.\n");
        } else {
            status.append("Đường huyết cao lúc đói (≥ 126 mg/dL). Nên kiểm tra tình trạng tiểu đường.\n");
        }

        // Cảnh báo đường huyết lúc no
        if (bloodSugarPP < 140) {
            status.append("Đường huyết bình thường lúc no (Dưới 140 mg/dL).\n");
        } else if (bloodSugarPP < 200) {
            status.append("Đường huyết cao lúc no (140-199 mg/dL). Cần theo dõi.\n");
        } else {
            status.append("Đường huyết cao lúc no (≥ 200 mg/dL). Nên kiểm tra tình trạng tiểu đường.\n");
        }

        // Cảnh báo cholesterol
        if (cholesterol < 200) {
            status.append("Cholesterol bình thường (Dưới 200 mg/dL).\n");
        } else if (cholesterol < 240) {
            status.append("Cholesterol cao (200-239 mg/dL). Cần chú ý.\n");
        } else {
            status.append("Cholesterol rất cao (≥ 240 mg/dL). Cần có chế độ ăn uống hợp lý và kiểm tra sức khỏe.\n");
        }

        // Cập nhật văn bản cảnh báo
        alertText.setText(status.toString());
    }

}