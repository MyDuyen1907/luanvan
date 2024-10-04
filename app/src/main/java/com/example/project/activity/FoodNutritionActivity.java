package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.ApiClient;
import com.example.project.FoodDataCentralService;
import com.example.project.R;
import com.example.project.model.FoodResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodNutritionActivity extends AppCompatActivity {
    private static final String API_KEY = "wJWNKjpHsk4Q7a0g4uTTIJ8keNMEpivatgbYWC05";  // Thay YOUR_API_KEY bằng API Key của bạn
    private EditText editTextFood;
    private TextView textViewResult;
    private Button btnBackFoodNutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        editTextFood = findViewById(R.id.editTextFood);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button btnBackFoodNutrition = findViewById(R.id.btn_back_food_nutrition);

        buttonSearch.setOnClickListener(v -> {
            String foodName = editTextFood.getText().toString().trim();
            if (!foodName.isEmpty()) {
                fetchNutritionData(foodName);
            }
        });
        btnBackFoodNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchNutritionData(String foodName) {
        FoodDataCentralService service = ApiClient.getClient().create(FoodDataCentralService.class);
        Call<FoodResponse> call = service.searchFood(foodName, API_KEY);

        call.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodResponse.Food food = response.body().getFoods().get(0);  // Lấy món ăn đầu tiên
                    StringBuilder result = new StringBuilder("Món ăn: " + food.getDescription() + "\n\n");
                    for (FoodResponse.Nutrient nutrient : food.getFoodNutrients()) {
                        if (nutrient.getNutrientName().equalsIgnoreCase("Energy")
                                || nutrient.getNutrientName().equalsIgnoreCase("Protein")
                                || nutrient.getNutrientName().equalsIgnoreCase("Total lipid (fat)")
                                || nutrient.getNutrientName().equalsIgnoreCase("Carbohydrate, by difference")
                                || nutrient.getNutrientName().equalsIgnoreCase("Iron, Fe")
                                || nutrient.getNutrientName().equalsIgnoreCase("Vitamin A, RAE")
                                || nutrient.getNutrientName ().equalsIgnoreCase("Vitamin C, total ascorbic acid")
                                || nutrient.getNutrientName().equalsIgnoreCase("Sugars, total"))
                        {
                            result.append(nutrient.getNutrientName()).append(": ")
                                    .append(nutrient.getValue()).append(" ")
                                    .append(nutrient.getUnitName()).append("\n");
                        }
                    }
                    textViewResult.setText(result.toString());
                } else {
                    textViewResult.setText("Không tìm thấy thông tin dinh dưỡng.");
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                textViewResult.setText("Lỗi: " + t.getMessage());
            }
        });
    }
}
