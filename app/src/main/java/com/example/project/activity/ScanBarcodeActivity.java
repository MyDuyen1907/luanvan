package com.example.project.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.model.FoodProduct;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ScanBarcodeActivity extends AppCompatActivity {

    private Button btnScan,btnbackscan;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        btnScan = findViewById(R.id.btnScan);
        txtResult = findViewById(R.id.txtResult);
        btnbackscan = findViewById(R.id.btnbackscan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScanBarcodeActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Quét mã vạch");
                integrator.setCameraId(0); // camera trước
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        btnbackscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                txtResult.setText("Quét mã vạch bị hủy");
            } else {
                txtResult.setText(result.getContents());
                fetchFoodData(result.getContents()); // Lấy dữ liệu món ăn từ API
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fetchFoodData(String barcode) {
        new FetchFoodTask().execute(barcode);
    }

    private class FetchFoodTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String barcode = params[0];
            String jsonResult = "";
            try {
                URL url = new URL("https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                jsonResult = response.toString();
            } catch (Exception e) {
                Log.e("FetchFoodTask", "Error fetching food data", e);
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            displayFoodInfo(jsonData);
        }
    }

    private void displayFoodInfo(String jsonData) {
        Gson gson = new Gson();
        FoodProduct product = gson.fromJson(jsonData, FoodProduct.class);

        if (product.getProduct() != null) {
            String name = product.getProduct().getProductName();
            String nutrientInfo = "Thông tin dinh dưỡng: \n";

            // Lấy thông tin dinh dưỡng
            Map<String, Object> nutrients = product.getProduct().getNutrients();
            if (nutrients != null && !nutrients.isEmpty()) {
                if (nutrients.containsKey("energy-kcal")) {
                    nutrientInfo += "Calories: " + nutrients.get("energy-kcal") + " kcal\n";
                }
                if (nutrients.containsKey("proteins")) {
                    nutrientInfo += "Proteins: " + nutrients.get("proteins") + " g\n";
                }
                if (nutrients.containsKey("carbohydrates")) {
                    nutrientInfo += "Carbohydrates: " + nutrients.get("carbohydrates") + " g\n";
                }
                if (nutrients.containsKey("fat")) {
                    nutrientInfo += "Fat: " + nutrients.get("fat") + " g\n";
                }
            } else {
                nutrientInfo += "Không có thông tin dinh dưỡng cho mã vạch này.\n";
            }

            // Tạo nội dung cho dialog
            String result = "Tên món ăn: " + (name != null ? name : "Không có tên") + "\n" + nutrientInfo;

            // Hiển thị dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông tin sản phẩm")
                    .setMessage(result)
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Xóa nội dung TextView khi nhấn OK
                        txtResult.setText("");
                    })
                    .create()
                    .show();
        } else {
            // Nếu không tìm thấy sản phẩm
            new AlertDialog.Builder(this)
                    .setTitle("Lỗi")
                    .setMessage("Không tìm thấy thông tin cho mã vạch này")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Xóa nội dung TextView khi nhấn OK
                        txtResult.setText("");
                    })
                    .create()
                    .show();
        }
    }
}